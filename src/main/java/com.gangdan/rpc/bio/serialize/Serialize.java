package com.gangdan.rpc.bio.serialize;

/**
 * Created by yangzhuo on 16-12-16.
 */
public interface Serialize {

    public byte[] serialize(Object object);

    public Object deserialize(byte[] bytes);

}
