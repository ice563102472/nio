package com.gangdan.rpc.aio;

import java.io.IOException;

/**
 * Created by yangzhuo02 on 2016/12/20.
 */
public class AioServer {

    private int port = 8081;

    public static void main(String[] args) throws Exception {
        AioServer aioServer = new AioServer();
        aioServer.init();
    }

    public void init() throws IOException {
        new Thread(new Server(port)).start();
    }

}
