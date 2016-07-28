package com.sunrun.code;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ClientCodecFactory implements ProtocolCodecFactory{
	
	private static final Charset charset = Charset.forName("UTF-8");
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("****客户端的解码用到了！****");
		return new ServerDecode(charset);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("****客户端的编码用到了！****");
		return new ServerEncode(charset);
	}
	
}
