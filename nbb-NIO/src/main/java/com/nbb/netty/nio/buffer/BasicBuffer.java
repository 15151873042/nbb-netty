package com.nbb.netty.nio.buffer;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        // 设置初始化空间为10
        IntBuffer buffer = IntBuffer.allocate(10);

        System.out.println("buffer.put(10); buffer.put(20); buffer.put(30);");
        buffer.put(10);
        buffer.put(20);
        buffer.put(30);

        // 反转缓冲器，切换成读模式
        buffer.flip();
        System.out.println("buffer.flip()");
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

        // 设置position为0，此时又可以重新读取一边
        buffer.rewind();
        System.out.println("buffer.rewind()");
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

        // 将limit设置为容量大小，此时可以继续向buffer写数据
        System.out.println("buffer.limit(10)");
        buffer.limit(10);

        System.out.println("buffer.put(40); buffer.put(50); buffer.put(60);");
        buffer.put(40);
        buffer.put(50);
        buffer.put(60);

        System.out.println("buffer.flip()");
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
