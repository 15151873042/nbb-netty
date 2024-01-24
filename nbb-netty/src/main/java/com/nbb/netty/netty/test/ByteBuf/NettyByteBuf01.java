package com.nbb.netty.netty.test.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyByteBuf01 {


    //创建一个ByteBuf
    //说明
    //1. 创建 对象，该对象包含一个数组arr , 是一个byte[10]
    //2. 在netty 的buffer中，不需要使用flip 进行反转
    //   底层维护了 readerindex 和 writerIndex
    //3. 通过 readerindex 和  writerIndex 和  capacity， 将buffer分成三个区域
    // 0---readerindex 已经读取的区域
    // readerindex---writerIndex ， 可读的区域
    // writerIndex -- capacity, 可写的区域
    public static void main(String[] args) {

        ByteBuf buffer = Unpooled.buffer(10);

        log.info("写数据前的信息：capacity = {}，readerindex = {}，writeindex = {}", buffer.capacity(), buffer.readerIndex(), buffer.writerIndex());//10
        for (int i = 0; i < 8; i++) {
            buffer.writeByte(i);
        }
        System.out.println(buffer.arrayOffset());
        log.info("写数据后的信息：capacity = {}，readerindex = {}，writeindex = {}", buffer.capacity(), buffer.readerIndex(), buffer.writerIndex());//10

        for (int i = 0; i < buffer.writerIndex(); i++) {
            System.out.print(buffer.readByte()); // 每次读取后readerindex会自动向后移动
        }

        log.info("读数据后的信息：capacity = {}，readerindex = {}，writeindex = {}", buffer.capacity(), buffer.readerIndex(), buffer.writerIndex());//10
    }
}
