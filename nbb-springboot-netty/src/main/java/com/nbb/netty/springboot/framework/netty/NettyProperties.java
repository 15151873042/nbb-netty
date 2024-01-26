package com.nbb.netty.springboot.framework.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyProperties {

    private Integer port;

    private Integer boosThread = 0;

    private Integer workerThread = 0;
}
