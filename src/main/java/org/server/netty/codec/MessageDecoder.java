package org.server.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 自定义解码器(具体功能：可以解决TCP粘包分包问题).
 * @author 刘源
 */
public class MessageDecoder extends ByteToMessageDecoder {
	/**
	 * 日志组件.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);

	public MessageDecoder() {

	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buff, List<Object> objs) throws Exception {
		// 防止不发报文就关闭连接出现的错误
		if (!buff.isReadable()) {
			return;
		}
		LOGGER.info(String.format("[%s]收到的的报文:[%s]", ctx.channel().id().asLongText(), ByteBufUtil.hexDump(buff)));

		byte[] ss = new byte[buff.readableBytes()];
		buff.readBytes(ss);
		objs.add(ss);
	}

}
