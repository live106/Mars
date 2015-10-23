/**
 * 
 */
package com.live106.mars.master;

import java.lang.reflect.Method;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.live106.mars.common.thrift.TServiceClientBeanProxyFactory;
import com.live106.mars.protocol.coder.ProtocolEncoder;
import com.live106.mars.protocol.config.GlobalConfig;
import com.live106.mars.protocol.handler.MessageProcessor;
import com.live106.mars.protocol.handler.ProtocolMessageRPCDispacher;
import com.live106.mars.protocol.pojo.IProtocol;
import com.live106.mars.protocol.thrift.IUserService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Service
public class MasterApplication {
	
	@Autowired
	private IUserService.Iface userRpcClient;
	
	@Bean
	public TServiceClientBeanProxyFactory userRpcClient() {
		try {
			return new TServiceClientBeanProxyFactory(GlobalConfig.accountRpcHost, GlobalConfig.accountRpcPort, IUserService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Autowired
	private ProtocolMessageRPCDispacher protocolMessageRPCDispacher;
	
	@PostConstruct
	public void init() throws InterruptedException {
		protocolMessageRPCDispacher.start(2);
		
//		Thread threadAccRpcClient = new Thread(accountRpcClient);
//		threadAccRpcClient.start();
		new Thread(masterNettyServer).start();
//		new Thread(thriftServer).start();

//		threadAccRpcClient.join();
		
		Map<Integer, MessageProcessor<?>> processors = protocolMessageRPCDispacher.getMessageProcessors();
		
		Method[] methods = IUserService.Iface.class.getDeclaredMethods();
		for (Method method : methods) {
			String messageName = method.getParameterTypes()[0].getSimpleName();
			MessageProcessor<IUserService.Iface> messageProcessor = new MessageProcessor<IUserService.Iface>(userRpcClient, method, messageName);
			processors.put(messageName.hashCode(), messageProcessor);
				
		}
	}
	
//	Runnable accountRpcClient = new Runnable() {
//		public void run() {
//			try {
//				TTransport transport = new TSocket(GlobalConfig.accountRpcHost, GlobalConfig.accountRpcPort);
//				transport.open();
//				
//				TProtocol protocol = new TBinaryProtocol(transport);
//				TMultiplexedProtocol mProtocol = new TMultiplexedProtocol(protocol, IUserService.class.getSimpleName());
//
//				userRpcClient0 = new IUserService.Client(mProtocol);
//				
//			} catch (TException e) {
//				e.printStackTrace();
//			}
//		}
//	};

	Runnable thriftServer = new Runnable() {
		public void run() {
			try {
				TServerTransport transport = new TServerSocket(GlobalConfig.masterRpcPort);
				TMultiplexedProcessor processor = new TMultiplexedProcessor();
//				processor.registerProcessor(BaseService.class.getSimpleName(), new BaseService.AsyncProcessor<BaseService.AsyncIface>(new AsyncBaseServiceImpl()));
//				processor.registerProcessor(IMasterService.class.getSimpleName(), new IMasterService.Processor<IMasterService.Iface>(new IMasterServiceImpl()));
				
				TServer server = new TThreadPoolServer(new Args(transport).processor(processor));
				
				server.serve();
			} catch (TTransportException e) {
				e.printStackTrace();
			}
		}
	};

	Runnable masterNettyServer = new Runnable() {
		public void run() {
			
			ResourceLeakDetector.setLevel(Level.ADVANCED);
			
			NioEventLoopGroup bossGroup = new NioEventLoopGroup();
			NioEventLoopGroup workerGroup = new NioEventLoopGroup();

			try {
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
						.handler(new LoggingHandler(LogLevel.INFO))
						.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(
								new LengthFieldPrepender(4, true), 
								new LoggingHandler(LogLevel.INFO),
								new LengthFieldBasedFrameDecoder(IProtocol.MAX_LENGTH, 0, 4, -4, 4),
								new MasterProtocolDecoder(), new ProtocolEncoder(), new MasterProtocolHandler());
					}

				}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

				ChannelFuture f = b.bind(GlobalConfig.masterNettyPort).sync();

				f.channel().closeFuture().sync();

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		}
	};
}