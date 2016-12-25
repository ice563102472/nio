package com.gangdan.rpc.bio.serialize;

import com.gangdan.rpc.util.NioUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by yangzhuo on 16-12-24.
 */
public class JdkSerialize implements Serialize {

    private Logger logger = LoggerFactory.getLogger(JdkSerialize.class);
    private ObjectInputStream  objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket             socket;

    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream bis    = new ByteArrayOutputStream();
            ObjectOutputStream    stream = new ObjectOutputStream(bis);
            stream.writeObject(obj);
            stream.close();
            byte[] bytes = bis.toByteArray();
            //使用zip压缩，缩小网络包
            return NioUtils.zip(bytes);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    @Override
    public Object deserialize(byte[] bytes) {
        try {
            //使用zip解压缩
            byte[]               unzip  = NioUtils.unzip(bytes);
            ByteArrayInputStream bis    = new ByteArrayInputStream(unzip);
            ObjectInputStream    stream = new ObjectInputStream(bis);
            return stream.readObject();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
