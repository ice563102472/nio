package com.gangdan.rpc.bio.protocol;

/**
 * Created by yangzhuo on 16-12-16.
 */
public abstract class Protocol {

  protected String version;
  protected String protocol;
  protected String head;
  protected String content;
  protected long   contentLength;
  protected int    messageId;
}
