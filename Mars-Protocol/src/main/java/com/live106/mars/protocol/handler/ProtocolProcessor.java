package com.live106.mars.protocol.handler;

/**
 * NettyЭ�鴦����ͳһ�ӿ�
 * @author live106 @creation Oct 9, 2015
 */
public interface ProtocolProcessor {
	public void setListener(ProcessorListener listener);

	public ProcessorListener getListener();
}
