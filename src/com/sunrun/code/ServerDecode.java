package com.sunrun.code;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunrun.common.util.ConstantsUtil;
import com.sunrun.common.util.DataHandle;
import com.sunrun.entity.BodyCommonData;
import com.sunrun.entity.Head18BData;

/**
 * 解码
 * 
 * @author
 */
public class ServerDecode extends CumulativeProtocolDecoder {
	private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
	private final Charset charset;
	private int maxPackLength = 100;
	private static final Logger log = LoggerFactory.getLogger(ServerDecode.class);
	public ServerDecode() {//初始化字符集为当前虚拟机默认的字符编码格式的CharSet
		this(Charset.defaultCharset());
	}

	public ServerDecode(Charset charset) {
		this.charset = charset;
	}

	public int getMaxLineLength() {
		return maxPackLength;
	}

	public void setMaxLineLength(int maxLineLength) {
		if (maxLineLength <= 0) {
			throw new IllegalArgumentException("maxLineLength: "+ maxLineLength);
		}
		this.maxPackLength = maxLineLength;
	}

	/*
	 * TCP为了保证不发生丢包，就给每个字节一个序号， 同时序号也保证了传送到接收端实体的包的按序接收。
	 * 然后接收端实体对已成功收到的字节发回一个相应的确认(ACK)；
	 * 如果发送端实体在合理的往返时延(RTT)内未收到确认，那么对应的数据（假设丢失了）将会被重传。
	 * TCP用一个校验和函数来检验数据是否有错误；在发送和接收时都要计算校验和。
	 */
	private void getValid(IoBuffer buf, ProtocolDecoderOutput out) throws IOException {
		IoBuffer newBuf = IoBuffer.allocate(maxPackLength).setAutoExpand(true);
		int limit = buf.remaining();
		// 判断缓存中数据长度是否可构成一个完整的包
		while (buf.hasRemaining() && limit >= ConstantsUtil.MINILENGTH) {//24为开关项目最小数据包长度
			// 计算包长度,自动门长度为数据包体长度，其它为总长度
			buf.position(4);
			byte version=buf.get();
			int devtype = buf.get();
			int length=0;
			buf.position(22);
			int cmdType=buf.get();
			length = buf.get();
			
			buf.position(0);
			//if(cmdType!=6&&cmdType!=7&&devtype!=0){
			if(devtype!=0){
				log.info("设备数据          接收:"+buf.getHexDump());
			//}else if(cmdType!=8&&cmdType!=9&&devtype==0){
			}else if(devtype==0){
				log.info("用户数据          接收:"+buf.getHexDump());
			}
			System.out.println(" 接收:"+buf.getHexDump());
			//log.info("数据包length:"+length);
			
			if (limit >= length) {// 实际缓存数据长度>=消息长度
				// 获得包校验码
				buf.position(0);
				byte[] b = new byte[4];
				buf.get(b);
				buf.position(18);
				byte[] rb = new byte[4];
				buf.get(rb);
				int ccode = 0;
				boolean check=true;
				if(version>=ConstantsUtil.VERSION_ZHIMAYUN){
					buf.position(length-4);
					ccode=buf.getInt();//取校验码
					check=DataHandle.checkCode(buf, length, ccode);
					check=true;
				}
				if(Arrays.equals(ConstantsUtil.checkcode, b)&&(Arrays.equals(ConstantsUtil.rcheckcode, rb)||Arrays.equals(ConstantsUtil.checkcode, rb))&&check){
					buf.position(0);
					buf=doDecode(buf, out, length);
					newBuf.clear();
					newBuf.order(buf.order());
	                newBuf.put(buf);
	                newBuf.flip();
	                buf.clear();
					buf.put(newBuf);
					buf.flip();
					limit = buf.remaining();
					
					
				}else{
					if(version==ConstantsUtil.VERSION_ZHIMAYUN){
						out.write("fault");
					}
					// 包无效扔掉,会保留有用的数据包 错包+粘包
					//此段代码感觉有问题，待后续研究
					/*无效包保存*/
					buf.position(0);
					log.error("错包+粘包等无效数据:"+buf.getHexDump());
					/*无效包保存结束，丢弃无效包*/
					buf.clear();
					buf.limit(0);
					System.out.println("!@#@!#@!#@!#@!##@!#@!#@!#");
					break;
				}
			} else {
				//buf.position(0);
				break;
			}
		}
		
			buf.position(0);
			  byte[] b = new byte[buf.limit()];  
			  buf.get(b); 
			  String s=new String(b);
			  if(s.equals("sign in")||s.equals("alive")){
				  System.out.println(s);
			  		buf.clear();
			  		buf.limit(0);
			  }
	}
	
	private IoBuffer doDecode(IoBuffer buf, ProtocolDecoderOutput out,Integer dataLength) {
		Head18BData head18BData=new Head18BData();
		head18BData.setCheckcode(buf.getInt());
		byte version=buf.get();
		head18BData.setVersion(version);
		byte devType=buf.get();
		head18BData.setDevType(devType);
		byte[] srcc = new byte[6];
		for (int i = 0; i < 6; i++) {
			srcc[i] = buf.get();
		}
		head18BData.setFromMac(srcc);
		srcc = new byte[6];
		for (int i = 0; i < 6; i++) {
			srcc[i] = buf.get();
		}
		head18BData.setToMac(srcc);
		BodyCommonData bodyCommonData=new BodyCommonData();
		bodyCommonData.setHead18BData(head18BData);
		bodyCommonData.setRcheckcode(buf.getInt());
		byte cmdType=buf.get();
		bodyCommonData.setControlType(cmdType);
		/*
		 *  手机端协议版本为2时长度为4个字节，其他数据包长度为1个字节
		 * */
		if(version==0x02&&devType==0){
			buf.getInt();
			srcc = new byte[dataLength-27];
			for (int i = 0; i < srcc.length; i++) {
				srcc[i] = buf.get();
			}
			bodyCommonData.setDataPackets(srcc);
		}else {
			buf.get();
			srcc = new byte[dataLength-24];
			for (int i = 0; i < srcc.length; i++) {
				srcc[i] = buf.get();
			}
			bodyCommonData.setDataPackets(srcc);
		}
		bodyCommonData.setDataLength(dataLength);
		/*为避免协议反复修改，因此暂不进行区分收到数据是什么，直接进行转发处理，如有需要后期可在WEB服务器根据协议进行解析具体包*/
		
		out.write(bodyCommonData);
		return buf;
	}


	@Override
	protected boolean doDecode(IoSession session, IoBuffer buf,
			ProtocolDecoderOutput out) throws Exception {
		/*buf.order(ByteOrder.LITTLE_ENDIAN);*/
		getValid(buf, out);
		return false;
	}
}
