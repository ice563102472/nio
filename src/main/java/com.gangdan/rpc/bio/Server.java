package com.gangdan.rpc.bio;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 目前只是简单的服务端程序,没有添加任何协议
 * 也没有处理任何异常信息
 * Created by yangzhuo on 16-11-30.
 */
public class Server {

  private static final int    THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;
  private              Logger logger       = LoggerFactory.getLogger(Server.class);
  private ExecutorService service;
  private ServerSocket    serverSocket;

  private void init() {
    if (service == null) {
      service = Executors.newFixedThreadPool(1);
    }

    if (Objects.isNull(serverSocket)) {
      try {
        serverSocket = new ServerSocket(8301);
        logger.info("server starup success");
      } catch (IOException ex) {
        logger.warn(String.format("%s:%s", ex.getMessage(), "server starup failed"));
      }

    }
  }

  public void mainRun() {

    this.init();
    ServerSocket serverSocket = this.serverSocket;
    Socket       socket       = null;
    while (true) {
      if (!this.service.isShutdown()) {
        try {
          socket = serverSocket.accept();
        } catch (Exception ex) {

        }
        new Thread(new SocketThread(socket)).start();
      } else {
        break;
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server();
    server.mainRun();
  }

  @RequiredArgsConstructor
  private class SocketThread implements Runnable {
    @NonNull
    private Socket  socket;
    private Scanner scanner;


    public void run() {
      if (!Objects.isNull(socket)) {
        try {
          InputStream inputStream = socket.getInputStream();
          scanner = new Scanner(inputStream);
          int result = 0;
          System.out.println(Thread.currentThread().getName());
          while ((scanner.hasNextLine())) {

          }
        } catch (Exception ex) {

        } finally {
          scanner.close();
          try {
            socket.close();
          } catch (IOException ex) {

          }
        }

      } else {
      }
    }
  }

}
