package com.sunrun.jmter.zhimayun;

import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.util.Date;
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
import com.sunrun.common.util.ConstantsUtil;
import com.sunrun.common.util.DataHandle;
import com.sunrun.entity.BodyCommonData;
import com.sunrun.entity.Head18BData;
import com.sunrun.handler.MinaServerHandler;
import com.sunrun.handler.SomeServer;

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
		 hostMAC=DataHandle.HexStringToBytes("11:22:33:44:55:66");
		for(int i=0;i<1;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					Tasks tasks=new Tasks();
					while (true) {
						  tasks.execute("101.37.163.225",9955,hostMAC);
						 try {
							Thread.sleep(20000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
	
	public void initTCP(){
		conn = new NioSocketConnector();
		// 设置链接超时时间
		conn.setConnectTimeoutMillis(3000L);
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
	
	public void execute(String hostip, int port,byte[] hostMAC) {
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
						ss.write(sendData((byte) 0x07,hostMAC));//发送心跳包
						byte[] dataPackets=new byte[4];
						dataPackets[0]=1;//驶入状况（0x00 进入0x01离开）
						dataPackets[1]=1;//触发地磁状态（0x00 未触发0x01已触发0x02触发离开）
						dataPackets[2]=1;//门禁状态（0x00 关0x01开）
						dataPackets[3]=0;//控制操作状态（0x00 开门失败0x01开门成功）
						byte[] serverTime=Head18BData.getTimestamp();
						//ss.write(Tasks.sendData(ConstantsUtil.CMD_OPEN_ZHIMAYUN, (byte)0x20, dataPackets,hostMAC,serverTime));
						//ss.write(Tasks.sendData(ConstantsUtil.CMD_OPEN_ZHIMAYUN, (byte)0x20, dataPackets,hostMAC,serverTime));
						//ss.write(Tasks.sendData(ConstantsUtil.CMD_OPEN_ZHIMAYUN, (byte)0x20, dataPackets,hostMAC,serverTime));
						dataPackets[3]=1;//控制操作状态（0x00 开门失败0x01开门成功）
						//ss.write(Tasks.sendData(ConstantsUtil.CMD_OPEN_ZHIMAYUN, (byte)0x20, dataPackets,hostMAC,serverTime));
						// byte[]	dataPackets=new byte[]{(byte)0x7B,(byte)0x22,(byte)0x50,(byte)0x6C,(byte)0x61,(byte)0x74,(byte)0x65,(byte)0x52,(byte)0x65,(byte)0x73,(byte)0x75,(byte)0x6C,(byte)0x74,(byte)0x22,(byte)0x3A,(byte)0x7B,(byte)0x22,(byte)0x6C,(byte)0x69,(byte)0x63,(byte)0x65,(byte)0x6E,(byte)0x73,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x22,(byte)0x5F,(byte)0xCE,(byte)0xDE,(byte)0x5F,(byte)0x22,(byte)0x2C,(byte)0x22,(byte)0x63,(byte)0x6F,(byte)0x6C,(byte)0x6F,(byte)0x72,(byte)0x56,(byte)0x61,(byte)0x6C,(byte)0x75,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x63,(byte)0x6F,(byte)0x6C,(byte)0x6F,(byte)0x72,(byte)0x54,(byte)0x79,(byte)0x70,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x74,(byte)0x79,(byte)0x70,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x63,(byte)0x6F,(byte)0x6E,(byte)0x66,(byte)0x69,(byte)0x64,(byte)0x65,(byte)0x6E,(byte)0x63,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x62,(byte)0x72,(byte)0x69,(byte)0x67,(byte)0x68,(byte)0x74,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x64,(byte)0x69,(byte)0x72,(byte)0x65,(byte)0x63,(byte)0x74,(byte)0x69,(byte)0x6F,(byte)0x6E,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x6C,(byte)0x6F,(byte)0x63,(byte)0x61,(byte)0x74,(byte)0x69,(byte)0x6F,(byte)0x6E,(byte)0x22,(byte)0x3A,(byte)0x7B,(byte)0x22,(byte)0x52,(byte)0x45,(byte)0x43,(byte)0x54,(byte)0x22,(byte)0x3A,(byte)0x7B,(byte)0x22,(byte)0x6C,(byte)0x65,(byte)0x66,(byte)0x74,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x74,(byte)0x6F,(byte)0x70,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x72,(byte)0x69,(byte)0x67,(byte)0x68,(byte)0x74,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x62,(byte)0x6F,(byte)0x74,(byte)0x74,(byte)0x6F,(byte)0x6D,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x7D,(byte)0x7D,(byte)0x2C,(byte)0x22,(byte)0x74,(byte)0x69,(byte)0x6D,(byte)0x65,(byte)0x55,(byte)0x73,(byte)0x65,(byte)0x64,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x63,(byte)0x61,(byte)0x72,(byte)0x42,(byte)0x72,(byte)0x69,(byte)0x67,(byte)0x68,(byte)0x74,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x63,(byte)0x61,(byte)0x72,(byte)0x43,(byte)0x6F,(byte)0x6C,(byte)0x6F,(byte)0x72,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x74,(byte)0x69,(byte)0x6D,(byte)0x65,(byte)0x53,(byte)0x74,(byte)0x61,(byte)0x6D,(byte)0x70,(byte)0x22,(byte)0x3A,(byte)0x7B,(byte)0x22,(byte)0x54,(byte)0x69,(byte)0x6D,(byte)0x65,(byte)0x76,(byte)0x61,(byte)0x6C,(byte)0x22,(byte)0x3A,(byte)0x7B,(byte)0x22,(byte)0x73,(byte)0x65,(byte)0x63,(byte)0x22,(byte)0x3A,(byte)0x31,(byte)0x34,(byte)0x38,(byte)0x37,(byte)0x30,(byte)0x36,(byte)0x33,(byte)0x38,(byte)0x33,(byte)0x37,(byte)0x2C,(byte)0x22,(byte)0x75,(byte)0x73,(byte)0x65,(byte)0x63,(byte)0x22,(byte)0x3A,(byte)0x38,(byte)0x38,(byte)0x34,(byte)0x35,(byte)0x32,(byte)0x31,(byte)0x7D,(byte)0x7D,(byte)0x2C,(byte)0x22,(byte)0x74,(byte)0x72,(byte)0x69,(byte)0x67,(byte)0x67,(byte)0x65,(byte)0x72,(byte)0x54,(byte)0x79,(byte)0x70,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x32,(byte)0x7D,(byte)0x2C,(byte)0x22,(byte)0x63,(byte)0x6D,(byte)0x64,(byte)0x22,(byte)0x3A,(byte)0x22,(byte)0x69,(byte)0x76,(byte)0x73,(byte)0x5F,(byte)0x72,(byte)0x65,(byte)0x73,(byte)0x75,(byte)0x6C,(byte)0x74,(byte)0x22,(byte)0x2C,(byte)0x22,(byte)0x69,(byte)0x64,(byte)0x22,(byte)0x3A,(byte)0x33,(byte)0x33,(byte)0x32,(byte)0x2C,(byte)0x22,(byte)0x69,(byte)0x6D,(byte)0x61,(byte)0x67,(byte)0x65,(byte)0x66,(byte)0x6F,(byte)0x72,(byte)0x6D,(byte)0x61,(byte)0x74,(byte)0x22,(byte)0x3A,(byte)0x22,(byte)0x6A,(byte)0x70,(byte)0x67,(byte)0x22,(byte)0x2C,(byte)0x22,(byte)0x74,(byte)0x69,(byte)0x6D,(byte)0x65,(byte)0x53,(byte)0x74,(byte)0x72,(byte)0x69,(byte)0x6E,(byte)0x67,(byte)0x22,(byte)0x3A,(byte)0x22,(byte)0x32,(byte)0x30,(byte)0x31,(byte)0x37,(byte)0x2D,(byte)0x30,(byte)0x32,(byte)0x2D,(byte)0x31,(byte)0x34,(byte)0x20,(byte)0x31,(byte)0x37,(byte)0x3A,(byte)0x31,(byte)0x37,(byte)0x3A,(byte)0x31,(byte)0x37,(byte)0x22,(byte)0x2C,(byte)0x22,(byte)0x66,(byte)0x75,(byte)0x6C,(byte)0x6C,(byte)0x49,(byte)0x6D,(byte)0x67,(byte)0x53,(byte)0x69,(byte)0x7A,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x63,(byte)0x6C,(byte)0x69,(byte)0x70,(byte)0x49,(byte)0x6D,(byte)0x67,(byte)0x53,(byte)0x69,(byte)0x7A,(byte)0x65,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x2C,(byte)0x22,(byte)0x61,(byte)0x63,(byte)0x74,(byte)0x69,(byte)0x76,(byte)0x65,(byte)0x5F,(byte)0x69,(byte)0x64,(byte)0x22,(byte)0x3A,(byte)0x30,(byte)0x7D,(byte)0x00};
						
						//dataPackets[0]=1;//驶入状况（0x00 进入0x01离开）
						//session.write(Tasks.sendData(controlType, (byte)0x1D, dataPackets,fromMacToMac));
						//session.write(Tasks.sendData((byte)9, 447, dataPackets,hostMAC));
					}else {
						it.remove();
					}
				}
			}
			System.out.println("*"+hostip+"*"+port+"*"+DataHandle.bytesToHexString(hostMAC));
			//少于连接至TCP服务器客户端数量的进行连接操作，目前默认数量为5
			for(int i=0;i<1-size;i++){
				IoSession ss=connTcpServer(hostip, port,hostMAC);
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
	public IoSession connTcpServer(String hostip, int PORT,byte[] hostMAC){
		if(conn==null||conn.isDisposing()){
			initTCP();
		}
		// 添加业务处理handler
		if(conn.getHandler()==null){
			conn.setHandler(new MinaServerHandler());
		}
		ConnectFuture future = conn.connect(new InetSocketAddress(hostip, PORT)/*,new InetSocketAddress(50000)*/);// 创建连接
		future.awaitUninterruptibly();// 等待连接创建完成
		try {
			if  (future.isDone()) {
			    try {
					if  (!future.isConnected()) {  //若在指定时间内没连接成功，则抛出异常   
						conn.dispose();    //不关闭的话会运行一段时间后抛出，too many open files异常，导致无法连接   
					}
				} catch (Exception e) {
					e.printStackTrace();
				}  
				session = future.getSession();// 获得session
				// 能耗服务器，模拟手机发送登录信息，在应用服务器上建立session会话映射。
				session.write(sendData((byte) 0x06,hostMAC));
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
	public BodyCommonData sendData(byte orderType,byte[] hostMAC){
		head18BData = new Head18BData((byte) 0x0C, hostMAC,	dest);
		SomeServer.devMacMap.put(DataHandle.bytesToHexString(hostMAC), (byte)0x0C);
		return new BodyCommonData(head18BData, orderType, (byte)0x20, null);
	}
	
	public static BodyCommonData sendData(byte orderType,int dataLength, byte[] dataPackets,byte[] toMac,byte[] serverTime){
		Head18BData head18BData = new Head18BData((byte) 0x0C, hostMAC,	toMac,serverTime);
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
