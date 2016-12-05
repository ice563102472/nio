package com.gangdan.rpc.bio;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangzhuo on 16-11-30.
 */
public class Client {

public static void main(String [] args) throws Exception{
  for(int index = 0; index < 10000; ++index) {
    Socket      socket = new Socket("127.0.0.1", 8391);
    PrintWriter writer = new PrintWriter(socket.getOutputStream());
    writer.println(UUID.randomUUID());
    writer.flush();
    socket.close();
  }
}

}
