package com.gangdan.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 本类没有考虑目标文件已经存在的情况
 * Created by yangzhuo on 16-12-4.
 */
public class CopyFile {

  public static void main(String[] args) throws Exception {
    CopyFile copyFile = new CopyFile();
    Path     source   = Paths.get("/media/yangzhuo/Seagate Backup Plus Drive1/电影/柴静雾霾调查：穹顶之下.HD1280高清国语中字.mp4.tar.gz");
    Path     target   = Paths.get("/tmp/ttttt.tar.gz");

    long start = System.currentTimeMillis();
//        copyFile.copyFileWithByteBuffer(source.toString(), target.toString());
    copyFile.copyFileWithMapBuffer(source.toString(), target.toString());

    long end = System.currentTimeMillis();
    System.out.println(end - start);
  }

  private void copyFileWithFiles(String source, String target) throws FileNotFoundException, IOException {

    Path pathSource = Paths.get(source);
    Path pathTarget = Paths.get(target);

    if (!Files.exists(pathSource)) {
      throw new FileNotFoundException(source);
    }
    Files.copy(pathSource, pathTarget, LinkOption.NOFOLLOW_LINKS);
  }

  private void copyFileWithByteBuffer(String source, String target) throws FileNotFoundException, IOException {
    Path pathSource = Paths.get(source);
    Path pathTarget = Paths.get(target);
    if (!Files.exists(pathSource)) {
      throw new FileNotFoundException(source);
    }

    File        fileSource     = pathSource.toFile();
    File        fileTarget     = pathTarget.toFile();
    FileChannel fileInChannel  = new RandomAccessFile(fileSource, "r").getChannel();
    FileChannel fileOutChannel = new RandomAccessFile(fileTarget, "rw").getChannel();
    //尽量的使用内存,不过由于本身的效率 以及IO的瓶颈,分配更多的内存也无法提高速度
    ByteBuffer byteBuffer = ByteBuffer.allocate(80000);
    int        hasRead;

    while ((hasRead = fileInChannel.read(byteBuffer)) > 0) {
      byteBuffer.flip();
      fileOutChannel.write(byteBuffer);
      byteBuffer.clear();
    }
    byteBuffer.clear();
    fileOutChannel.close();
    fileInChannel.close();

  }

  private void copyFileWithMapBuffer(String source, String target) throws FileNotFoundException, IOException {
    Path pathSource = Paths.get(source);
    Path pathTarget = Paths.get(target);
    if (!Files.exists(pathSource)) {
      throw new FileNotFoundException(source);
    }

    File             fileSource     = pathSource.toFile();
    File             fileTarget     = pathTarget.toFile();
    FileChannel      fileInChannel  = new RandomAccessFile(fileSource, "rw").getChannel();
    FileChannel      fileOutChannel = new RandomAccessFile(fileTarget, "rw").getChannel();
    int              startIndex     = 0;
    int              length         = 4096;
    long             endIndex       = fileSource.length();
    MappedByteBuffer inbuffer       = null;
    MappedByteBuffer outBuffer      = null;

    inbuffer = fileInChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileInChannel.size());
    outBuffer = fileOutChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileInChannel.size());
    outBuffer.put(inbuffer);
    unmap(inbuffer);
//    //清理这句运行时间比较长,如果去掉这句 运行时间会大幅提升
//    unmap(outBuffer);
    fileInChannel.close();
    fileOutChannel.close();
  }

  /**
   * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
   * 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检
   * 查是否还有线程在读或写
   *
   * @param mappedByteBuffer
   */
  public static void unmap(final MappedByteBuffer mappedByteBuffer) {
    try {
      if (mappedByteBuffer == null) {
        return;
      }

      mappedByteBuffer.force();
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
        @Override
        @SuppressWarnings("restriction")
        public Object run() {
          try {
            Method getCleanerMethod = mappedByteBuffer.getClass()
                                                      .getMethod("cleaner", new Class[0]);
            getCleanerMethod.setAccessible(true);
            sun.misc.Cleaner cleaner =
                (sun.misc.Cleaner) getCleanerMethod
                    .invoke(mappedByteBuffer, new Object[0]);
            cleaner.clean();

          } catch (Exception e) {
            e.printStackTrace();
          }
          System.out.println("clean MappedByteBuffer completed");
          return null;
        }
      });

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
