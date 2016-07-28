package com.sunrun.handler;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mina服务器端事件处理
 * @author
 */
public class MinaServerHandler extends IoHandlerAdapter {
	
	private SomeServer someServer;
	private int stt = 0;
	private static final Logger log = LoggerFactory.getLogger(MinaServerHandler.class);
	
	public MinaServerHandler(){
		setSomeServer(new SomeServer());
	}
	public void setSomeServer(SomeServer someServer) {
		this.someServer = someServer;
	}

	/**
	 * 服务端接收消息处理
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// 服务器收到数据，进行协议类型匹配，如果不匹配，则不执行相应操作。
		someServer.beginHandle(session, message);
	}

	/**
	 * 客户端连接的会话创建
	 **/
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		InetSocketAddress isa = (InetSocketAddress) session.getRemoteAddress();
		log.info("客户端:" + isa.getAddress() + ":" + isa.getPort()+ " 连接进来了");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		log.info(session.getRemoteAddress() + ":会话剩余：" + (--stt));
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		session.close(true);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		log.info("session open!!! (连接数)" + (++stt));
	}
}
