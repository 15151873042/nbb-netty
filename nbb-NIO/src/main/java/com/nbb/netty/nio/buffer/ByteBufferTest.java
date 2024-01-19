package com.nbb.netty.nio.buffer;


import cn.hutool.core.util.StrUtil;

import java.nio.ByteBuffer;

public class ByteBufferTest {

    /**
     * ByteBuffer 支持类型化的 put 和 get，put 放入的是什么数据类型，
     * get 就应该使用相应的数据类型来取出，否则可能有 BufferUnderflowException 异常
     */
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(Integer.MAX_VALUE);
        buffer.putLong(Long.MAX_VALUE);
        buffer.putChar('狗');

        buffer.flip();


        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());


    }
}
