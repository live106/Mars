/**
 * 
 */
package com.live106.mars.account;

import java.security.Security;
import java.util.Date;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;

import com.live106.mars.account.config.AccountAppConfig;

/**
 * @author live106 @creation Oct 8, 2015
 *
 */
@Controller
public class AccountRunner {
	
	private final static Logger logger = LoggerFactory.getLogger(AccountRunner.class);

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws InterruptedException, TException {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		
		SpringApplication.run(AccountAppConfig.class, args);
		
		logger.info("Account server started at {}.", new Date().toString());
	}

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
