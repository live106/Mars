/**
 * 
 */
package com.live106.mars.master.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Master服务器Spring启动配置类
 * @author live106 @creation Oct 8, 2015
 */
@Configuration
@ComponentScan(basePackages = { "com.live106.mars.master", "com.live106.mars.master.service", "com.live106.mars.master.processor", "com.live106.mars.protocol.handler" })
public class MasterAppConfig {
	
}
