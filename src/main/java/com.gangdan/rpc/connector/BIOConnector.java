package com.gangdan.rpc.connector;

import com.gangdan.rpc.bio.Server;
import com.gangdan.rpc.bio.serialize.JdkSerialize;
import com.gangdan.rpc.bio.serialize.Serialize;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

/**
 * Created by yangzhuo on 16-12-25.
 */
@RequiredArgsConstructor
public class BIOConnector extends AbstractRPCConnector implements IRPCConnector {

    private static final int    THREAD_COUNT = 2;
    private              Logger logger       = LoggerFactory.getLogger(Server.class);
    private ExecutorService service;
    private ServerSocket    serverSocket;
    private Serialize serialize = new JdkSerialize();//默认采用jdk的序列化方式,正式环境需要通过配置文件来实现具体的序列化方法

    @Override
    public void startServeice() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            logger.warn(String.format("%s:%s", ex.getMessage(), "服务没有启动"));
        }
        logger.info("服务正常启动");
        Socket socket = null;

        while (!isStop()) {
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                logger.warn(String.format("%s%s", ex.getMessage(), "socket没有创建成功"));
            }
            if (!Objects.isNull(socket)) {
                new Thread(new SocketThread(socket)).start();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            logger.warn("服务器没有正常关闭");
        }
    }

    @Override
    public void stopService() {
        if (!Objects.isNull(service)) {
            service.shutdownNow();
        }

        if (!Objects.isNull(serverSocket)) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                logger.warn(ex.getMessage());
            }
        }

    }

    @Override
    public void clear() {
        super.clear();
        this.service = null;
        this.serverSocket = null;
    }

    @Override
    public byte[] write(Object object) {
        return serialize.serialize(object);
    }

    @Override
    public Object read(byte[] bytes) {
        return serialize.deserialize(bytes);
    }

    /**
     * 该线程不应该放在这里
     */
    @RequiredArgsConstructor
    private class SocketThread implements Runnable {
        @NonNull
        private Socket  socket;
        private Scanner scanner;

        public void run() {
            if (!Objects.isNull(socket)) {
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
                    byte[]              bytes               = new byte[1024];

                    int    hasNext = bufferedInputStream.read(bytes);
                    Object object  = read(bytes);
                    System.out.println(object);

                    //                Protocol          protocol          = (Protocol) objectInputStream.readObject();
                    //
                    //                //这里应该是具体的connector来做读写操作
                    //                //如果没有就返回错误信息
                    //                Class              clazz              = (Class) cache.get(protocol.getClassName());
                    //                Method             m1                 = clazz.getDeclaredMethod(protocol.getMethod());
                    //                Object             result             = m1.invoke(new Person());
                    //                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    //                objectOutputStream.writeObject(result);
                    //                socket.close();
                } catch (IOException ex) {

                }


            } else {
            }
        }
    }

    public static void main(String[] args) {

    }

}
