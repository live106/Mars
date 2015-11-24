package com.live106.mars.protocol.handler;

/**
 * Netty协议处理类统一接口
 * @author live106 @creation Oct 9, 2015
 */
public interface ProtocolProcessor {
	public void setListener(ProcessorListener listener);

	public ProcessorListener getListener();
}
