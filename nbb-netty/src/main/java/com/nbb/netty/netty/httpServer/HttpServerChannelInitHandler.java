package com.nbb.netty.netty.httpServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerChannelInitHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        pipeline.addLast("MyTestHttpServerHandler", new HttpServerChannelReadHandler());
    }
}
