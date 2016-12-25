package com.gangdan.rpc.connector;

import com.gangdan.rpc.cache.Cache;
import lombok.*;

/**
 * Created by yangzhuo on 16-12-25.
 */
@ToString
public class AbstractRPCConnector implements IRPCConnector {
    @Getter
    @Setter
    @NonNull
    protected int port;
    @Getter
    @Setter
    @NonNull
    protected String  address = "127.0.0.1";
    //服务是否正常运行
    @Setter
    protected boolean isStop  = false;
    protected Cache<String, Object> cache = new Cache<>();


    @Override
    public void startServeice() {

    }

    @Override
    public void stopService() {

    }

    @Override
    public void clear() {

    }

    @Override
    public void write(Object object) {

    }

    @Override
    public Object read(byte[] bytes) {
        return new byte[0];
    }

    /**
     * 服务是否正常运行
     *
     * @return
     */
    public boolean isStop() {
        return this.isStop;
    }

}
