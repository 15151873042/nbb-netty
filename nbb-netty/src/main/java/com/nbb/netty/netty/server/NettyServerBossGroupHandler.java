package com.nbb.netty.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * bossGroup 的 EventLoop 处理请求的handler
 * 自定义一个handler，需要继承netty规定好的某个HandlerAdapter
 */
@Slf4j
public class NettyServerBossGroupHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //可以使用一个list维护SocketChannel， 在推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
        log.info("====有新的客户端【{}】连接进来了，当前通道id为【{}】====", ch.remoteAddress(), ch.id());
        ch.pipeline().addLast(new NettyServerWorkerGroupHandler()); // 给workerGroup 的 EventLoop 对应的管道设置处理器
    }
}
