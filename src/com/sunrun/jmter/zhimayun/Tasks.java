package com.sunrun.jmter.zhimayun;

import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.sunrun.code.ClientCodecFactory;
import com.sunrun.code.QueryMac;
import com.sunrun.common.util.DataHandle;
import com.sunrun.entity.BodyCommonData;
import com.sunrun.entity.Head18BData;
import com.sunrun.handler.MinaServerHandler;

public class Tasks{
	//private final String hostip = "smart.sunruncn.com";
	private  IoSession session;
	// 查询能耗服务器的mac地址
	// 本地的session map映射
	public  Set<IoSession> sessions = new HashSet<IoSession>();
	private  Head18BData head18BData = new Head18BData();
	public static byte[] hostMAC= { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	private static byte dest[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	private  IoConnector conn;
	private  ConnectFuture future;
	private static int ccc=0;private static int i=0;
	
	
	
	
	/*public static void main(String[] args) throws Exception {
		
		FileWriter writer = new FileWriter("D:/zhimayun_macAddr.csv", true);
		Random random = new Random();
		int i=0;
		while (i!=1000) {
			hostMAC=new byte[]{(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15)};
			writer.write("hostMAC,");
			writer.write(DataHandle.bytesToHexString(hostMAC));
			writer.write("\n");
			i++;
		}
		writer.write("\n");
		writer.close();
	}*/
	
	/* execute("192.168.1.101",9955);
	 Thread.sleep(3000);
	 close();*/
	
	public static void main(String[] args) throws Exception {
		hostMAC=DataHandle.HexStringToBytes("FF:FF:FF:FF:FF:FF");
		Tasks tasks=new Tasks();
		while (true) {
			 tasks.execute("192.168.1.101",9955);
			// tasks.execute("121.41.74.164",9955);
			 // tasks.execute("120.55.119.102",9955);
			 Thread.sleep(30000);
		}
	}
	
	public void initTCP(){
		conn = new NioSocketConnector();
		// 设置链接超时时间
		conn.setConnectTimeoutMillis(30000L);
		// 添加过滤器
		conn.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ClientCodecFactory()));
		
	}
	
	/*static{
		Tasks tasks=new Tasks();
		tasks.initTCP();
		try {
			//Random random = new Random();
			//hostMAC=new byte[]{(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15),(byte) random.nextInt(15)};
			//hostMAC=QueryMac.getMACAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public void clossSession(IoSession session){
		if(sessions.size()>0){
			Iterator<IoSession> it = sessions.iterator();
			while(it.hasNext()){
				IoSession ss=it.next();
				if(ss ==session){
					it.remove();
				}
			}
		}
	}
	
	public void execute(String hostip, int port) {
			if(sessions==null){
				sessions=new HashSet<IoSession>();
			}
			//已连接至TCP服务器的发送心跳包
			int size=sessions.size();
			//System.out.println(size);
			if(size>0){
				Iterator<IoSession> it = sessions.iterator();
				while(it.hasNext()){
					IoSession ss=it.next();
					if(ss != null&&ss.isConnected()){
						ss.write(sendData((byte) 0x07));
					}else {
						it.remove();
					}
				}
			}
			System.out.println("*"+hostip+"*"+port+"*"+DataHandle.bytesToHexString(hostMAC));
			//少于连接至TCP服务器客户端数量的进行连接操作，目前默认数量为5
			for(int i=0;i<2-size;i++){
				IoSession ss=connTcpServer(hostip, port);
				if(ss != null&&ss.isConnected()){
					sessions.add(ss);
				}
			}
	}
	/**
	 * 连接TCP服务器
	 * @author GDQ
	 * 2015年10月29日 上午10:33:11 
	 * @return IoSession
	 */
	public IoSession connTcpServer(String hostip, int PORT){
		if(conn==null||conn.isDisposing()){
			initTCP();
		}
		// 添加业务处理handler
		if(conn.getHandler()==null){
			conn.setHandler(new MinaServerHandler());
		}
		future = conn.connect(new InetSocketAddress(hostip, PORT));// 创建连接
		future.awaitUninterruptibly();// 等待连接创建完成
		try {
			if  (future.isDone()) {
			    if  (!future.isConnected()) {  //若在指定时间内没连接成功，则抛出异常   
			    	conn.dispose();    //不关闭的话会运行一段时间后抛出，too many open files异常，导致无法连接   
			        throw   new  Exception();  
			    }  
				session = future.getSession();// 获得session
				// 能耗服务器，模拟手机发送登录信息，在应用服务器上建立session会话映射。
				session.write(sendData((byte) 0x06));
				return session;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 向服务器发送数据格式
	 * @author GDQ
	 * 2015年10月29日 上午10:32:16 
	 * @param orderType 06 登录 07心跳包
	 * @return 数据
	 */
	public BodyCommonData sendData(byte orderType){
		head18BData = new Head18BData((byte) 0x0A, hostMAC,	dest);
		return new BodyCommonData(head18BData, orderType, (byte)0x1C, null);
	}
	
	public static BodyCommonData sendData(byte orderType,byte dataLength, byte[] dataPackets,byte[] toMac){
		Head18BData head18BData = new Head18BData((byte) 0x0A, hostMAC,	toMac);
		return new BodyCommonData(head18BData, orderType, dataLength, dataPackets);
	}

	// 字符串转换成字节数组
	public static byte[] HexstringTobytes(String str) {

		if (str == null || str.length() < 0) {
			return null;
		}
		String[] strArray = str.split("-");
		byte[] num = new byte[strArray.length];// 定义整型数组用来接收转换的字符串数组
		// 将字符串数组转换成整型数组
		for (int i = 0; i < strArray.length; i++) {
			// 注意转换写法
			num[i] = (byte) Integer.parseInt(strArray[i], 16);
		}
		return num;
	}
	
	public void close(){
		if(!conn.isDisposing()){
			conn.dispose();
		}
	}

	public Head18BData getHead18BData() {
		return head18BData;
	}

	public void setHead18BData(Head18BData head18bData) {
		head18BData = head18bData;
	}
	
}
