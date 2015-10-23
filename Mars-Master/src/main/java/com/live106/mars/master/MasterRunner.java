package com.live106.mars.master;

import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;

import com.live106.mars.master.config.MasterAppConfig;

/**
 * @author live106 @creation Oct 10, 2015
 *
 */
@Controller
public class MasterRunner {
	
//	@Autowired
//	private ConfigurableApplicationContext springContext;
//	@Autowired
//	private ProtocolMessageDispacher protocolHandlerFactory;
	
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(MasterAppConfig.class, args);
        
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
