package com.nbb.netty.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 从通道中读取数据的handler
 */
@Slf4j
public class NettyClientChannelReadHandler extends ChannelInboundHandlerAdapter {

    // 通道就绪时回调该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("什么碗？", StandardCharsets.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf) msg;
        log.info("====接收到服务端【{}】发送的消息【{}】，当前通道id为【{}】====", channel.remoteAddress(), buf.toString(StandardCharsets.UTF_8), channel.id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("====出错了====");
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
