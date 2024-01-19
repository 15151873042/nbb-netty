package com.nbb.netty.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过channel拷贝数据
 */
public class FileChannelTransfer {

    public static void main(String[] args) throws Exception {

        FileInputStream fis = new FileInputStream("d:\\fileChannelCopy.txt");
        FileOutputStream fos = new FileOutputStream("d:\\fileChannelCopy2.txt");

        FileChannel channelRead = fis.getChannel();
        FileChannel channelWrite = fos.getChannel();

        //使用 transferForm 完成拷贝
        channelWrite.transferFrom(channelRead, 0, channelRead.size());

        fis.close();
        fos.close();

    }
}
