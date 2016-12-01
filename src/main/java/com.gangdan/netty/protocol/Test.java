/*
package com.gangdan.netty.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

*/
/**
 * Created by yangzhuo on 16-11-20.
 *//*

public class Test {
  public static void main(String[] args) throws Exception {
    Person.Builder personBuilder = Person.newBuilder();
    personBuilder.setId(1);
    personBuilder.setName("叉叉哥");
    personBuilder.setEmail("xxg@163.com");
    Person                xxg    = personBuilder.build();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    long                  start  = System.currentTimeMillis();
//    ArrayList             list   = null;
    for (int index = 0; index < 10000000; ++index) {
      // 按照定义的数据结构，创建一个Person
      //      Person.Builder xx = Person.newBuilder();
      // 将数据写到输出流，如网络输出流，这里就用ByteArrayOutputStream来代替

      //      xxg.writeTo(output);

      // -------------- 分割线：上面是发送方，将数据序列化后发送 ---------------

      //      byte[] byteArray = output.toByteArray();
      //
      //      // -------------- 分割线：下面是接收方，将数据接收后反序列化 ---------------
      //
      //      // 接收到流并读取，如网络输入流，这里用ByteArrayInputStream来代替
      //      ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
      //
      //      // 反序列化
      //      Person xxg2 = Person.parseFrom(input);
    }
    long end = System.currentTimeMillis();
    System.out.println(end - start);


  }
}
*/
