package com.live106.mars.master.processor;

import java.util.UUID;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.live106.mars.master.service.MasterService;
import com.live106.mars.protocol.handler.ProtocolProcessor;
import com.live106.mars.protocol.handler.annotation.Processor;
import com.live106.mars.protocol.handler.annotation.ProcessorMethod;
import com.live106.mars.protocol.pojo.ProtocolBase;
import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.thrift.LoginCode;
import com.live106.mars.protocol.thrift.RequestServerLogin;
import com.live106.mars.protocol.thrift.RequestUserLogin;
import com.live106.mars.protocol.thrift.ResponseServerLogin;
import com.live106.mars.protocol.thrift.ResponseUserLogin;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Service
@Processor
public class MasterProcessor implements ProtocolProcessor {
	
	@Autowired
	private MasterService masterService;

	@ProcessorMethod(messageClass = RequestServerLogin.class)
	public ProtocolBase severRegister(ChannelHandlerContext context, RequestServerLogin request) throws TException {
		boolean result = masterService.registerAndOverwrite(context, request);
		
		ResponseServerLogin resp = new ResponseServerLogin();
		resp.setResult(result);
		resp.setMsg(String.format("registered to the master return %s !", result));
		
		return ProtocolSerializer.serialize(resp, new ProtocolPeer2Peer());
	}
	
	@ProcessorMethod(messageClass = RequestUserLogin.class)
	public ProtocolBase register(ChannelHandlerContext context, RequestUserLogin request) throws TException {
		
		ResponseUserLogin resp = new ResponseUserLogin();
		resp.setCode(LoginCode.OK);
		resp.setMsg(UUID.randomUUID().toString());
		
		return ProtocolSerializer.serialize(resp, new ProtocolPeer2Peer());
	}

	public void setMasterService(MasterService masterService) {
		this.masterService = masterService;
	}

}
