package com.live106.mars.protocol.coder;

import org.apache.thrift.TException;

import com.live106.mars.protocol.pojo.ProtocolBase;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

//public class TimeEncoder extends ChannelHandlerAdapter {
//	@Override
//	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//		UnixTime m = (UnixTime) msg;
//		ByteBuf encoded = ctx.alloc().buffer(4);
//		encoded.writeInt((int) m.value());
//		ctx.write(encoded, promise);
//	}
//}

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