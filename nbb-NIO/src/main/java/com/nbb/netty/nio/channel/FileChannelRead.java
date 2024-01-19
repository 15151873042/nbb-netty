package com.nbb.netty.nio.channel;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 从channel读数据
 */
public class FileChannelRead {

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("d:\\fileChannel.txt");

        FileChannel channel = fis.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 将channel中的数据读取到buffer中
        channel.read(buffer);

        System.out.println(new String(buffer.array()));

        fis.close();
    }
}
