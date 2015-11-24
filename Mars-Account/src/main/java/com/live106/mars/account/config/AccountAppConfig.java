/**
 * 
 */
package com.live106.mars.account.config;

import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.live106.mars.account.rpc.UserServiceRpc;
import com.live106.mars.common.thrift.TServiceClientBeanProxyFactory;
import com.live106.mars.concurrent.MarsDefaultThreadFactory;
import com.live106.mars.protocol.config.GlobalConfig;
import com.live106.mars.protocol.thrift.IUserService;
import com.live106.mars.protocol.thrift.game.IGamePlayerService;

/**
 * 账号服务器Spring启动配置
 * @author live106 @creation Oct 8, 2015
 */
@Configuration
@MapperScan({ "com.live106.mars.account.db.mapper" })
@ComponentScan(basePackages = { "com.live106.mars.account", "com.live106.mars.account.service", "com.live106.mars.account.rpc", 
		"com.live106.mars.protocol.thrift.game",
		})
@PropertySource("classpath:/datasource.properties")
public class AccountAppConfig {
	@Autowired
	private Environment env;
	
	@Autowired
	private UserServiceRpc userServiceRpc;

	@PostConstruct
	public void init() {
		new Thread(thriftServer, "thrfit-server-account").start();
	}
	
	/**
	 * 提供Thrift RPC 服务
	 */
	Runnable thriftServer = new Runnable() {
		public void run() {
			try {
				TServerTransport transport = new TServerSocket(GlobalConfig.accountRpcPort);
				TMultiplexedProcessor processor = new TMultiplexedProcessor();
				//注册定义的服务
				processor.registerProcessor(IUserService.class.getSimpleName(), new IUserService.Processor<IUserService.Iface>(userServiceRpc));
				
				TServer server = new TThreadPoolServer(new Args(transport)
						.processor(processor)
						.maxWorkerThreads(10)
						.executorService(Executors.newCachedThreadPool(new MarsDefaultThreadFactory("thrift-pool-account")))
						);
					
				server.serve();
			} catch (TTransportException e) {
				e.printStackTrace();
			}
		}
	};

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getRequiredProperty("dataSource.driverClassName"));
		dataSource.setUrl(env.getRequiredProperty("dataSource.url"));
		dataSource.setUsername(env.getRequiredProperty("dataSource.username"));
		dataSource.setPassword(env.getRequiredProperty("dataSource.password"));
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource());
		return (SqlSessionFactory) sqlSessionFactory.getObject();
	}
	
	/**
	 * 定义RPC服务代理Bean
	 * @return
	 */
	@Bean
	public TServiceClientBeanProxyFactory gamePlayerService() {
		try {
			return new TServiceClientBeanProxyFactory(GlobalConfig.gameHost, GlobalConfig.gameRpcPort, IGamePlayerService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
