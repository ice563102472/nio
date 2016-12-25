package com.gangdan.rpc.connector;

/**
 * 服务和读写都在这里完成了
 * 后续可以拆开
 * 设置connector类型
 * 1.bio
 * 2.nio
 * 3.aio
 * 4.netty
 * Created by yangzhuo on 16-12-25.
 */
public interface IRPCConnector {

    /**
     * 启动服务
     */
    public void startServeice();

    /**
     * 暂停服务
     */
    public void stopService();

    /**
     * 清理资源
     */
    public void clear();

    public void write(Object object);

    public Object read(byte[] bytes);

}
