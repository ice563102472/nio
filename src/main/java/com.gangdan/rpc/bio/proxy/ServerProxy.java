package com.gangdan.rpc.bio.proxy;

import com.gangdan.rpc.bio.IPerson;
import com.gangdan.rpc.bio.Person;
import com.gangdan.rpc.cache.Cache;
import com.gangdan.rpc.connector.BIOConnector;

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

    public static void main(String[] args) throws Exception {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.init();
        serverProxy.setConnector(new BIOConnector());
        serverProxy.register(IPerson.class, Person.class);
        serverProxy.setHost("127.0.0.1");
        serverProxy.setPort(8301);
        serverProxy.startServeice();


    }

}
