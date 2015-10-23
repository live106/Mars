/**
 * 
 */
package com.live106.mars.master.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.live106.mars.master.MasterProtocolHandler;
import com.live106.mars.protocol.pojo.ProtocolTranser;
import com.live106.mars.protocol.thrift.PeerType;
import com.live106.mars.protocol.thrift.RequestServerLogin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Service
public class MasterService {
	
	private final static Logger logger = LoggerFactory.getLogger(MasterService.class);
	
	private static Map<Byte, Set<ServerData>> servers = new ConcurrentHashMap<>();//FIXME does set need to be concurrent ?

	public ServerData getServer(byte serverType, int serverId) {
		Set<ServerData> ss = servers.get(serverType);
		if (ss == null) {
			return null;
		}
		synchronized (ss) {
			if (serverId > 0) {
				Iterator<ServerData> iterator = ss.iterator();
				while (iterator.hasNext()) {
					ServerData s = iterator.next();
					if (s.id == serverId) {
						return s;
					}
				}
			}
			return ss.iterator().next();
		}
	}
	
	//TODO test concurrent, consider more than one same type and id servers connect to master at the same time
	public boolean registerAndOverwrite(ChannelHandlerContext context, RequestServerLogin request) {
		int id = request.getId();
		byte type = (byte) request.getType().getValue();
		
		ServerData server = new ServerData(id, context);
		if (servers.containsKey(type)) {
			Set<ServerData> set = servers.get(type);
			//TODO
			synchronized (set) {
				set.remove(server);
				set.add(server);
			}
		} else {
			Set<ServerData> set = Collections.synchronizedSet(new HashSet<>());
			synchronized (set) {
				set.add(server);
			}
			servers.put(type, set);
		}
		return true;
	}
	
	class ServerData {
		int id;
		ChannelHandlerContext context;
		
		public ServerData(int id, ChannelHandlerContext context) {
			this.id = id;
			this.context = context;
		}
		
		@Override
		public int hashCode() {
			return id;
		}
		
		@Override
		public boolean equals(Object obj) {
			return id == ((ServerData) obj).id;
		}
	}

	public void trasferProtocol(ProtocolTranser pojo) {
		PeerType targetType = pojo.getHeader().getTargetType();
		int serverId = pojo.getHeader().getTargetId();
		if (targetType == PeerType.PEER_TYPE_CLIENT) {
			Channel channel = MasterProtocolHandler.channelGroup.find((int) pojo.getChannelId());
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(pojo);
			} else {
				//FIXME 
			}
		} else {
			ServerData server = getServer((byte) targetType.getValue(), serverId);
			if (server == null) {
//				ReferenceCountUtil.release(pojo.getByteBuf());
				logger.debug(String.format("server[%d,%d] not found.", targetType.getValue(), serverId));
				//FIXME
			} else if (!server.context.channel().isActive()) {
//				ReferenceCountUtil.release(pojo.getByteBuf());
				logger.debug(String.format("server[%d,%d] context channel is not active.", targetType.getValue(), serverId));
				//FIXME unregiserServer
			} else {
				server.context.writeAndFlush(pojo);
			}
		}
	}

}
