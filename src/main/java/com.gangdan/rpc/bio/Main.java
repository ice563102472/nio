package com.gangdan.rpc.bio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by yangzhuo on 16-11-30.
 */
public class Main {
  public static void main(String[] args) {
    ByteBuf byteBuf = Unpooled.copiedBuffer("$_".getBytes());
    System.out.println(byteBuf);
  }
}
