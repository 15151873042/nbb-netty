package com.nbb.netty.springboot.framework.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Component
public class NettyServer {


    @Autowired
    private NettyProperties nettyProperties;

    /** netty-server启动端口绑定成功的标识 */
    private AtomicBoolean nettyPortBind = new AtomicBoolean(false);

    /** 当前springBoot项目启动主线程 */
    private Thread springBootStartThread;


    /**
     * 启动netty服务器
     */
    public void start() {
        this.springBootStartThread = Thread.currentThread();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(nettyProperties.getBoosThread());
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettyProperties.getWorkerThread());

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerChannelInitializer());

            // netty-server启动绑定对应端口
            ChannelFuture portBindFuture = serverBootstrap.bind(nettyProperties.getPort());
            // 添加netty-server端口绑定任务处理完成后的回调
            portBindFuture.addListener(this::portBindComplete);

            // 主线程等待netty-server端口绑定任务处理结果
            LockSupport.park();

            if (Boolean.FALSE.equals(nettyPortBind.get())) {
                log.error("Netty-server在{}端口启动失败", nettyProperties.getPort());
                throw new RuntimeException("Netty-server启动失败");
            }

            log.info("Netty-server在{}端口启动成功", nettyProperties.getPort());

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    /**
     * netty-server启动绑定端口后的回调
     * @param future
     */
    private void portBindComplete(Future future) {
        boolean portBindSuccess = future.isSuccess();
        nettyPortBind.set(portBindSuccess);

        LockSupport.unpark(springBootStartThread);

        springBootStartThread = null;
    }

    private static class NettyServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
        @Override
        protected void initChannel(NioSocketChannel ch) {
            log.info("====有新的客户端【{}】连接进来了，当前通道id为【{}】====", ch.remoteAddress(), ch.id());
        }
    }


}
