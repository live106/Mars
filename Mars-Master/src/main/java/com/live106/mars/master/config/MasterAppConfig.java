/**
 * 
 */
package com.live106.mars.master.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author live106 @creation Oct 8, 2015
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.live106.mars.master", "com.live106.mars.master.service", "com.live106.mars.master.processor", "com.live106.mars.protocol.handler" })
// @PropertySources(value =
// {@PropertySource("classpath:/datasource.properties")})
public class MasterAppConfig {
	
}
