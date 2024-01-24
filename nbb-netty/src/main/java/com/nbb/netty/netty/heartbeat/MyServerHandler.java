package com.nbb.netty.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当空闲达到对应阀值后就回调
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {

            //将  evt 向下转型 IdleStateEvent
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE: eventType = "读空闲"; break;
                case WRITER_IDLE: eventType = "写空闲"; break;
                case ALL_IDLE: eventType = "读写空闲"; break;
            }
            log.info("客户端{} {} 超时", ctx.channel().remoteAddress(), eventType);
            // TODO 服务器做相应处理..

            //如果发生空闲，我们关闭通道
            // ctx.channel().close();
        }
    }
}
