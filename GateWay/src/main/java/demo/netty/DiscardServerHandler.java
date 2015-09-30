package demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		ctx.write(msg);
		ctx.flush();
		
//		ByteBuf in = (ByteBuf) msg;
	    try {
//	        while (in.isReadable()) { // (1)
//	            System.out.print((char) in.readByte());
//	            System.out.flush();
//	        }
	        
//	        System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
	    } finally {
//	        ReferenceCountUtil.release(msg); // (2)
	    }
		
//		((ByteBuf) msg).release();
		
//		try {
//	        // Do something with msg
//	    } finally {
//	        ReferenceCountUtil.release(msg);
//	    }
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
