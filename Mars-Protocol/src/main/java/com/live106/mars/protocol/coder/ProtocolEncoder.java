package com.live106.mars.protocol.coder;

import org.apache.thrift.TException;

import com.live106.mars.protocol.pojo.ProtocolBase;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Netty–≠“È±‡¬Î
 * @author live106 @creation Nov 24, 2015
 *
 */
public class ProtocolEncoder extends MessageToByteEncoder<ProtocolBase> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolBase msg, ByteBuf out) {
    	try {
			msg.encode(out);
		} catch (TException e) {
			out.release();
			e.printStackTrace();
		}
    }
}