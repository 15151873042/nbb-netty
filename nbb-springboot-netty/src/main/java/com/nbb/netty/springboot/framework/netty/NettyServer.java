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
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Component
public class NettyServer implements SmartLifecycle {


    @Autowired
    private NettyProperties nettyProperties;

    /** netty-server是否正在运行的标识 */
    private AtomicBoolean nettyRunning = new AtomicBoolean(false);

    /** netty-server启动端口是否绑定成功的标识 */
    private AtomicBoolean portBindSuccess = new AtomicBoolean(false);

    /** netty-server启动主线程 */
    private Thread nettyStartMainThread;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;


    @Override
    public void start() {
        boolean success = this.startServer();
        if (!success) {
            throw new RuntimeException("Netty-server启动失败");
        }
    }

    @Override
    public void stop() {
        boolean success = this.stopServer();
        if (!success) {
            throw new RuntimeException("Netty-server关闭失败");
        }
    }

    @Override
    public boolean isRunning() {
        return nettyRunning.get();
    }

    /**
     * 启动netty服务器
     */
    public boolean startServer() {
        if (!nettyRunning.compareAndSet(false, true)) {
            log.info("netty-server已启动");
            return false;
        }

        this.nettyStartMainThread = Thread.currentThread();

        bossGroup = new NioEventLoopGroup(nettyProperties.getBoosThread());
        workerGroup = new NioEventLoopGroup(nettyProperties.getWorkerThread());

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

        if (Boolean.FALSE.equals(portBindSuccess.get())) {
            nettyRunning.set(false);
            log.error("Netty-server在{}端口启动失败", nettyProperties.getPort());
            return false;
        }

        log.info("Netty-server在{}端口启动成功", nettyProperties.getPort());
        return true;
    }

    public boolean stopServer() {
        if (!nettyRunning.compareAndSet(true, false)) {
            log.info("netty-server已关闭");
            return false;
        }

        try {
            Future<?> boosGroupFuture = bossGroup.shutdownGracefully().sync();
            Future<?> workerGroupFuture = workerGroup.shutdownGracefully().sync();
            boolean bossSuccess = boosGroupFuture.isSuccess();
            boolean workerSuccess = workerGroupFuture.isSuccess();
            if (bossSuccess && workerSuccess) {
                bossGroup = null;
                workerGroup = null;
                portBindSuccess.set(false);
                log.info("Netty-server已关闭");
                return true;
            }
            throw new RuntimeException("netty-server关闭失败");
        } catch (Exception e) {
            this.nettyRunning.set(true);
            log.error(e.getMessage(), e);
            return false;
        }
    }






    /**
     * netty-server启动绑定端口后的回调
     * @param future
     */
    private void portBindComplete(Future future) {
        boolean portBindSuccess = future.isSuccess();
        this.portBindSuccess.set(portBindSuccess);

        LockSupport.unpark(nettyStartMainThread);

        nettyStartMainThread = null;
    }

    private static class NettyServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
        @Override
        protected void initChannel(NioSocketChannel ch) {
            log.info("====有新的客户端【{}】连接进来了，当前通道id为【{}】====", ch.remoteAddress(), ch.id());
        }
    }


}
