package com.nbb.netty.springboot.controller;

import com.nbb.netty.springboot.framework.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/netty")
public class NettyController {

    @Autowired
    private NettyServer nettyServer;


    @RequestMapping("/start")
    public boolean start() {
        return nettyServer.startServer();
    }

    @RequestMapping("/stop")
    public boolean stop() {
        return nettyServer.stopServer();
    }

    @RequestMapping("/status")
    public String status() {
        return nettyServer.isRunning() ? "running" : "stop";
    }
}
