package com.live106.mars.client;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.live106.mars.protocol.coder.ProtocolDecoder;
import com.live106.mars.protocol.coder.ProtocolEncoder;
import com.live106.mars.protocol.config.GlobalConfig;
import com.live106.mars.protocol.handler.ProcessorListener;
import com.live106.mars.protocol.handler.ProtocolProcessor;
import com.live106.mars.protocol.handler.annotation.Processor;
import com.live106.mars.protocol.pojo.IProtocol;
import com.live106.mars.protocol.pojo.ProtocolPeer2Peer;
import com.live106.mars.protocol.queue.ProtocolMessage;
import com.live106.mars.protocol.thrift.PeerType;
import com.live106.mars.protocol.thrift.RequestLoadArchive;
import com.live106.mars.protocol.thrift.RequestSaveArchive;
import com.live106.mars.protocol.thrift.RequestSendClientPublicKey;
import com.live106.mars.protocol.thrift.SerializeType;
import com.live106.mars.protocol.thrift.TArchiveInfo;
import com.live106.mars.protocol.thrift.TProcessInfo;
import com.live106.mars.protocol.thrift.TUserInfo;
import com.live106.mars.protocol.util.Cryptor;
import com.live106.mars.protocol.util.ProtocolSerializer;
import com.live106.mars.util.LoggerHelper;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

@Controller
@Scope(value="prototype")
public class ClientRunner implements ProcessorListener {
	
	private final static Logger logger = LoggerFactory.getLogger(ClientRunner.class);
	
	public static List<ClientRunner> runners = Collections.synchronizedList(new ArrayList<>());
	
	@Autowired
	private ConfigurableApplicationContext springContext;
	@Autowired
	private ProtocolMessageClientDispacher protocolMessageClientDispacher;
	@Autowired
	private ClientProtocolHandler clientProtocolHandler;
	
	private final AtomicInteger connectNumber = new AtomicInteger();
	volatile boolean doReconnect = false;
	volatile boolean doCloseSocket = false;
	private ConnectThread connectThread;
	
	private Cryptor cryptor = new Cryptor(Cryptor.AES);
	private Random random = new Random();
	
	private LinkedBlockingQueue<ProtocolMessage> messages = new LinkedBlockingQueue<>(10); 
	public byte[] receiverLock = new byte[0];
	public byte[] connectorLock = new byte[0];
	
	private String secretKey;
	//
	private String machineId;
	private int uid;
	private String passport;
	
	//
	private TArchiveInfo archive;
	private int mission = 1;
	
	public ChannelFuture connectChannelFulture() {
		if (connectThread == null) {
			return null;
		}
		if (connectThread.connectChannelFulture == null) {
			return null;
		}
		if (doReconnect) {
			try {
				this.reconnect();
				synchronized(connectorLock) {
					connectorLock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return connectThread.connectChannelFulture;
	}
	
	public void start() throws NoSuchAlgorithmException, TException, InterruptedException, InvalidAlgorithmParameterException {
		if (connectChannelFulture() == null) {
			synchronized (connectorLock) {
				connectorLock.wait();
			}
		}
		
		synchronized (runners) {
			runners.add(this);
		}
		
		Channel ch = connectChannelFulture().channel();
		
		ProtocolPeer2Peer tp = new ProtocolPeer2Peer();
		tp.getHeader().setTargetType(PeerType.PEER_TYPE_ACCOUNT);
		tp.getHeader().setSourceType(PeerType.PEER_TYPE_CLIENT);
		tp.getHeader().setSerializeType(SerializeType.SERIALIZE_TYPE_THRIFT);//thrift is default.
		
		RequestSendClientPublicKey request = new RequestSendClientPublicKey();
		
		cryptor.generateKey();
		
		secretKey = cryptor.getSecretKey();
		
		request.setClientPubKey(secretKey);
		ProtocolSerializer.serialize(request, tp);
		
		ch.writeAndFlush(tp);
				
		LoggerHelper.debug(logger, ()->String.format("%s start.", Thread.currentThread().getName()));
	}
	
	public void saveArchive() throws TException {
		ProtocolPeer2Peer tp = new ProtocolPeer2Peer();
		tp.getHeader().setTargetType(PeerType.PEER_TYPE_GAME);
		tp.getHeader().setSourceType(PeerType.PEER_TYPE_CLIENT);
		tp.getHeader().setSerializeType(SerializeType.SERIALIZE_TYPE_THRIFT);
		
		tp.getHeader().setSourceId(uid);
		tp.getHeader().setPassport(passport);
		
		RequestSaveArchive request = new RequestSaveArchive();
		if (archive == null) {
			archive = new TArchiveInfo();
			TUserInfo userinfo = new TUserInfo();
			Map<Integer, TProcessInfo> processes = new HashMap<>();
			
			userinfo.setId(String.valueOf(uid));
			
			archive.setUser(userinfo);
			archive.setProcesses(processes);
		}
		
		archive.getUser().setGold(archive.getUser().getGold() + 100);
		archive.getUser().setTimestamp(System.currentTimeMillis());
		
		mission++;
		TProcessInfo processInfo = new TProcessInfo();
		processInfo.setMission(mission);
		processInfo.setUnlock(true);
		processInfo.setStar(random.nextInt(3) + 1);
		archive.getProcesses().put(mission, processInfo);
		
		request.setArchive(archive);
		
		ProtocolSerializer.serialize(request, tp);
		
		Channel ch = connectChannelFulture().channel();
		ch.writeAndFlush(tp);
		
		LoggerHelper.debug(logger, ()->String.format("%s save archive.\n%s", this.toString(), archive.toString()));
	}
	
	public void loadArchive() throws TException {
		ProtocolPeer2Peer tp = new ProtocolPeer2Peer();
		tp.getHeader().setTargetType(PeerType.PEER_TYPE_GAME);
		tp.getHeader().setSourceType(PeerType.PEER_TYPE_CLIENT);
		tp.getHeader().setSerializeType(SerializeType.SERIALIZE_TYPE_THRIFT);
		
		tp.getHeader().setSourceId(uid);
		tp.getHeader().setPassport(passport);
		
		RequestLoadArchive request = new RequestLoadArchive();
		ProtocolSerializer.serialize(request, tp);
		
		Channel ch = connectChannelFulture().channel();
		ch.writeAndFlush(tp);
		
		LoggerHelper.debug(logger, ()->String.format("%s load archive.", this.toString()));
	}
	
	@PostConstruct
	public void init() {
		Map<String, Object> processors = springContext.getBeansWithAnnotation(Processor.class);
		for (Object obj : processors.values()) {
			if (obj instanceof ProtocolProcessor) {
				ProtocolProcessor processor = (ProtocolProcessor) obj; 
				processor.setListener(this);
			}
		}
		protocolMessageClientDispacher.scanProcessor(processors);
		
		clientProtocolHandler.setClientRunner(this);
		
		try {
			reconnect();
		} catch (InterruptedException e) {
			logger.error("runner init failed!", e);
		}
	}
	
	public void reconnect() throws InterruptedException {
		connectThread = new ConnectThread(threadNumber + "-" + connectNumber.getAndIncrement() + "-netty-client");
		connectThread.start();
	}
	
	private Bootstrap configureBootstrap(Bootstrap b, EventLoopGroup g) {
		b.group(g).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LengthFieldPrepender(4, true),
//								new LoggingHandler(LogLevel.INFO),
								new LengthFieldBasedFrameDecoder(IProtocol.MAX_LENGTH, 0, 4, -4, 4),
								new ProtocolDecoder(), new ProtocolEncoder(), clientProtocolHandler);
					}
				});
		
