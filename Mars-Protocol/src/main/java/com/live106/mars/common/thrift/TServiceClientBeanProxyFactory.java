/**
 * 
 */
package com.live106.mars.common.thrift;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.live106.mars.util.LoggerHelper;

/**
 * <h1>RPC调用client的代理bean工厂类</h1>
 * <ol>
 * <li>拦截client方法</li>
 * <li>从工厂{@link TServiceClientPoolFactory}获取连接池中的client实例执行方法调用</li>
 * </ol>
 * 
 * @author live106 @creation Oct 20, 2015
 */
public class TServiceClientBeanProxyFactory implements FactoryBean<Object>, InitializingBean {
	
	private final static Logger logger = LoggerFactory.getLogger(TServiceClientBeanProxyFactory.class);

	private String host = "localhost";
	private int port = 8080;
	private Object clientProxy;
	private Class<?> ifaceClass;
	private Class<?> rpcInterfaceClazz;

	
	/**
	 * 重连机制相关参数
	 */
	private Options option = Options.defaults();
	/**
	 * List of causes which suggest a restart might fix things (defined as
	 * constants in {@link org.apache.thrift.transport.TTransportException}).
	 */
	private static final Set<Integer> RESTARTABLE_CAUSES = new HashSet<Integer>(
			Arrays.asList(TTransportException.NOT_OPEN, TTransportException.END_OF_FILE, TTransportException.TIMED_OUT,
					TTransportException.UNKNOWN));

	public static class Options {
		private int numRetries;
		private long timeBetweenRetries;

		/**
		 *
		 * @param numRetries
		 *            the maximum number of times to try reconnecting before
		 *            giving up and throwing an exception
		 * @param timeBetweenRetries
		 *            the number of milliseconds to wait in between reconnection
		 *            attempts.
		 */
		public Options(int numRetries, long timeBetweenRetries) {
			this.numRetries = numRetries;
			this.timeBetweenRetries = timeBetweenRetries;
		}

		private int getNumRetries() {
			return numRetries;
		}

		private long getTimeBetweenRetries() {
			return timeBetweenRetries;
		}

		public Options withNumRetries(int numRetries) {
			this.numRetries = numRetries;
			return this;
		}

		public Options withTimeBetweenRetries(long timeBetweenRetries) {
			this.timeBetweenRetries = timeBetweenRetries;
			return this;
		}

		public static Options defaults() {
			return new Options(5, 5000L);
		}
	}

	/**
	 * 初始化RPC service bean代理工厂参数
	 * @param host RPC服务地址
	 * @param port RPC服务端口
	 * @param rpcInterfaceClazz RPC服务接口类
	 */
	public TServiceClientBeanProxyFactory(String host, int port, Class<?> rpcInterfaceClazz) {
		this.host = host;
		this.port = port;
		this.rpcInterfaceClazz = rpcInterfaceClazz;
	}

	/**
	 * 在设置完bean属性值后{@link BeanFactory}会调用该方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ifaceClass = classLoader.loadClass(rpcInterfaceClazz.getName() + "$Iface");
		Class<TServiceClientFactory<TServiceClient>> factoryClass = (Class<TServiceClientFactory<TServiceClient>>) classLoader
				.loadClass(rpcInterfaceClazz.getName() + "$Client$Factory");
		TServiceClientFactory<TServiceClient> clientFactory = factoryClass.newInstance();
		TServiceClientPoolFactory poolFactory = new TServiceClientPoolFactory(host, port, clientFactory,
				rpcInterfaceClazz.getSimpleName());

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		// TODO set the object pool configuration
		// poolConfig.setMaxIdle(maxIdle);
		GenericObjectPool<TServiceClient> pool = new GenericObjectPool<TServiceClient>(poolFactory, poolConfig);

		clientProxy = Proxy.newProxyInstance(classLoader, new Class[] { ifaceClass }, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				TServiceClient client = pool.borrowObject();
				try {
					return method.invoke(client, args);
				} catch (InvocationTargetException e) {
					if (e.getTargetException() instanceof TTransportException) {
						TTransportException te = (TTransportException) e.getTargetException();
						
						//如果认为是可进行重连尝试的异常则进行重连操作
						if (RESTARTABLE_CAUSES.contains(te.getType())) {
							reconnectOrThrowException(client.getInputProtocol().getTransport());
							return method.invoke(client, args);
						} else if (te.getCause() != null && te.getCause() instanceof ConnectException) {
							reconnectOrThrowException(client.getInputProtocol().getTransport());
							return method.invoke(client, args);
						}
						
						LoggerHelper.error(logger, ()->String.format("rpc call %s.%s failed!", client, method), te);
						
					} else if (e.getTargetException() instanceof ConnectException) {
						reconnectOrThrowException(client.getInputProtocol().getTransport());
						return method.invoke(client, args);
					}
					
					LoggerHelper.error(logger, ()->String.format("rpc call %s.%s failed!", client, method), e.getTargetException());
					
					throw e.getTargetException();
					
				} finally {
					pool.returnObject(client);
				}
			}

		});
	}
	
	/**
	 * 重新连接{@link TTransport}
	 * @param transport
	 * @throws TTransportException
	 */
	private void reconnectOrThrowException(TTransport transport) throws TTransportException {
		int errors = 0;
		transport.close();
		
		int numRetries = option.getNumRetries();
		while (errors < numRetries) {
			try {
				logger.info("Attempting to reconnect /{}:{}...", host, port);
				transport.open();
				logger.info("Reconnection successful /{}:{}", host, port);
				break;
			}
			catch (TTransportException e) {
				logger.error("Error while reconnecting /{}:{}:", host, port, e);
				errors++;
				
				if (errors < numRetries) {
					try {
						long timeBetweenRetries = option.getTimeBetweenRetries();
						LoggerHelper.debug(logger,
								() -> String.format("Sleeping for %s milliseconds before retrying /%s:%d",
										timeBetweenRetries, host, port));
						Thread.sleep(timeBetweenRetries);
					} catch (InterruptedException e2) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		
		if (errors >= numRetries) {
			throw new TTransportException("Failed to reconnect");
		}
	}

	@Override
	public Object getObject() throws Exception {
		return clientProxy;
	}

	@Override
	public Class<?> getObjectType() {
		return ifaceClass;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
