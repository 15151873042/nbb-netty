package com.nbb.netty.nio.groupChat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

@Slf4j
public class GroupChatServer {




    public static void main(String[] args) {

    }

    private Selector selector;

    private ServerSocketChannel listenChannel;

    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            this.selector = Selector.open();
            this.listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = this.selector.select();
                if (count > 0) {
                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

//    private void handle(SelectionKey key) {
//        if (key.isAcceptable()) {
//            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
//            SocketChannel socketChannel = listenChannel.accept();
//            socketChannel.configureBlocking(false);
//            socketChannel.register(selector, SelectionKey.OP_READ);
//        } else if (key.isReadable()) {
//            SocketChannel channel = (SocketChannel) key.channel();
//            ByteBuffer buffer = ByteBuffer.allocate(1024);
//            int readCount = channel.read(buffer);
//            if (readCount != -1) {
//                String clientMsg = new String(buffer.array());
//                log.info("读取到客户端发送的消息：{}", new String(buffer.array()));
//                log.info("准备向客户端转发消息");
//                this.sendMessageToOtherClient(clientMsg, channel);
//
//            }
//        }
//    }

    // 向所有其它client端发送消息
    private void sendMessageToOtherClient(String msg, SocketChannel selfChannel) {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        selector.keys().stream() // 遍历所有SelectionKey
                .map(SelectionKey::channel) // 获取SelectionKey对应的channel
                .filter(channel -> { // 排除当前发送该消息的客户端channel
                    return channel instanceof SocketChannel && channel != selfChannel;})
                .forEach(channel -> {
                    buffer.rewind();
                    try {
                        ((SocketChannel) channel).write(buffer); // 向client发送消息
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                });
    }












}
