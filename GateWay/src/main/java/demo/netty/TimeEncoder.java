package demo.netty;

import demo.netty.pojo.ThriftPojo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

//public class TimeEncoder extends ChannelHandlerAdapter {
//	@Override
//	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//		UnixTime m = (UnixTime) msg;
//		ByteBuf encoded = ctx.alloc().buffer(4);
//		encoded.writeInt((int) m.value());
//		ctx.write(encoded, promise);
//	}
//}

public class TimeEncoder extends MessageToByteEncoder<ThriftPojo> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ThriftPojo msg, ByteBuf out) {
    	byte[] data = msg.getData();
    	out.writerIndex(5);// int length + byte length
    	int nameLen = ByteBufUtil.writeUtf8(out, msg.getClassName());//no more then Byte.MAX_VALUE
    	out.markWriterIndex();
    	out.writerIndex(0);
    	out.writeInt(data.length + nameLen);
    	out.writeByte(nameLen);
    	out.resetWriterIndex();
		out.writeBytes(data);
    }
}