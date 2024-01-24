package com.nbb.netty.netty.httpServer;

import java.net.URI;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject
 */
@Slf4j
public class HttpServerChannelReadHandler extends SimpleChannelInboundHandler<HttpObject> {


    /**
     * 一次http请求，会回调本方法两次，一次msg是HttpRequest，一次msg是HttpContent（具体原理还不清除）
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;
            log.info("====http请求的uri为：{}====", httpRequest.uri());
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                return; // 请求favicon.ico，不做响应
            }

            // 回复信息给浏览器
            ByteBuf byteBuf = Unpooled.copiedBuffer("狗碗！", StandardCharsets.UTF_8);

            // 构造HttpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            ctx.writeAndFlush(response);
        } else if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            String requestContent = httpContent.content().toString(StandardCharsets.UTF_8);
            log.info("====请求体数据为：\n{}====", requestContent);
            // 回复信息给浏览器
            ByteBuf byteBuf = Unpooled.copiedBuffer(requestContent, StandardCharsets.UTF_8);

            // 构造HttpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            ctx.writeAndFlush(response);

        }

    }
}
