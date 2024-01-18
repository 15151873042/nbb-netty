package com.nbb.bio;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(6666);
        ExecutorService threadPool = Executors.newCachedThreadPool();

        while (true) {
            System.out.println("====等待连接====");
            Socket socket = serverSocket.accept(); // 阻塞方法，等待连接连入，有连接进来时则继续执行
            System.out.println("====进来一个连接====");

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
                    System.out.println("====读取到到达流结尾，数据读完了====");
                    break; // 读取到达达流的末尾
                }
                System.out.println("====读取到client端发送的信息：" + new String(bytes, 0, read) + "====");
            }

//            inputStream.close();
        } catch (Exception e) {
            System.out.println("====bio-server读数据出错，错误信息是：" + e.getMessage() + "====");
        }
    }


    private static void write(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                System.out.println("我也正在写数据");
                outputStream.write(("bio-server-" + System.currentTimeMillis()).getBytes());
                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            System.out.println("====bio-server写数据出错，错误信息是：" + e.getMessage() + "====");
        }
    }
}
