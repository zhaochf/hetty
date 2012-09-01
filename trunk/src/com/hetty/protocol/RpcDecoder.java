package com.hetty.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class RpcDecoder extends FrameDecoder {

	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() < 4) {
			return null;
		}
		/*
		 * int nameLength = buffer.getInt(buffer.readerIndex()); if
		 * (buffer.readableBytes() < nameLength + 4) { return null; }
		 */

		/*
		 * buffer.skipBytes(4); byte[] nameBytes = new byte[nameLength];
		 * buffer.readBytes(nameBytes, 0, nameLength); String clsName = new
		 * String(nameBytes); Class<?> cls = Class.forName(clsName);
		 */

		// buffer.skipBytes(nameLength);

		int dataLength = buffer.getInt(buffer.readerIndex());
		if (buffer.readableBytes() < dataLength + 4) {
			return null;
		}
		buffer.skipBytes(4);

		byte[] bytes = new byte[dataLength];
		buffer.readBytes(bytes);

		Object resultObject = ProtocolUtils.decode(bytes);

		return resultObject;

	}

}
