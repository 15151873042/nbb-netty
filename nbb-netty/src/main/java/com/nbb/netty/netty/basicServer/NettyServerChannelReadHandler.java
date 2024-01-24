package com.nbb.netty.netty.basicServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 从管道中读取数据的handler
 * workerGroup 的 EventLoop 处理请求的handler
 * 自定义一个handler，需要继承netty规定好的某个HandlerAdapter
 */
@Slf4j
public class NettyServerChannelReadHandler extends ChannelInboundHandlerAdapter {

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

        /// 可以直接向通道中写数据
//        channel.write(Unpooled.copiedBuffer("小狗碗！", StandardCharsets.UTF_8));

        /// 使用异步任务
//        EventLoop eventLoop = channel.eventLoop();
//        eventLoop.execute(() -> {
//            log.info("====eventLoop.execute()===="); // TODO 可以将处理事件较长的业务逻辑放入任务队列中处理，当前event事件处理完之后会执行任务队列中的任务
//        });

        /// 使用异步定时任务
//        eventLoop.schedule(() -> {
//            log.info("====eventLoop.schedule()===="); // FIXME 定时任务是如何执行的，是在所有任务处理完成之后，select.select(时间)时，入参传入下一个定时任务将要发生的时间，当时间到了之后，select.select(时间)的返回值为0，这时从定时任务队列取出任务后再放入任务队列，将其当作普通任务处理
//        }, 10, TimeUnit.SECONDS);
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
