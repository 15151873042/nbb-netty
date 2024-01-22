package com.nbb.netty.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * workerGroup 的 EventLoop 处理请求的handler
 * 自定义一个handler，需要继承netty规定好的某个HandlerAdapter
 */
@Slf4j
public class NettyServerWorkerGroupHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取消息
     * @param ctx 上下文对象，含有 管道pipeline、 通道channel、 地址
     * @param msg 客户端发送的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf) msg; // 将msg转成ByteBuf
        log.info("====接收到客户端【{}】发送的消息【{}】，当前通道id为【{}】====", channel.remoteAddress(), buf.toString(StandardCharsets.UTF_8), channel.id());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush
        // 将数据写入到缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("狗碗！", StandardCharsets.UTF_8));
    }

    // 异常处理，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("====出错了====");
        log.error(cause.getMessage(), cause);
        ctx.close();
    }
}
