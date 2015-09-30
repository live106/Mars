package demo.netty;

import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

import demo.netty.pojo.ThriftPojo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import thrift.pojo.RequestUserLogin;

public class TimeClient {
	public static void main(String[] args) throws InterruptedException, TException {
		String host;
		int port;
		if (args.length > 1) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            host = "localhost";
            port = 8080;
        }
		
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new TimeClientDecoder(), new TimeClientEncoder(), new TimeClientHandler());
				}
			});
			
			ChannelFuture f = b.connect(host, port).sync();
			Channel ch = f.channel();
			
			RequestUserLogin login = new RequestUserLogin();
			login.setUsername("admin");
			login.setPassword("111111");
			
			TSerializer ts = new TSerializer();
			byte[] bytes = ts.serialize(login);
			
			ThriftPojo tp = new ThriftPojo();
			tp.setClassName(login.getClass().getName());
			tp.setData(bytes);
			
			ChannelFuture writeFulture = ch.writeAndFlush(tp);
			
			if (writeFulture != null) {
				writeFulture.sync();
			}
			
			ch.closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
