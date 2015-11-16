package com.live106.mars.protocol.handler;

/**
 * @author live106 @creation Oct 9, 2015
 *
 */
public interface ProtocolProcessor {
	public void setListener(ProcessorListener listener);

	public ProcessorListener getListener();
}
