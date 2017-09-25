package com.sunrun.code;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;

public class Context {
	private final CharsetDecoder decoder;  
    private IoBuffer buf;  
    private int matchCount = 0;  
    private int overflowPosition = 0;  
      
    public Context(Charset charset,Integer length) {  
        decoder = charset.newDecoder();  
        buf = IoBuffer.allocate(length).setAutoExpand(true);  
        buf.order(ByteOrder.LITTLE_ENDIAN);  
    }  
  
    public CharsetDecoder getDecoder() {  
        return decoder;  
    }  
  
    public IoBuffer getBuffer() {  
        return buf;  
    }  
  
    public int getOverflowPosition() {  
        return overflowPosition;  
    }  
  
    public int getMatchCount() {  
        return matchCount;  
    }  
  
    public void setMatchCount(int matchCount) {  
        this.matchCount = matchCount;  
    }  
  
    public void reset() {  
        overflowPosition = 0;  
        matchCount = 0;  
        decoder.reset();  
    }  
  
    public void append(IoBuffer in) {  
        getBuffer().put(in);  
    }  
  
}
