package com.gangdan.server.ftp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by yangzhuo02 on 2016/12/15.
 */
public class FtpHandler extends SimpleChannelInboundHandler {
  private              Logger           logger   = LoggerFactory.getLogger(FtpHandler.class);
  private              FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
  private              File             rootFile = new File("/home/yangzhuo");
  //默认是utf-8编码
  private static final String           CODE     = "UTF-8";

  /**
   * 默认构造函数
   */
  public FtpHandler() {
    response.headers().set(CONTENT_TYPE, "text/html; charset=" + CODE);
  }

  /**
   * 使用aop记录日志
   *
   * @param ctx
   * @param msg
   * @throws Exception
   */
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    HttpRequest httpRequest;
    logger.info(String.format("%s:%s", "IP", getUserIp(ctx, msg)));
    if (msg instanceof HttpRequest) {
      httpRequest = (HttpRequest) msg;
      if (httpRequest.method() != HttpMethod.GET) {
        setError(ctx, MsgConstant.PROTOCOL_ERROR_MSG);
        return;
      }

      File tmp;

      String url = httpRequest.uri();
      if ("/".equals(url)) {
        tmp = rootFile;
      } else {
        tmp = new File(URLDecoder.decode(url, "UTF-8"));
      }

      if (!tmp.exists()) {
        setError(ctx, MsgConstant.FILE_NOT_EXISTS);
        logger.warn(String.format("%s %s", tmp.getAbsolutePath(), "不存在"));
        return;
      }
      String result;
      if (tmp.isDirectory()) {
        File[] fileList = tmp.listFiles();
        result = outpuResult(fileList);
      } else {
        RandomAccessFile accessFile = new RandomAccessFile(tmp, "r");
        byte[]           bytes      = new byte[(int) tmp.length()];
        accessFile.read(bytes);
        accessFile.close();
        result = new String(bytes, CODE);
      }

      response.content().writeBytes(Unpooled.wrappedBuffer(result.getBytes()));
      response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
      response.headers().setInt(
          CONTENT_LENGTH,
          response.content().readableBytes()
      );
      ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
  }

  private String getUserIp(ChannelHandlerContext ctx, Object msg) {

    String clientIp = "";

    if (msg instanceof HttpRequest) {
      clientIp = ((HttpRequest) msg).headers().get("X-Forwarded-For");
      if (Strings.isNotEmpty(clientIp) && !"unKnown".equalsIgnoreCase(clientIp)) {
        //多次反向代理后会有多个ip值，第一个ip才是真实ip
        int index = clientIp.indexOf(',');
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

  private void setError(ChannelHandlerContext ctx, String msg) {

    ByteBuf buf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
    response.content().writeBytes(buf);
    ctx.writeAndFlush(response);
  }

  /**
   * 輸出最後結果
   *
   * @param fileList
   * @return
   */
  private String outpuResult(File[] fileList) {
    List<String> resultList = new ArrayList<>();
    resultList.add("<html>");
    resultList.add("<p>.</p>");
    resultList.add("<p>..</p>");
    for (File file : fileList) {
      if (!file.isHidden()) {
        if (file.isDirectory()) {
          resultList.add(outputDirectory(file));
        } else if (file.isFile()) {
          resultList.add(outputFile(file));
        }
      }
    }
    resultList.add("</html>");
    return String.join("", resultList);
  }

  /**
   * 输出文件样式
   *
   * @param file
   * @return
   */
  private String outputFile(File file) {
    List<String> stringList = new ArrayList<>();
    stringList.add("<p>");
    stringList.add("<a href=' " + file.getAbsolutePath() + "'>");
    stringList.add("<font color='red'>");
    stringList.add(file.getName());
    stringList.add("</font>");
    stringList.add("</a>");
    stringList.add("</p>");
    return String.join("", stringList);
  }

  /**
   * 输出文件夹样式
   *
   * @param file
   * @return
   */
  private String outputDirectory(File file) {
    List<String> stringList = new ArrayList<>();
    stringList.add("<p>");
    stringList.add("<a href=' " + file.getAbsolutePath() + "'>");
    stringList.add("<font color='blue'>");
    stringList.add(file.getName());
    stringList.add("</font>");
    stringList.add("</a>");
    stringList.add("</p>");
    return String.join("", stringList);
  }

}
