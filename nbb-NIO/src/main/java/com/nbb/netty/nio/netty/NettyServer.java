package com.nbb.netty.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    /**
     * 创建BossGroup 和 WorkerGroup
     * 1、创建两个线程组 boosGroup 和 workerGroup
     * 2、bossGroup只是处理连接请求，真正和客户吨啊业务处理，会交给workerGroup完成
     * 3、两个都是无线循环
     * 4、bossGroup 和 workerGroup 含有子线程(NioEventLoop)的个数
     */
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //可以使用一个list维护SocketChannel， 在推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
                            log.info("====有新的客户端【{}】连接进来了====", ch.remoteAddress());
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); // 给workerGroup 的 EventLoop 对应的管道设置处理

            log.info("====服务器启动了====");

            // 绑定一个端口并同步，生成一个 ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        log.info("====监听端口 6668 成功====");
                    } else {
                        log.info("====监听端口 6668 失败====");
                    }
                }
            });

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
