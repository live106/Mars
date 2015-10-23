/**
 * 
 */
package com.live106.mars.account;

import java.security.Security;

import org.apache.thrift.TException;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;

import com.live106.mars.account.config.AccountAppConfig;

/**
 * @author live106 @creation Oct 8, 2015
 *
 */
@Controller
public class AccountRunner {
//	@Autowired
//	private ConfigurableApplicationContext springContext;
//	private static ChannelFuture masterChannelFulture;
	
//	@Autowired
//	private ProtocolMessageDispacher protocolHandlerFactory;

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws InterruptedException, TException {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		
		SpringApplication.run(AccountAppConfig.class, args);
		
		
//        testMybatisSpring();
        
//		connectMaster();

	}
	
//	@PostConstruct
//	public void init() {
//		Map<String, Object> processors = springContext.getBeansWithAnnotation(Processor.class);
//		protocolHandlerFactory.scanProcessor(processors);
//	}

//	private static void connectMaster() throws InterruptedException, TException {
//		String masterHost = "localhost";
//		int masterPort = 8080;
//		
//		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
//		
//		try {
//			Bootstrap b = new Bootstrap();
//			b.group(workerGroup)
//			.channel(NioSocketChannel.class)
//			.handler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				protected void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast(
//							new LengthFieldPrepender(4, true),
//							new LoggingHandler(LogLevel.INFO), 
//							new LengthFieldBasedFrameDecoder(IProtocol.MAX_LENGTH, 0, 4, -4, 4),
//							new ProtocolDecoder(), 
//							new ProtocolEncoder(), 
//							new ProtocolHandler());
//				}
//			});
//			
//			masterChannelFulture = b.connect(masterHost, masterPort).sync();
//			Channel ch = masterChannelFulture.channel();
//			
//			registerToMaster();
//			
//			ch.closeFuture().sync();
//		} finally {
//			workerGroup.shutdownGracefully();
//		}
//	}

//	private static void registerToMaster() throws TException {
//		RequestServerLogin login = new RequestServerLogin();
//		login.setType(PeerType.PEER_TYPE_ACCOUNT);
//		login.setId(0x1010);
//		
//		TSerializer ts = new TSerializer();
//		byte[] bytes = ts.serialize(login);
//		
//		ProtocolPeer2Peer pojo = new ProtocolPeer2Peer();
//		pojo.getHeader().setTargetType(PeerType.PEER_TYPE_MASTER);
//		pojo.getHeader().setSourceType(PeerType.PEER_TYPE_ACCOUNT);
//		pojo.getHeader().setProtocolHash(login.getClass().getSimpleName().hashCode());
//		
////		pojo.setTargetType(IProtocol.PEER_TYPE_MASTER);
////		pojo.setSourceType(IProtocol.PEER_TYPE_ACCOUNT);
////		pojo.setTargetId(0x0001);
////		pojo.setSourceId(sourceId);
////		pojo.setSerializeType(IProtocol.SERIALIZE_TYPE_THRIFT);
////		pojo.setProtocolHash(login.getClass().getSimpleName().hashCode());
//		pojo.setData(bytes);
//		
//		Channel ch = masterChannelFulture.channel();
//		ch.writeAndFlush(pojo);
//	}

//	private static void testMybatisSpring() {
//		//test bean injection
//        String[] beanNames = springContext.getBeanDefinitionNames();
//        Arrays.sort(beanNames);
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
//        
//        //test database access
//        UserService service = springContext.getBean(UserService.class);
//        boolean result = service.exist("admin");
//        System.err.println(result);
//        
//        String password = service.getPassword("admin");
//        System.err.println(password);
//        
//        User user = service.getUser("admin");
//        System.err.println(user.getPassword());
//        
//        //test schedule
//        //test AOP
//        //test transaction
//	}

}
