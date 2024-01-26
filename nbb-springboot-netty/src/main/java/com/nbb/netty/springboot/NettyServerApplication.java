package com.nbb.netty.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class NettyServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(NettyServerApplication.class, args);
        log.info("tomcat在{}端口启动", applicationContext.getEnvironment().getProperty("server.port"));
    }
}
