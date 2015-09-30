package demo.netty;

import java.util.UUID;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;

import demo.netty.pojo.ThriftPojo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import thrift.pojo.LoginCode;
import thrift.pojo.RequestUserLogin;
import thrift.pojo.ResponseUserLogin;

public class TimeServerHandler extends ChannelHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		final ByteBuf time = ctx.alloc().buffer(4);
//		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//		
//		ChannelFuture f = ctx.writeAndFlush(time);
//		f.addListener(new ChannelFutureListener() {
//			
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				assert f == future;
//				ctx.close();
//			}
//		});
		
//		ChannelFuture f = ctx.writeAndFlush(new UnixTime());
//		f.addListener(ChannelFutureListener.CLOSE);
		
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ThriftPojo tt = (ThriftPojo) msg;
		
		System.err.println(tt.getClassName());
		
		RequestUserLogin reqLogin = new RequestUserLogin();
		TDeserializer td = new TDeserializer();
		td.deserialize(reqLogin, tt.getData());
		
		System.err.println(reqLogin.getUsername() + " " + reqLogin.getPassword());
		
		ResponseUserLogin resLogin = new ResponseUserLogin();
		resLogin.setCode(LoginCode.OK);
		resLogin.setMsg(UUID.randomUUID().toString());
		
		TSerializer ts = new TSerializer();
		byte[] bytes = ts.serialize(resLogin);
		
		ThriftPojo tp = new ThriftPojo();
		tp.setClassName(resLogin.getClass().getName());
		tp.setData(bytes);
		
		ctx.writeAndFlush(tp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
