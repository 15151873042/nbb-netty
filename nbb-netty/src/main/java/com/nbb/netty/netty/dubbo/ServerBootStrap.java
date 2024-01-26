package com.nbb.netty.netty.dubbo;

public class ServerBootStrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
