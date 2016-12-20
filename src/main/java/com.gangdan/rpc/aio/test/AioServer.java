package com.gangdan.rpc.aio.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * Created by yangzhuo02 on 2016/12/20.
 */
public class AioServer {

    private int port = 8081;

    public static void main(String[] args) throws Exception{

    }
    public void init()throws IOException {
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8081));
        serverSocketChannel.accept(this, new AcceptHandler());



    }

}
