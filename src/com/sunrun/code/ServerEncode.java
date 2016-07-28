package com.sunrun.code;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunrun.common.util.ConstantsUtil;
import com.sunrun.common.util.DataHandle;
import com.sunrun.entity.BodyCommonData;

/**
 * 编码
 * 
 * @author
 */
public class ServerEncode extends ProtocolEncoderAdapter {
	private static final Logger log = LoggerFactory.getLogger(ServerEncode.class);
	private Charset charset = null;

	public ServerEncode(Charset charset) {
		this.setCharset(charset);
	}

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {

		IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);

		//buffer.order(ByteOrder.LITTLE_ENDIAN);// 修改此缓冲区的字节顺序,小端模式
		int length=0;
		byte type=0,cmd=0,version=0;
		if (message instanceof BodyCommonData) {
			BodyCommonData data=(BodyCommonData)message;
			/* 协议头 */
			buffer.putInt(data.getHead18BData().getCheckcode());
			version=data.getHead18BData().getVersion();
			buffer.put(version);
			type=data.getHead18BData().getDevType();
			buffer.put(type);
			buffer.put(data.getHead18BData().getFromMac());
			buffer.put(data.getHead18BData().getToMac());
			/* 数据包 */
			buffer.putInt(data.getRcheckcode());
			cmd=data.getControlType();
			buffer.put(cmd);
			length=data.getDataLength();
			if(version==0x02&&type==0){
				buffer.putInt(length);
			}else {
				buffer.put((byte)length);
			}
			if(data.getDataPackets()!=null){
				buffer.put(data.getDataPackets());
			}
			if(version==ConstantsUtil.VERSION_ZHIMAYUN){
				int sum=DataHandle.comuCode(buffer, length);
				buffer.position(length-4);
				buffer.putInt(sum);
			}
		}else {
			buffer.put(message.toString().getBytes());
			//buffer.putObject((String)message.getbyte);
		}
		buffer.flip();
		/*
		 * 编码的方式就是按照短信协议拼 装字符串到IoBuffer 缓冲区，然后调用ProtocolEncoderOutput的write()方法输出字节 流
		 */
		out.write(buffer);
		
		if(type!=0){
		//if(cmd!=6&&cmd!=7&&type!=0){
			log.info("设备数据          发送:"+buffer.getHexDump());
		}else if(type==0){
		//}else if(cmd!=8&&cmd!=9&&type==0){
			log.info("用户数据          发送:"+buffer.getHexDump());
		}
		System.out.println("发送:"+buffer.getHexDump());
		out.flush();
		buffer.free();
		
	}
public static void main(String[] args) {
	byte[] b="fault".getBytes();
	for (byte c : b) {
		System.out.println(c);
	}
	System.out.println("fault".getBytes());
}
	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

}
