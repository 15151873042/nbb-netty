package com.nbb.netty.nio.buffer;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);

        buffer.put(10);
        buffer.put(20);
        buffer.put(30);
        buffer.put(40);
        buffer.put(50);

        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
        buffer.limit(10);

        buffer.put(60);
        buffer.put(70);
        buffer.put(80);

        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }



    }
}
