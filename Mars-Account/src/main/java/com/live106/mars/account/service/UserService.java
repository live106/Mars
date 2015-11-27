/**
 * 
 */
package com.live106.mars.account.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.live106.mars.account.bean.UserPassport;
import com.live106.mars.account.db.mapper.UserMapper;
import com.live106.mars.account.db.model.User;
import com.live106.mars.account.db.model.UserExample;
import com.live106.mars.account.redis.IRedisConstants;
import com.live106.mars.account.redis.UserRedis;
import com.live106.mars.protocol.util.Cryptor;
import com.live106.mars.protocol.util.ProtocolCrypto;
import com.live106.mars.util.LoggerHelper;

/**
 * <h1>账号服务类</h1>
 * <ol>
 * <li>秘钥生成、管理</li>
 * <li>登录校验</li>
 * </ol>
 * <br>
 * @author live106 @creation Oct 8, 2015
 */

@Service
public class UserService implements IRedisConstants {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserMapper userDao;
	@Autowired
	private UserRedis userRedis;
	@Autowired
	private RpcClientServiceFactory rpcClientServiceFactory;
	
	/* DH + AES start */ 
	private Map<String, Object> dhServerKeyMap;
	private Map<String, Byte[]> dhAesSecretKeys = new ConcurrentHashMap<>();

	private String sdServerPublicKeyString;
	/* DH + AES end*/
	
	/** AES key for user login */
	private static Map<String, String> aesKeyMap = new ConcurrentHashMap<>();//TODO  remove data of aesKeyMap in a proper moment.
	
	public boolean exist(String name) {
		UserExample userExample = new UserExample();
		userExample.createCriteria().andUsernameEqualTo(name);
		
		int count = userDao.countByExample(userExample);
		return count > 0;
	}
	
	public boolean checkPassword(String name, String password) {
		UserExample userExample = new UserExample();
		userExample.createCriteria().andUsernameEqualTo(name).andPasswordEqualTo(password);
		
		int count = userDao.countByExample(userExample);
		return count > 0;
	}
	
	public User getUser(String name) {
		return userDao.selectByUsername(name);
	}
	
