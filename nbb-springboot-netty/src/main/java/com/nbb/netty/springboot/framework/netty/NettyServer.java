package com.nbb.netty.springboot.framework.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NettyServer {


    @Autowired
    private NettyProperties nettyProperties;

    public ServerBootstrap serverBootstrap;


    /**
     * 启动netty服务器
     */
    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(nettyProperties.getBoosThread());
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettyProperties.getWorkerThread());

        try {
            this.serverBootstrap = new ServerBootstrap();
            this.serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            log.info("====有新的客户端【{}】连接进来了，当前通道id为【{}】====", channel.remoteAddress(), channel.id());
                        }
                    });

            this.serverBootstrap.bind(nettyProperties.getPort());
            log.info("Netty在{}端口启动", nettyProperties.getPort());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
