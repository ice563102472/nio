package com.gangdan.server.ftp;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

/**
 * Created by yangzhuo02 on 2016/12/15.
 */
public class FtpHandler extends SimpleChannelInboundHandler {
    private Logger logger = LoggerFactory.getLogger(FtpHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info(String.format("%s:%s", "IP", getUserIp(ctx, msg)));

        int                     count   = 10000;
        HashMap<String, String> hashMap = new HashMap<>(150);

        for (int index = 0; index < count; ++index) {
            hashMap.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        }
        long start = System.currentTimeMillis();
        String jobId = JSON.toJSONString(hashMap);
        long end = System.currentTimeMillis();
        logger.info(String.format("%s:%s", "消耗時間", end - start));
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(jobId.getBytes())
        );
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(
                CONTENT_LENGTH,
                response.content().readableBytes()
        );
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private String getUserIp(ChannelHandlerContext ctx, Object msg) {

        String clientIp = "";

        if (msg instanceof HttpRequest) {
            clientIp = ((HttpRequest) msg).headers().get("X-Forwarded-For");
            if (Strings.isNotEmpty(clientIp) && !"unKnown".equalsIgnoreCase(clientIp)) {
                //多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = clientIp.indexOf(",");
                if (index != -1) {
                    return clientIp.substring(0, index);
                } else {
                    return clientIp;
                }
            }
            if (clientIp == null) {
                InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                        .remoteAddress();
                clientIp = insocket.getAddress().getHostAddress();
            }
        }
        return clientIp;
    }

}
