package com.gangdan.rpc.bio.proxy;

import com.gangdan.rpc.connector.AbstractRPCConnector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 默认采用jdk的动态代理实现方式
 * 只能是代理接口不能代理实现类
 * // * 如果需要代理实现类需要使用cdlib
 * Created by yangzhuo on 16-12-24.
 */
public class BaseProxy implements InvocationHandler {

    @Getter
    @Setter
    @NonNull
    private AbstractRPCConnector connector;
    @Getter
    private int                  port;
    @Getter
    private String               host;

    public void setPort(int port) {
        if (connector == null) {
            throw new NullPointerException("connector is null");
        }
        connector.setPort(port);
    }

    public void setHost(String host) {
        if (connector == null) {
            throw new NullPointerException("connector is null");
        }
        connector.setAddress(host);
    }

    public <T> T bind(Class<T> object) {
        T result = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{object}, this);
        return result;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(proxy, args);
        return result != null ? result : null;
    }

    public void stopService() {
        connector.stopService();
    }

    public void startServeice() {
        connector.startServeice();
    }


}
