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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.account.bean.UserPassport;
import com.live106.mars.account.db.mapper.UserMapper;
import com.live106.mars.account.db.model.User;
import com.live106.mars.account.db.model.UserExample;
import com.live106.mars.account.redis.IRedisConstants;
import com.live106.mars.account.redis.UserRedis;
import com.live106.mars.protocol.util.Cryptor;
import com.live106.mars.protocol.util.ProtocolCrypto;

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
	
	@Autowired
	private UserMapper userDao;
	@Autowired
	private UserRedis userRedis;
	
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
	public String aesDecrypt(String channelId, String data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, TimeoutException {
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

}
