package demo.netty;

import java.util.List;

import demo.netty.pojo.ThriftPojo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

public class TimeDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}
		
		int len = in.readInt();
		
		if (in.readableBytes() < len) {
			in.resetReaderIndex();
			return;
		}
		
		ThriftPojo tt = new ThriftPojo();
		
		int nameLen = in.readByte();
		ByteBuf b = in.readBytes(nameLen);
		String name = new String(b.array(), CharsetUtil.UTF_8);
		
		tt.setClassName(name);
		
		byte[] bytes = new byte[len - nameLen];
		in.readBytes(bytes);
		
		tt.setData(bytes);
		
		out.add(tt);
	}

}
