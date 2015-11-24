/**
 * 
 */
package com.live106.mars.master.channel;

import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.PlatformDependent;

/**
 * 可用于存储和管理Channel连接，目前尚未使用
 * @author live106 @creation Oct 13, 2015
 */
public class MasterChannelGroup extends DefaultChannelGroup {
	
	private final ConcurrentMap<Integer, Channel> allChannels = PlatformDependent.newConcurrentHashMap();

	public MasterChannelGroup(String name, EventExecutor executor) {
		super(name, executor);
	}

	public MasterChannelGroup(EventExecutor executor) {
		super(executor);
	}
	
	@Override
	public boolean add(Channel channel) {
		boolean added = allChannels.putIfAbsent(channel.id().asLongText().hashCode(), channel) == null;
		return super.add(channel) && added;
	}
	
	@Override
	public boolean remove(Object o) {
		boolean removed = false;
		if (o instanceof Integer) {
			removed = allChannels.remove(o) == null;
		}
		return super.remove(o) && removed;
	}
	
	public Channel find(int hashcode) {
		return allChannels.get(hashcode);
	}
	
}
