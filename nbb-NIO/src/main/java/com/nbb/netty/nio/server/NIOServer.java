package com.nbb.netty.nio.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class NIOServer {


    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // 创建一个在本地端口进行监听的服务Socket通道.并设置为非阻塞方式
        serverSocketChannel.configureBlocking(false);  // 必须配置为非阻塞才能往selector上注册，否则serverSocketChannel.register()会报错
        serverSocketChannel.socket().bind(new InetSocketAddress(7777));

        Selector selector = Selector.open(); // 创建一个选择器selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 把ServerSocketChannel注册到selector上，并且selector对客户端连接操作感兴趣

        while(true) {
            log.info("====等待事件发生====");
            int eventCount = selector.select(); // 轮询监听channel里的key，此处select是非阻塞的，返回值是事件的个数
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            if (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove(); // 删除本次已处理的key，防止下次selector.select()重复处理
                handle(selectionKey);
            }
        }
    }

    private static void handle(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                log.info("====有客户端连接事件发生了====");
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(key.selector(), SelectionKey.OP_READ); // 将新接入的客户端对应的socketChannel注册到select上，并且selector对客户端数据读取操作感兴趣
            } else if (key.isReadable()) {
                read(key);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private static void read(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(true) {
                int readCount = socketChannel.read(buffer); //  NIO非阻塞体现:首先read方法不会阻塞，当读取返回值为0时候，表示通道中展示没有可读的数据，当返回值为-1时表示读取到了结尾
                if (readCount == -1) {
                    log.info("====读取到到达流结尾，数据读完了====");
                    log.info("====客户端【{}】离线了====", socketChannel.getRemoteAddress());
                    key.cancel();
                    socketChannel.close();
                    break; // 读取到达达流的末尾
                } else if (readCount == 0) {
                    break; // 当前没有数据可读
                }
                log.info("====读取到client端发送的信息：{}====", new String(buffer.array()));
            }
        } catch(Exception e) {
            log.info("====读数据出错了====");
            log.error(e.getMessage(), e);
            key.cancel();
            try {
                socketChannel.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void write(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel)key.channel();
            ByteBuffer buffer = ByteBuffer.wrap(("nio-server-" + System.currentTimeMillis()).getBytes());
            while (true) {
                log.info("====我也正在写数据====");
                socketChannel.write(buffer);
                Thread.sleep(2000L);
            }
        } catch (Exception e) {
            log.info("====写数据出错了====");
            log.error(e.getMessage(), e);
        }
    }
}