		return b;
	}
	
	
	class ConnectThread extends Thread {
		
		private Bootstrap bootstrap;
		private ChannelFuture connectChannelFulture;
		
		public ConnectThread(String name) {
			super(name);
		}
		
		@Override
		public void run() {
			try {
				bootstrap = configureBootstrap(new Bootstrap(), new NioEventLoopGroup());

				try {
					connectChannelFulture = bootstrap.connect(GlobalConfig.connectorHost, GlobalConfig.connectorPort)
							.sync();
					
					LoggerHelper.debug(logger, ()->String.format("客户端{%s}玩家{%d} 连接网关服务器", Thread.currentThread().getName(), uid));
					
					ReceiveThread receiver = receiverLocals.get();
					receiver.setRunner(ClientRunner.this);
					receiver.start();
					
					synchronized (connectorLock) {
						connectorLock.notifyAll();
					}
					
					Channel ch = connectChannelFulture.channel();
					ch.closeFuture().sync();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} finally {
				bootstrap.group().shutdownGracefully();
			}
			
			LoggerHelper.debug(logger, ()->String.format("客户端{%s}玩家{%d} 断开网关服务器", Thread.currentThread().getName(), uid));
		}
	}

	public void addMessage(ProtocolMessage message) {
		try {
			messages.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (receiverLock) {
			receiverLock.notifyAll();
		}
	}
	
	private static final ThreadLocal<ReceiveThread> receiverLocals = new ThreadLocal<ReceiveThread>() {

		protected ReceiveThread initialValue() {
			return new ReceiveThread();
		};
	};
	
	static class ReceiveThread extends Thread {
		
		private ClientRunner runner;
		
		private void setRunner(ClientRunner runner) {
			this.runner = runner;
			setName(runner.threadNumber + "-" + runner.connectNumber.get() + "-receiver");
		}
		
		public void run() {
			while (true) {
				if (runner.messages.size() == 0) {
					synchronized(runner.receiverLock) {
						try {
							runner.receiverLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				while (runner.messages.size() > 0) {
					ProtocolMessage message = runner.messages.poll();
					if (message != null) {
						runner.protocolMessageClientDispacher.handleMessage(message);
					}
				}
				if (runner.doReconnect || runner.doCloseSocket) {
					break;
				}
			}
			runner.doReconnect = false;
			runner.doCloseSocket = false;
		}
	};

	private int threadNumber;

	public void setClientProtocolHandler(ClientProtocolHandler clientProtocolHandler) {
		this.clientProtocolHandler = clientProtocolHandler;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}
	
	public int getUid() {
		return this.uid;
	}
	
	public String getSecretKey() {
		return this.secretKey;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}
	
	public String getPassport() {
		return this.passport;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	
	public String toString() {
		return String.format("%s[%d]", machineId, uid);
	}

	public void setThreadNumber(int number) {
		this.threadNumber = number;
	}

}