	public String getPassword(String name) {
		return userDao.getPassword(name);
	}

	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}

	//Solution 1. start //
	//DH exchange public key
	//generate secure key with the private key
	//encrypt data in communication by the secure key
	
	/**
	 * lazy generate DH algorithm key pair and get the public key 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public synchronized byte[] getServerPublicKey() throws NoSuchAlgorithmException {
		if (dhServerKeyMap == null || dhServerKeyMap.size() == 0) {
			dhServerKeyMap = ProtocolCrypto.generateKey();
		}
		
		return ProtocolCrypto.getPublicKey(dhServerKeyMap);
	}
	
	/**
	 * encode server DH public key by Base64
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public synchronized String getServerPublicKeyBase64() throws NoSuchAlgorithmException {
		if (sdServerPublicKeyString == null) {
			sdServerPublicKeyString = Base64.getEncoder().encodeToString(getServerPublicKey());
		}
		return sdServerPublicKeyString;
	}

	/**
	 * generate AES secure key with the client public key and server private key, store in the map
	 * @param channelId
	 * @param clientPubKey
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IllegalStateException
	 */
	public void generateSecretKey(String channelId, String clientPubKey) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalStateException {
		byte[] clientPubKeyData = Base64.getDecoder().decode(clientPubKey);
		byte[] secretKey = ProtocolCrypto.getSecretKey(clientPubKeyData, ProtocolCrypto.getPrivateKey(dhServerKeyMap));
		dhAesSecretKeys.put(channelId, ArrayUtils.toObject(secretKey));
	}

	/**
	 * decrypt the string encrypted by AES with the secure key.
	 * @param channelId
	 * @param encrytedString
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public String decrypt(String channelId, String encrytedString) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] secretKey = ArrayUtils.toPrimitive(dhAesSecretKeys.get(channelId));
		byte[] decryptData = ProtocolCrypto.decrypt(Base64.getDecoder().decode(encrytedString), secretKey);
		return new String(decryptData);
	}
	
	//Solution 1. end.//
	
	//Solution 2. start //
	public void storeClientAesKey(String channelId, String key) {
		aesKeyMap.put(channelId, key);
	}
	
	public String delUserClientKey(String channelId) {
		return aesKeyMap.remove(channelId);
	}
	
	/**
	 * decrypt the AES encrypted data from client
	 * @param channelId
	 * @param data
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws TimeoutException 
	 */
	public String decryptAES(String channelId, String data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, TimeoutException {
		String key = aesKeyMap.get(channelId);
		
		if (key == null) {
			throw new TimeoutException("key is not found, maybe out-of-date.");
		}
		
		Cryptor cryptor = new Cryptor(Cryptor.AES);
		cryptor.setSecretKey(key);
		byte[] decryptData = cryptor.doCrypt(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), Cipher.DECRYPT_MODE);
		
		return new String(decryptData);
	}
	
	//Solution 2. end. //

	/**
	 * generate passport identify for the login user
	 * @param user
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException 
	 */
	public UserPassport generatePassport(User user) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		StringBuilder sb = new StringBuilder();
		sb.append(user.getId());
		sb.append(user.getUsername());
		sb.append(user.getCreateday().toString());
		
		String identity = DigestUtils.md5Hex(sb.toString());
		
		UserPassport passport = new UserPassport();
		passport.setIdentify(identity);
		passport.setSecureKey(generateGameServerSecureKey(user));
		
		return passport;
	}
	
	public UserPassport generatePassport(int userid, String username, String createtime) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		StringBuilder sb = new StringBuilder();
		sb.append(userid);
		sb.append(username);
		sb.append(createtime);
		
		String identity = DigestUtils.md5Hex(sb.toString());
		
		UserPassport passport = new UserPassport();
		passport.setIdentify(identity);
		passport.setSecureKey(generateGameServerSecureKey(userid));
		
		return passport;
	}
	
	/**
	 * generate AES secure key for game server
	 * @param user
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException
	 */
	private String generateGameServerSecureKey(User user) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		Cryptor cryptor = new Cryptor(Cryptor.AES);
		cryptor.generateKey();
		String secureKey = cryptor.getSecretKey();
		return secureKey;
	}
	
	private String generateGameServerSecureKey(int userid) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		Cryptor cryptor = new Cryptor(Cryptor.AES);
		cryptor.generateKey();
		String secureKey = cryptor.getSecretKey();
		return secureKey;
	}

	public boolean isValidMachineId(String machineId) {
		return (machineId != null) && (machineId.length() >= 32) || true;
	}

	/**
	 * 机器码登录
	 * @param machineId
	 * @return
	 */
	public Map<String, String> loginByMachineId(String machineId) {
		Map<String, String> usermap = null;
		long userId = userRedis.getUserIdByMachineId(machineId);
		if (userId > 0) {
			usermap = userRedis.getUser(String.format("%s:%d", field_user_prefix, userId));
		} else {
			usermap = userRedis.addUser(machineId);
			userId = userRedis.getUserIdByMachineId(machineId);
		}
		usermap.put(additional_field_userid, String.valueOf(userId));
		
		return usermap;
	}

	/**
	 * SDK登录
	 * @param channel
	 * @param sdkChannel
	 * @param uid
	 * @param serverId
	 * @param pluginId
	 * @return
	 */
	public Map<String, String> doLoginBySDK(String sdkChannel, String sdkUid) {
		Map<String, String> usermap = null;
		long userId = userRedis.getUserIdBySDK(sdkChannel, sdkUid);
		if (userId > 0) {
			usermap = userRedis.getUser(String.format("%s:%d", field_user_prefix, userId));
			LoggerHelper.info(logger, ()->String.format("SDK已映射用户登录userSDK:%s, sdkUid:%s", sdkChannel, sdkUid));
		} else {
			usermap = userRedis.addUserBySDK(sdkChannel, sdkUid);
			userId = userRedis.getUserIdBySDK(sdkChannel, sdkUid);
			LoggerHelper.info(logger, ()->String.format("SDK登录增加用户映射userSDK:%s, sdkUid:%s", sdkChannel, sdkUid));
		}
		usermap.put(additional_field_userid, String.valueOf(userId));
		
		return usermap;
	}
	
	/**
	 * <h2>支付回调触发该方法</h2>
	 * <p>保存支付数据</p>
	 * <span>回调传递参数示例</span>
	 * <p>
	 * 			 "amount=1" + 
				 "channel_number=999999" +
				 "game_user_id=1" + 
				 "game_id=" + 
				 "order_id=PB069714081310394265014" + 
				 "order_type=999" + 
				 "pay_status 1" + 
				 "pay_time 2014-08-13 10:39:43" + 
				 "private_data=" + 
				 "product_count=0" + 
				 "product_id=jinbi" + 
				 "product_name=gold" + 
				 "server_id=13" + 
				 "source={\"amount\":\"1\",\"app_id\":\"1738\",\"cp_order_id\":\"PB069714081310394265014\",\"ext1\":\"\",\"ext2\":\"\",\"trans_id\":\"20282\",\"trans_status\":\"1\",\"user_id\":\"1799\",\"sign\":\"08dfe21e1f4f26e334ec3b9b7f419b731dcd8255\"}" + 
				 "user_id=1799";
		</p>
	 * 
	 * @param json
	 */
	public void pay(JsonObject json) {
		String sdkUid = json.get("user_id").getAsString();
		String amount = json.get("amount").getAsString();
		String sdkChannel = json.get("channel_number").getAsString();
		long userId = userRedis.getUserIdBySDK(sdkChannel, sdkUid);
		if (userId < 0) {
			LoggerHelper.info(logger, ()->String.format("用户未找到sdkUid:%d, 请求支付%s", sdkUid, json.toString()));
			return;
		}
		userRedis.addUserPayLog(userId, json.toString());
		userRedis.addUserPay(userId, amount);
		
		String orderId = json.get("order_id").getAsString();
		//通知游戏服务器
		try {
			rpcClientServiceFactory.getGamePlayerService().pay(userId, Integer.valueOf(amount).intValue(), orderId, json.toString());
		} catch (TException e) {
			e.printStackTrace();
		}
	}

	public RpcClientServiceFactory getRpcClientServiceFactory() {
		return rpcClientServiceFactory;
	}

	public void setRpcClientServiceFactory(RpcClientServiceFactory rpcClientServiceFactory) {
		this.rpcClientServiceFactory = rpcClientServiceFactory;
	}

}
