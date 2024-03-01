package com.nbb.netty.springboot;

import com.nbb.netty.springboot.framework.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyServerApplicationRunner implements ApplicationRunner {

    @Autowired
    private NettyServer nettyServer;
    @Override
    public void run(ApplicationArguments args) {
        nettyServer.start();
    }
}
