package com.gangdan.rpc.bio.proxy;

import com.gangdan.rpc.bio.IPerson;
import com.gangdan.rpc.bio.Person;
import com.gangdan.rpc.bio.protocol.Protocol;
import com.gangdan.rpc.cache.Cache;
import com.gangdan.rpc.connector.IRPCConnector;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * Created by yangzhuo on 16-12-16.
 */
public class ServerProxy extends BaseProxy {

    private Class src;
    private Class impl;
    private Cache<String, Object> cache = new Cache<>();

    /**
     * 注册接口和实现类
     *
     * @param src
     * @param impl
     */
    public void register(Class src, Class impl) {
        this.src = src;
        this.impl = impl;
        cache.put(src.getCanonicalName(), impl);
    }



    public void init() {
        this.register(IPerson.class, Person.class);
    }

    public void test() throws Exception {
        ServerSocket serverSocket = new ServerSocket(8301);
        Socket       socket       = null;

//        while (!isStop) {
//            socket = serverSocket.accept();
//            if (!Objects.isNull(socket)) {
//                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
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
//            }
//        }
        serverSocket.close();
    }

    public static void main(String[] args) throws Exception {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.init();
        serverProxy.test();

    }

}
