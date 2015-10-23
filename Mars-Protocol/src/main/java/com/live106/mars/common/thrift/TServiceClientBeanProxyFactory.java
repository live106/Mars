/**
 * 
 */
package com.live106.mars.common.thrift;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author live106 @creation Oct 20, 2015
 *
 */
public class TServiceClientBeanProxyFactory implements FactoryBean<Object>, InitializingBean {

	private String host = "localhost";
	private int port = 8080;
	private Object clientProxy;
	private Class<?> ifaceClass;
	private Class<?> clazz;
	
	public TServiceClientBeanProxyFactory(String host, int port, Class<?> clazz) {
		this.host = host;
		this.port = port;
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ifaceClass = classLoader.loadClass(clazz.getName() + "$Iface");
		Class<TServiceClientFactory<TServiceClient>> factoryClass = (Class<TServiceClientFactory<TServiceClient>>) classLoader.loadClass(clazz.getName() + "$Client$Factory");
		TServiceClientFactory<TServiceClient> clientFactory = factoryClass.newInstance();
		TServiceClientPoolFactory poolFactory = new TServiceClientPoolFactory(host, port, clientFactory, clazz.getSimpleName());
		
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		//FIXME set the object pool configuration
//		poolConfig.setMaxIdle(maxIdle);
		GenericObjectPool<TServiceClient> pool = new GenericObjectPool<TServiceClient>(poolFactory, poolConfig);
		
		clientProxy = Proxy.newProxyInstance(classLoader, new Class[] {ifaceClass}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				TServiceClient client = pool.borrowObject();
				try {
					return method.invoke(client, args);
				} catch (Exception e) {
					throw e;
				} finally {
					pool.returnObject(client);
				}
			}
		});
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
