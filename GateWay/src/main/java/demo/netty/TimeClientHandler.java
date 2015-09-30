package demo.netty;

import org.apache.thrift.TDeserializer;

import demo.netty.pojo.ThriftPojo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import thrift.pojo.ResponseUserLogin;

public class TimeClientHandler extends ChannelHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		ByteBuf m = (ByteBuf) msg;
//		
//		try {
//			long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//		} finally {
//			m.release();
//		}
		
//		UnixTime m = (UnixTime) msg;
//		System.out.println(m);
//		ctx.close();
		//whether need call release ?
		
		ThriftPojo tt = (ThriftPojo) msg;
		
		System.err.println(tt.getClassName());
		
		ResponseUserLogin resLogin = new ResponseUserLogin();
		TDeserializer td = new TDeserializer();
		td.deserialize(resLogin, tt.getData());
		
		System.err.println(resLogin.getCode().name() + " " + resLogin.getMsg());
		
	}
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		super.write(ctx, msg, promise);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
