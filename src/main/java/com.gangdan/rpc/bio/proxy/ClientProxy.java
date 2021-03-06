package com.gangdan.rpc.bio.proxy;

import com.gangdan.rpc.bio.IPerson;
import com.gangdan.rpc.bio.protocol.Protocol;
import com.gangdan.rpc.bio.protocol.ProtocolConstant;
import com.gangdan.rpc.connector.BIOConnector;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by yangzhuo on 16-12-16.
 */
public class ClientProxy extends BaseProxy {


    private Class    impl;
    private Protocol protocol;


    public <T> T register(Class<T> impl) {
        this.impl = impl;
        return bind(impl);
    }

    public static void main(String[] args) {
        ClientProxy clientProxy = new ClientProxy();
        clientProxy.init();
    }

    private void init() {
        IPerson person = this.register(IPerson.class);
        person.test();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Protocol protocol = new Protocol();
        protocol.setProtocol(ProtocolConstant.JDK_PROTOCOL);

        protocol.setMethod(method.getName());
        protocol.setClassName(method.getDeclaringClass().getCanonicalName());
        List params = new LinkedList();
        if (!Objects.isNull(args)) {
            params.add(Arrays.asList(args));
        }
        protocol.setArgs(params);
        Socket       socket       = new Socket("127.0.0.1", 8301);
        OutputStream outputStream = socket.getOutputStream();
        setConnector(new BIOConnector());
        startServeice();
        byte[]               bytes                = getConnector().write(protocol);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(bytes);


        return null;
    }
}
