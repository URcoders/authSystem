package com.qgailab.authsystem.net.server;


import com.qgailab.authsystem.AbstractStarter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author linxu
 * @date 2019/8/22
 * <tip>take care of yourself.everything is no in vain.</tip>
 * UDP协议服务器
 */
//@Component
@Slf4j
public class StatelessServerStarter extends AbstractStarter {
    private final InetSocketAddress port;
    private final Bootstrap bootstrap;
    private ChannelFuture channelFuture;

    @Autowired
    public StatelessServerStarter(@Qualifier("udpAddress") InetSocketAddress port, Bootstrap bootstrap) {
        this.port = port;
        this.bootstrap = bootstrap;
    }

    @PostConstruct
    @Override
    public void start() throws Exception {
        log.info("UDP server start on (port) {}", port.getPort());
        channelFuture = bootstrap.bind(port).sync().channel().closeFuture().await();
    }

    @PreDestroy
    @Override
    public void stop() throws Exception {
        log.info("UDP server stop ");
        channelFuture.channel().closeFuture().sync();
    }
}
