/****************************************************************
 *文件名: 类名.java <br>
 *版本: <br>
 *描述: 相关描述<br>
 *版权所有: <br>
 *创建者: 刘源 <br>
 *创建日期: 2014年7月4日 <br>
 *修改者: 刘源<br>
 *修改日期: 2014年7月4日<br>
 *修改说明: 修改说明<br>
 ****************************************************************/

package org.server.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import org.server.netty.handler.EchoServerHandler;

/**
 *装载Netty处理链路.
 * @author 刘源
 */

public class InitializerPipeline extends ChannelInitializer<SocketChannel> {
	public InitializerPipeline() {
   
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		//使用Netty实现的线程池
//        DefaultEventExecutorGroup e1=new DefaultEventExecutorGroup(16);
		ChannelPipeline pipeline = ch.pipeline();
//		pipeline.addLast("decoder", new MessageDecoder());
//      pipeline.addLast("encoder", new MessageEncoder());
//		pipeline.addLast(e1,"handler", new CommonHandler());
		ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
		ch.pipeline().addLast(new ObjectEncoder());
		pipeline.addLast("handler", new EchoServerHandler());
	}
}
