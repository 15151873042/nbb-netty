package com.nbb.netty.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;


/**
 * 初始化管道的handler
 * workerGroup 的 EventLoop 处理请求的handler
 * 自定义一个handler，需要继承netty规定好的某个HandlerAdapter
 */
@Slf4j
public class NettyServerChannelInitHandler extends ChannelInitializer<SocketChannel> {

    /**
     * 通道初始化时回调
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //可以使用一个list维护SocketChannel， 在推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
        log.info("====有新的客户端【{}】连接进来了，当前通道id为【{}】====", ch.remoteAddress(), ch.id());

        // FIXME 由于ChannelInitializer#initChannel回调当前方法结束后，会将当前handler从通道中移除，所以还需要向管道中添加读取数据的handler
        ch.pipeline().addLast(new NettyServerChannelReadHandler());
    }

}
