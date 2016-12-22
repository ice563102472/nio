package com.gangdan.rpc.aio.test;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by yangzhuo02 on 2016/12/20.
 */
@RequiredArgsConstructor
public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    @NonNull
    private AsynchronousSocketChannel socketChannel;

    @Override
    public void completed(Integer result, ByteBuffer attachment) {

        attachment.flip();
        int length = attachment.remaining();
        if (length <= 0) {
            System.out.println("客户端关闭");
            try {
                socketChannel.close();
            } catch (IOException ex) {

            }
            return;
        }
        byte[] body = new byte[length];
        attachment.get(body);
        String input = new String(body);
        System.out.println(input);
        String str = "hello world";
        ByteBuffer bb = ByteBuffer.allocate(str.getBytes().length);
        bb.put(str.getBytes());
        bb.flip();
        socketChannel.write(bb);
        attachment.clear();
        //继续接收客户端的数据
        socketChannel.read(attachment, attachment, this);


    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
