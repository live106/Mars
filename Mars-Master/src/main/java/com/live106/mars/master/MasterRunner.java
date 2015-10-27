package com.live106.mars.master;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;

import com.live106.mars.master.config.MasterAppConfig;
import com.live106.mars.util.LoggerHelper;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Controller
public class MasterRunner {
	
	private final static Logger logger = LoggerFactory.getLogger(MasterRunner.class);
	
//	@Autowired
//	private ConfigurableApplicationContext springContext;
//	@Autowired
//	private ProtocolMessageDispacher protocolHandlerFactory;
	
	public static void main(String[] args) throws InterruptedException {
		
		SpringApplication.run(MasterAppConfig.class, args);
//		
		LoggerHelper.info(logger, () -> String.format("Master server started at %1$s", new Date().toString()));
		
//		testMultiThreadRpcCall();
	}

//	private static void testMultiThreadRpcCall() {
//		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//		for (int i = 0; i < 20; i++) {
//			new Thread(new Runnable() {
//				public void run() {
//					IUserService.Iface bean = (Iface) ctx.getBean("userRpcClient");
//					String result;
//					try {
//						result = bean.ping("master");
//						System.err.println(Thread.currentThread().getName() + " : " + result);
//					} catch (TException e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
//		}
//	}
	
}
