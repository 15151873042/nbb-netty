package com.nbb.netty.netty.groupChat;

import cn.hutool.core.date.DateUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

@Slf4j
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {


    // 定义一个chanel组，管理所有channel
    // GlobalEventExecutor.INSTANCE 是全局执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    /**
     * channel建立之后回调该方法
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        String message = MessageFormat.format("[客户端]{0} 加入聊天 {1}", channel.remoteAddress(), DateUtil.now());
        channelGroup.writeAndFlush(message);
        channelGroup.add(channel);
    }

    /**
     * channel端开连接回调该方法
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String message = MessageFormat.format("[客户端]{0} 离开聊天 {1}", channel.remoteAddress(), DateUtil.now());
        log.info("channelGroup中的通道数为：{}", channelGroup.size()); // channel断开之后netty框架底层会自动将其从channelGroup中移除
        channelGroup.writeAndFlush(message);

    }


    /**
     * 暂时还不知道和handlerAdded区别，channel建立之后会先调用handlerAdded，然后再调用channelActive
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("[客户端]{} 上线了", ctx.channel().remoteAddress());
    }

    /**
     * 暂时还不知道和handlerRemoved区别，channel端开连接之后会先调用channelInactive，然后再调用handlerRemoved
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[客户端]{} 离线了", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(item -> {
            if (channel != item) { // 不是当前的channel,转发消息
                item.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息：" + msg + "\n");
            } else { //回显自己发送的消息给自己
                item.writeAndFlush("[自己]发送了消息：" + msg + "\n");
            }
        });
    }
}
