package com.hetty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class RpcEncoder extends OneToOneEncoder {

	public RpcEncoder() {

	}

	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if(msg instanceof ChannelBuffer){
			return msg;
		}
		byte[]	data = ProtocolUtils.encode(msg);
		int dataLen = data.length;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(dataLen);
		buffer.writeBytes(data);
		return buffer;
	}
}
