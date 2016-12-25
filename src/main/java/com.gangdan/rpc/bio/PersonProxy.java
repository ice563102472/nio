package com.gangdan.rpc.bio;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PersonProxy implements InvocationHandler {

    private Object target = null;

    public Object bind(Object object) {
        this.target = object;
        Object proxy = Proxy
                .newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);

        System.out.println(proxy.getClass());

        return proxy;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(this.target, args);
        return result != null ? result : null;
    }

    public static void main(String[] args) {
        IPerson     person = new Person();
        PersonProxy proxy  = new PersonProxy();
        person = (IPerson) proxy.bind(IPerson.class);
        person.test();
    }

}