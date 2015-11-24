/**
 * 
 */
package com.live106.mars.common.thrift;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.live106.mars.util.LoggerHelper;

/**
 *  <h1>thrift��RPC�������ӳ�</h1>
 *  <br>
 * 
 * @author live106 @creation Oct 20, 2015
 */
public class TServiceClientPoolFactory extends BasePooledObjectFactory<TServiceClient> {

	private final static Logger logger = LoggerFactory.getLogger(TServiceClientPoolFactory.class);

	private String host = "localhost";
	private int port = 8080;
	private TServiceClientFactory<TServiceClient> clientFactory;

	private AtomicInteger counter = new AtomicInteger(1);
	private String serviceName = "";

	/**
	 * ��ʼ�����ӳز���
	 * @param host Զ��RPC�����ַ
	 * @param port Զ��RPC����˿�
	 * @param clientFactory RPC�ͻ���client��Ӧ��factory
	 * @param serviceName Զ��RPC������
	 */
	public TServiceClientPoolFactory(String host, int port, TServiceClientFactory<TServiceClient> clientFactory,
			String serviceName) {
		this.clientFactory = clientFactory;
		this.host = host;
		this.port = port;
		this.serviceName = serviceName;
	}

	/**
	 * ��������ʱ����TSocket����
	 */
	@Override
	public TServiceClient create() throws Exception {
		TServiceClient client;
		try {
			TTransport transport = new TSocket(host, port);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mProtocol = new TMultiplexedProtocol(protocol, serviceName);

			client = clientFactory.getClient(mProtocol);

			LoggerHelper.debug(logger, ()->String.format("open new transport-%d on /%s:%s success.", counter.getAndIncrement(), host, port));
			
			return client;
		} catch (Exception e) {
			LoggerHelper.error(logger, ()->String.format("open new transport-%d on /%s:%s throw exception.", counter.getAndIncrement(), host, port), e);
			throw e;
		}
	}

	@Override
	public PooledObject<TServiceClient> wrap(TServiceClient obj) {
		return new DefaultPooledObject<TServiceClient>(obj);
	}

	/**
	 * У��RPC����Client�Ƿ�����������
	 */
	@Override
	public boolean validateObject(PooledObject<TServiceClient> p) {
		TServiceClient client = p.getObject();
		TTransport transport = client.getInputProtocol().getTransport();
		return transport.isOpen();
	}

	@Override
	public void destroyObject(PooledObject<TServiceClient> p) throws Exception {
		TServiceClient client = p.getObject();
		TTransport transport = client.getInputProtocol().getTransport();
		transport.close();
	}

}
