package com.gangdan.rpc.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by yangzhuo on 16-12-3.
 */
public class NioServer {

  private static final int     PORT = 8391;
  private volatile     boolean stop = false;
  private ServerSocketChannel serverSocketChannel;
  private Selector            selector;
  private Logger logger = LoggerFactory.getLogger(NioServer.class);

  public static void main(String[] args) throws IOException {
    NioServer nioServer = new NioServer();
    nioServer.init();
  }

  public void init() throws IOException {
    selector = Selector.open();
    serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.socket().bind(new InetSocketAddress(PORT), 1024);
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    new Thread(new SelectThread()).start();

    logger.info("nio server starup success");
  }

  class SelectThread implements Runnable {
    @Override
    public void run() {
      while (!stop) {
        try {
          if (selector.select(10) <= 0) {
            continue;
          }
          Set<SelectionKey>      selectionKeys = selector.selectedKeys();
          Iterator<SelectionKey> iterator      = selectionKeys.iterator();
          while (iterator.hasNext()) {
            SelectionKey keyElement = iterator.next();
            iterator.remove();
            handleInput(keyElement);
          }
        } catch (IOException ex) {
          logger.warn(ex.toString());
        }
      }
      try {
        if (selector != null) {
          selector.close();
        }
      } catch (IOException ex) {
        logger.warn(ex.toString());
      }
    }

    private void handleInput(SelectionKey selectionKey) {
      if (selectionKey.isValid()) {

        if (selectionKey.isAcceptable()) {
          ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
          try {
            SocketChannel socketChannel = serverChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);

          } catch (IOException ex) {
            logger.warn(ex.toString());
          }
        }

        if (selectionKey.isReadable()) {
          SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
          ByteBuffer    byteBuffer    = ByteBuffer.allocate(2048);
          try {
            readFromBytes(socketChannel, byteBuffer);
          } catch (IOException ex) {
            logger.warn(ex.toString());
          }
        }
      }
    }


    private String readFromBytes(ReadableByteChannel channel, ByteBuffer byteBuffer) throws IOException {
      StringBuilder readString = new StringBuilder("");
      int           hasRead;

      while ((hasRead = channel.read(byteBuffer)) > 0) {
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        readString.append(new String(bytes, "utf8"));
        byteBuffer.clear();
      }
      if (hasRead == -1) {
        channel.close();
      }
      return readString.toString();
    }

    private void doWrite(SocketChannel socketChannel, String response) {

      byte[]     bytes      = response.getBytes();
      ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
      byteBuffer.put(bytes);
      byteBuffer.flip();
      try {
        socketChannel.write(byteBuffer);
      } catch (IOException ex) {
        logger.warn(ex.toString());
      }
    }
  }
}
