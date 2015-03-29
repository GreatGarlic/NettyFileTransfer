package org.server.netty.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty业务逻辑处理器(Disruptor的生产者).
 * 
 * @author 刘源
 */

public class CommonHandler extends ChannelHandlerAdapter {
	/**
	 * 日志组件.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonHandler.class);

	public CommonHandler() {

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug(String.format("[%s]========打开连接=======", ctx.channel().id().asLongText()));
		ctx.channel().attr(AttributeKey.valueOf("haha")).set("1");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.debug(String.format("[%s]========关闭连接=======", ctx.channel().id().asLongText()));
		LOGGER.debug(ctx.channel().remoteAddress().toString());
		LOGGER.debug(ctx.channel().attr(AttributeKey.valueOf("haha")).get().toString());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		PublishMessageEntity entity = new PublishMessageEntity(ctx, msg);
        byte[] aaa= {0x12,0x23};
        byte[] bbb={0x23,0x34};
        byte[] ccc={0x55,0x55};
        ctx.write(msg);
        ctx.write(aaa);
        ctx.write(bbb);
        ctx.writeAndFlush(ccc);
        
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("[" + ctx.channel().id().asLongText() + "]" + "通讯异常:", cause);
		ctx.close();
	}
}
