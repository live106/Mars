/**
 * 
 */
package com.live106.mars.game.rpc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.game.service.PlayerService;
import com.live106.mars.protocol.thrift.ResponseGameConnect;
import com.live106.mars.protocol.thrift.ReuqestGameConnect;
import com.live106.mars.protocol.thrift.game.IGamePlayerService.Iface;
import com.live106.mars.protocol.thrift.game.MessagePlayerSecureInfo;

/**
 * @author live106 @creation Oct 23, 2015
 *
 */
@Service
public class GamePlayerServiceRpc implements Iface {
	
	@Autowired
	private PlayerService playerService;

	@Override
	public String ping(String visitor) throws TException {
		return "hello " + visitor;
	}

	@Override
	public boolean setPlayerSecureKey(MessagePlayerSecureInfo secureInfo) throws TException {
		boolean result = playerService.addPlayerSecureInfo(secureInfo);
		return result;
	}

	@Override
	public ResponseGameConnect clientLogin(ReuqestGameConnect request) throws TException {
		ResponseGameConnect resp = new ResponseGameConnect();
		try {
			boolean result = playerService.checkUserConnect(request);
			resp.setResult(result);
			if (result) {
				//����Ƿ����н�ɫ
				//���������ɫ����,����ɫid
				resp.setMsg("������Ϸ�������ɹ���");
			} else {
				resp.setMsg("ͨ��֤��Ч�������µ�¼��");
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}
}
