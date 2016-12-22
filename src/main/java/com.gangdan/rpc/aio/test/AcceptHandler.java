package com.gangdan.rpc.aio.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by yangzhuo02 on 2016/12/20.
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {

    private Logger logger = LoggerFactory.getLogger(AcceptHandler.class);

    @Override
    public void completed(AsynchronousSocketChannel socketChannel, Server server) {
        //递归接收请求
        server.asynchronousServerSocketChannel.accept(server, this);
        //递归读取数据,但是有可能设置的太小没有办法一次读取所有数据,
        //异步操作 在代码上是递归
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        //含义是什么,如何监控客户端,当客户端关闭的时候就关闭服务端
        //递归读取数据 attachment就是ReadHandler的V
        socketChannel.read(byteBuffer, byteBuffer, new ReadHandler(socketChannel));
    }

    @Override
    public void failed(Throwable exc, Server server) {
        logger.warn(exc.getMessage());
    }
}
