package com.gangdan.server.ftp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangzhuo02 on 2016/12/15.
 */
public class FtpServer {

    private final int    port   = 8081;
    private       Logger logger = LoggerFactory.getLogger(FtpServer.class);

    public static void main(String[] args) throws Exception {
        FtpServer ftpServer = new FtpServer();
        ftpServer.init();
    }

    private void init() {
        EventLoopGroup bossEventLoop   = new NioEventLoopGroup(1);
        EventLoopGroup workerEventLoop = new NioEventLoopGroup(1);
        try {

            ServerBootstrap bootstrap = new ServerBootstrap().group(bossEventLoop, workerEventLoop).option(ChannelOption.SO_BACKLOG, 1024).
                    channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).
                    childHandler(new FtpChannelInitializer());
            Channel channel = bootstrap.bind(port).sync().channel();
//        这两行有什么区别
//        channel.close().sync();
            channel.closeFuture().sync();
        } catch (InterruptedException exception) {
            bossEventLoop.shutdownGracefully();
            workerEventLoop.shutdownGracefully();
        }
    }

}
