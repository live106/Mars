/**
 * 
 */
package com.live106.mars.game.config;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

import com.live106.mars.concurrent.MarsDefaultThreadFactory;
import com.live106.mars.game.rpc.GamePlayerServiceRpc;
import com.live106.mars.game.rpc.GameStoreServiceRpc;
import com.live106.mars.protocol.config.GlobalConfig;
import com.live106.mars.protocol.thrift.game.IGamePlayerService;
import com.live106.mars.protocol.thrift.game.IGameStoreService;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 游戏服务器Spring启动配置文件
 * @author live106 @creation Oct 8, 2015
 */
@Configuration
@MapperScan({ "com.live106.mars.game.db.mapper" })
@ComponentScan(basePackages = { "com.live106.mars.game", "com.live106.mars.cache.game"})
@PropertySource("classpath:/datasource.properties")
public class GameAppConfig {
	@Autowired
	private Environment env;
	@Autowired
	private GamePlayerServiceRpc gamePlayerServiceRpc;
	@Autowired
	private GameStoreServiceRpc gameStoreServiceRpc;

	@PostConstruct
	public void init() {
		new Thread(thriftServer, "thrift-server-game").start();
	}

	/**
	 * 游戏服RPC服务
	 */
	Runnable thriftServer = new Runnable() {
		public void run() {
			try {
				TServerTransport transport = new TServerSocket(GlobalConfig.gameRpcPort);
				TMultiplexedProcessor processor = new TMultiplexedProcessor();
				
				//注册服务
				processor.registerProcessor(IGamePlayerService.class.getSimpleName(), new IGamePlayerService.Processor<IGamePlayerService.Iface>(gamePlayerServiceRpc));
				processor.registerProcessor(IGameStoreService.class.getSimpleName(), new IGameStoreService.Processor<IGameStoreService.Iface>(gameStoreServiceRpc));

				TServer server = new TThreadPoolServer(new Args(transport)
						.processor(processor)
						.maxWorkerThreads(10)
						.executorService(Executors.newCachedThreadPool(new MarsDefaultThreadFactory("thrift-pool-game")))
						.requestTimeout(3)
						.requestTimeoutUnit(TimeUnit.SECONDS)
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
	 * 配置Redis连接，当前使用Sentinel模式
	 * @return
	 */
	@Bean
	public JedisSentinelPool sentinelPool() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add(GlobalConfig.redisSentinelHost + ":" + GlobalConfig.redisSentinelPort);
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels, poolConfig, 5000);

		return sentinelPool;
	}

}
