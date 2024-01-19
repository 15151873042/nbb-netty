package com.nbb.netty.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过channel拷贝数据
 */
public class FileChannelCopy {

    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream("d:\\fileChannelCopy.txt");
        FileOutputStream fos = new FileOutputStream("d:\\fileChannelCopy2.txt");

        FileChannel channelRead = fis.getChannel();
        FileChannel channelWrite = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(5);

        while (true) {
            buffer.clear(); // 清空buffer
            int read = channelRead.read(buffer);
            if (read == -1) { // 全部读完了
                break;
            }

            buffer.flip();
            channelWrite.write(buffer);
        }

        fis.close();
        fos.close();

    }
}
