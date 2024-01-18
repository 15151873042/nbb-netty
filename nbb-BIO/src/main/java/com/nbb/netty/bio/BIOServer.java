package com.nbb.netty.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class BIOServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(6666);
        ExecutorService threadPool = Executors.newCachedThreadPool();

        while (true) {
            log.info("====等待连接====");
            Socket socket = serverSocket.accept(); // 阻塞方法，等待连接连入，有连接进来时则继续执行
            log.info("====进来一个连接====");

            threadPool.execute(() -> BIOServer.read(socket));
            threadPool.execute(() -> BIOServer.write(socket));
        }
    }


    private static void read(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();

            byte[] bytes = new byte[1024];
            while (true) {
                int read = inputStream.read(bytes);
                if (read == -1) {
                    log.info("====读取到到达流结尾，数据读完了====");
                    break; // 读取到达达流的末尾
                }
                log.info("====读取到client端发送的信息：{}====", new String(bytes, 0, read));
            }

//            inputStream.close();
        } catch (Exception e) {
            log.info("====读数据出错了====");
            log.error(e.getMessage(), e);
        }
    }


    private static void write(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                log.info("====我也正在写数据====");
                outputStream.write(("bio-server-" + System.currentTimeMillis()).getBytes());
                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            log.info("====写数据出错了====");
            log.error(e.getMessage(), e);
        }
    }
}
