package com.gangdan.rpc.bio.serialize;

/**
 * Created by yangzhuo on 16-12-16.
 */
public class JsonSerialize implements Serialize {

    @Override
    public byte[] serialize(Object object) {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return null;
    }
}
