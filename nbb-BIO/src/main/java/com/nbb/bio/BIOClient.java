package com.nbb.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO所谓阻塞时不管客户端还是服务端在读取数据（inputStream.read()）是阻塞的，必须等待另一方写数据
 *
 * @author 胡鹏
 * @date 2020/08/28
 */
public class BIOClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 6666);

        ExecutorService threadPool = Executors.newCachedThreadPool();

        threadPool.execute(() -> BIOClient.read(socket));
        threadPool.execute(() -> BIOClient.write(socket));
    }


    private static void read(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();

            byte[] bytes = new byte[1024];
            while (true) {
                int read = inputStream.read(bytes);
                if (read == -1) {
                    System.out.println("====读取到到达流结尾，数据读完了====");
                    break; // 读取到达流的末尾
                }
                System.out.println("====读取到server端发送的信息：" + new String(bytes, 0, read) + "====");
            }

//            inputStream.close();
        } catch (Exception e) {
            System.out.println("====bio-client读数据出错，错误信息是：" + e.getMessage() + "====");
        }
    }

    private static void write(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            while (true) {
                System.out.println("我也正在写数据");
                outputStream.write(("bio-client-" + System.currentTimeMillis()).getBytes());
                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            System.out.println("====bio-client写数据出错，错误信息是：" + e.getMessage() + "====");
        }
    }

}
