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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.cache.game.GameCacheManager;
import com.live106.mars.game.db.mapper.PlayerMapper;
import com.live106.mars.game.db.model.Player;
import com.live106.mars.protocol.thrift.ReuqestGameConnect;
import com.live106.mars.protocol.thrift.game.MessagePlayerSecureInfo;
import com.live106.mars.protocol.util.Cryptor;

/**
 * @author live106 @creation Oct 8, 2015
 *
 */

@Service
public class PlayerService {

	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private GameCacheManager gameCacheManager;

	public Player getPlayer(int id) {
		return playerMapper.selectByPrimaryKey(id);
	}

	public void setPlayerMapper(PlayerMapper playerMapper) {
		this.playerMapper = playerMapper;
	}

	public void setGameCacheManager(GameCacheManager gameCacheManager) {
		this.gameCacheManager = gameCacheManager;
	}

	public boolean addPlayerSecureInfo(MessagePlayerSecureInfo secureInfo) {
		return gameCacheManager.addPlayerSecureInfo(secureInfo);
	}

	public boolean checkUserConnect(ReuqestGameConnect request) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		MessagePlayerSecureInfo secureInfo = gameCacheManager.getPlayerSecureInfo(request.getUid());
		boolean result = false;
		do {
			if (secureInfo == null) {
				break;
			}
			//FIXME check gameserver field first.

			if (!request.getPassport().equals(secureInfo.getPassport())) {
				break;
			}
			String checkString = String.format("%s%d%d", request.getGameserver(), request.getUid(), request.getSequenceId());
			Cryptor cryptor = new Cryptor(Cryptor.AES);
			cryptor.setSecretKey(secureInfo.getSecureKey());
			byte[] encryptData = cryptor.doCrypt(checkString.getBytes(), Cipher.ENCRYPT_MODE);
			if (!request.getRandomKey().equals(Base64.getEncoder().encodeToString(encryptData))) {
				break;
			}
			
			result = true;
			
		} while (false);
		
		return result;
	}

}
