package demo.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class TimeClientDecoder2 extends ReplayingDecoder<Void> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		out.add(in.readBytes(4));
		
		//1.extension study 1
		
//		state();
//		checkpoint();
		
		//2.extension study 2
		
		/*
		// Decode the first message
        Object firstMessage = ...;

        // Add the second decoder
        ctx.pipeline().addLast("second", new SecondDecoder());

        if (buf.isReadable()) {
            // Hand off the remaining data to the second decoder
            out.add(firstMessage);
            out.add(buf.readBytes(super.actualReadableBytes()));
        } else {
            // Nothing to hand off
            out.add(firstMessage);
        }
        // Remove the first decoder (me)
        ctx.pipeline().remove(this);
        */
	}

}
