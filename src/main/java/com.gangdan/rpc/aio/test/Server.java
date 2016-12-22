package com.gangdan.rpc.aio.test;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by yangzhuo on 16-12-22.
 */
@RequiredArgsConstructor
public class Server implements Runnable {

    @NonNull
    private int port;
    private CountDownLatch downLatch = new CountDownLatch(1);
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    @Override
    public void run() {
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel = asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            asynchronousServerSocketChannel.accept(this, new AcceptHandler());
        } catch (IOException ex) {

        }

        try {
            downLatch.await();
        } catch (InterruptedException ex) {

        }


    }
}
