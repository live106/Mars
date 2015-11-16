/**
 * 
 */
package com.live106.mars.game.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.game.db.mapper.PlayerMapper;
import com.live106.mars.game.db.model.Player;
import com.live106.mars.game.redis.GameSecureRedis;
import com.live106.mars.protocol.thrift.Notify;
import com.live106.mars.protocol.thrift.ReuqestGameConnect;
import com.live106.mars.protocol.thrift.game.MessageUserSecureInfo;
import com.live106.mars.protocol.util.Cryptor;
import com.live106.mars.util.LoggerHelper;

/**
 * @author live106 @creation Oct 8, 2015
 *
 */

@Service
public class PlayerService {
	
	private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private GameSecureRedis gameSecureRedis;

	public Player getPlayer(int id) {
		return playerMapper.selectByPrimaryKey(id);
	}

	public void setPlayerMapper(PlayerMapper playerMapper) {
		this.playerMapper = playerMapper;
	}

	public boolean addPlayerSecureInfo(MessageUserSecureInfo secureInfo) throws TException {
//		return gameCacheManager.addPlayerSecureInfo(secureInfo);
		return gameSecureRedis.storeUserSecureInfo(secureInfo);
	}

	public boolean checkUserConnect(ReuqestGameConnect request) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, TException {
//		MessageUserSecureInfo secureInfo = gameCacheManager.getPlayerSecureInfo(request.getUid());
		MessageUserSecureInfo secureInfo = gameSecureRedis.loadUserSecureInfo(request.getUid());
		boolean result = false;
		do {
			if (secureInfo == null) {
				throw new Notify("not login or session timeout!");
			}
			//FIXME check gameserver field first.

			if (!request.getPassport().equals(secureInfo.getPassport())) {
				throw new Notify("uid and passport not match!");
			}
			
			String checkString = String.format("%s%d%d", request.getGameserver(), request.getUid(), request.getSequenceId());
			Cryptor cryptor = new Cryptor(Cryptor.AES);
			cryptor.setSecretKey(secureInfo.getSecureKey());
			byte[] encryptData = cryptor.doCrypt(checkString.getBytes(), Cipher.ENCRYPT_MODE);
			
			String base64Data = Base64.getEncoder().encodeToString(encryptData);
			if (!request.getRandomKey().equals(base64Data)) {
				Notify exception = new Notify("encrypted randomkey not match!");
				LoggerHelper.error(logger, ()->String.format("User %d check randomKey %s\n%s\n-------------\n%s error.", request.getUid(), checkString, request.getRandomKey(), base64Data), exception);
				throw exception;
			} else {
				LoggerHelper.debug(logger, ()->String.format("User %d check randomKey %s\n%s OK.", request.getUid(), checkString, base64Data));
			}
			
			result = true;
			
		} while (false);
		
		return result;
	}
	
}
