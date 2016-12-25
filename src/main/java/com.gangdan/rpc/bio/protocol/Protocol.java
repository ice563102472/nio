package com.gangdan.rpc.bio.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangzhuo on 16-12-16.
 */
@ToString
public class Protocol implements Serializable {

    private static final long serialVersionUID = 997446348954685410L;

    @Getter
    @Setter
    private String version;
    @Getter
    @Setter
    private String protocol;
    @Getter
    @Setter
    private List   head;
    @Getter
    @Setter
    private List   args;
    @Getter
    @Setter
    private String className;
    //如果是http服务 method就是接口路径
    @Getter
    @Setter
    private String method;
    @Getter
    @Setter
    private long   contentLength;
    @Getter
    @Setter
    private int    messageId;
}
