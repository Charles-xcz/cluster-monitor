package com.ustc.sa.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author charles
 */
@Component
public class NettyServer {

    @Autowired
    private ServerBootstrap serverBootstrap;
    @Value("${websocket.port}")
    private int port;

    private Channel serverChannel;

    public void start() throws Exception {
        System.out.println("netty启动");
        serverChannel = serverBootstrap.bind(port).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannel.close();
        serverChannel.parent().close();
    }


}
