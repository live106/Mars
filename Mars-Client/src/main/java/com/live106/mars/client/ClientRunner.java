package com.live106.mars.client;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.live106.mars.protocol.coder.ProtocolDecoder;
import com.live106.mars.protocol.coder.ProtocolEncoder;
import com.live106.mars.protocol.config.GlobalConfig;
import com.live106.mars.protocol.handler.annotation.Processor;
import com.live106.mars.protocol.pojo.IProtocol;
import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.queue.ProtocolMessage;
import com.live106.mars.protocol.thrift.PeerType;
import com.live106.mars.protocol.thrift.RequestSendClientPublicKey;
import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.protocol.util.Cryptor;
import com.live106.mars.protocol.util.ProtocolSerializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

@Controller
@Scope(value="prototype")
public class ClientRunner {
	private ChannelFuture channelFulture;
	@Autowired
	private ConfigurableApplicationContext springContext;
	@Autowired
	private ProtocolMessageClientDispacher protocolMessageClientDispacher;
	@Autowired
	private ClientProtocolHandler clientProtocolHandler;
	
	private Thread connectThread;
	
	private Cryptor cryptor = new Cryptor(Cryptor.AES);
	
	private LinkedBlockingQueue<ProtocolMessage> messages = new LinkedBlockingQueue<>(10); 
	private byte[] lock = new byte[0];
	
	public void start() throws NoSuchAlgorithmException, TException, InterruptedException, InvalidAlgorithmParameterException {
		if (channelFulture == null) {
			synchronized (lock) {
				lock.wait();
			}
		}
		
		Channel ch = channelFulture.channel();
		
		//check whether ch is valid
		
		ProtocolPeer2Peer tp = new ProtocolPeer2Peer();
		tp.getHeader().setTargetType(PeerType.PEER_TYPE_ACCOUNT);
		tp.getHeader().setSourceType(PeerType.PEER_TYPE_CLIENT);
		tp.getHeader().setSerializeType(SerializeType.SERIALIZE_TYPE_THRIFT);//thrift is default.
		
		RequestSendClientPublicKey request = new RequestSendClientPublicKey();
		
		cryptor.generateKey();
		
		String secretKey = cryptor.getSecretKey();
		
		ClientGroupRunner.secretKeys.put(ch.id().asLongText(), secretKey);
		
		request.setClientPubKey(secretKey);
		ProtocolSerializer.serialize(request, tp);
		
		ChannelFuture writeFulture = ch.writeAndFlush(tp);
		
		if (writeFulture != null) {
			writeFulture.sync();
		}
	}
	
	@PostConstruct
	public void init() {
		Map<String, Object> processors = springContext.getBeansWithAnnotation(Processor.class);
		protocolMessageClientDispacher.scanProcessor(processors);
		
		clientProtocolHandler.setClientRunner(this);
		
		Thread receiveThread = new Thread(receive);
		receiveThread.start();
		connectThread = new Thread(connect);
		connectThread.start();
	}

	Runnable connect = new Runnable () {
		public void run() {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(
							new LengthFieldPrepender(4, true),
//							new LoggingHandler(LogLevel.INFO), 
							new LengthFieldBasedFrameDecoder(IProtocol.MAX_LENGTH, 0, 4, -4, 4),
							new ProtocolDecoder(), 
							new ProtocolEncoder(), 
							clientProtocolHandler);
				}
			});
			
			try {
				channelFulture = b.connect(GlobalConfig.connectorHost, GlobalConfig.connectorPort).sync();
				synchronized (lock) {
					lock.notifyAll();
				}
				Channel ch = channelFulture.channel();
				
				ch.closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			workerGroup.shutdownGracefully();
		}
		}
	};
	
	public void addMessage(ProtocolMessage message) {
		try {
			messages.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	Runnable receive = new Runnable() {
		public void run() {
			while (true) {
				if (messages.size() == 0) {
					synchronized(lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				while (messages.size() > 0) {
					ProtocolMessage message = messages.poll();
					if (message != null) {
						protocolMessageClientDispacher.handleMessage(message);
					}
				}
			}
		}
	};

	public void setClientProtocolHandler(ClientProtocolHandler clientProtocolHandler) {
		this.clientProtocolHandler = clientProtocolHandler;
	}

	public ChannelFuture getChannelFulture() {
		return channelFulture;
	}
	
}
