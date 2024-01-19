package com.nbb.netty.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 向channel写数据
 */
public class FileChannelWrite {

    public static void main(String[] args) throws Exception {

        FileOutputStream fos = new FileOutputStream("d:\\fileChannel.txt");

        FileChannel channel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put("什么玩意".getBytes());

        buffer.flip(); // 反转后才能从buffer中读数据

        // 将buffer中的数据写入到channel中
        channel.write(buffer);

        fos.close();
    }
}
