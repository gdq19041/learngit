package com.sunrun.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunrun.common.util.ConstantsUtil;
import com.sunrun.common.util.DataHandle;
import com.sunrun.entity.BodyCommonData;
import com.sunrun.jmter.zhimayun.Tasks;

/**
 * 这个类主要是通过spring注入给MinaServerHandler用的, 表示: mina在接收到信息后,该由主要业务类来处理 服务器处理类
 **/
public class SomeServer{
	
	private static SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 设备MAC和通道对应关系
	 */
	public static ConcurrentMap<String,  Set<IoSession>> sessions_cz = new ConcurrentHashMap<String, Set<IoSession>>();
	/**
	 * 设备MAC和服务器本地ip地址
	 */
	public static ConcurrentMap<String, List<String>> webip_cz = new ConcurrentHashMap<String, List<String>>();
	/**
	 * 接口控制设备响应存储信息
	 */
	public static ConcurrentMap<String, Set<Map<String,Object>>> http_zmy = new ConcurrentHashMap<String, Set<Map<String,Object>>>();
	
	/**
	 *设备上线离线相关信息存储
	 */
	public static ConcurrentMap<String, Map<String,Object>> http_dev_zmy = new ConcurrentHashMap<String, Map<String,Object>>();
	/**
	 * 手机MAC和通道对应关系
	 */
	private ConcurrentMap<String, IoSession> sessions_sj = new ConcurrentHashMap<String, IoSession>();
	/**
	 * web服务器MAC和通道set集合对应关系
	 */
	private ConcurrentMap<String, Set<IoSession>> sessions_web = new ConcurrentHashMap<String, Set<IoSession>>();
	/**
	 * 设备类型和服务器MAC集合对应关系
	 */
	private ConcurrentMap<String,Set<String>> webMacMap=new ConcurrentHashMap<String,Set<String>>();
	/**
	 * 设备MAC和设备类型对应关系
	 */
	private ConcurrentMap<String,String> devMacMap=new ConcurrentHashMap<String,String>();
	/**
	 * 设备MAC和手机MAC集合对应关系
	 */
	private ConcurrentMap<String, Set<String>> set_cz = new ConcurrentHashMap<String, Set<String>>();
	/**
	 * 手机MAC和设备MAC集合对应关系
	 */
	private ConcurrentMap<String, Set<String>> set_sj = new ConcurrentHashMap<String, Set<String>>();
	
	private static String webip = null;
	
	
	private final SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
			
	private final byte dest[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	
	private static final Logger log = LoggerFactory.getLogger(SomeServer.class);
	
	public void beginHandle(IoSession session, Object message) {
		
		if (message instanceof BodyCommonData) {
			BodyCommonData bodyCommonData=(BodyCommonData) message;
			byte controlType=bodyCommonData.getControlType();
			byte[] fromMac=bodyCommonData.getHead18BData().getFromMac();// 获取源地址
			byte[] toMac=bodyCommonData.getHead18BData().getToMac();// 获取目的地址
			byte version =bodyCommonData.getHead18BData().getVersion();// 获取版本号
			byte devType =bodyCommonData.getHead18BData().getDevType();// 获取设备类型
			String deviceIp=session.getRemoteAddress().toString();
	        deviceIp= deviceIp.substring(1, deviceIp.indexOf(":"));//获取数据来源IP地址
	        String strFromMac = DataHandle.bytesToHexString(fromMac);
	        //Thread current = Thread.currentThread(); 
			//System.out.println(current.getName()+"***SomeServer   指令controlType"+controlType);
    		if(controlType==0x06||controlType==0x07){//登录数据包和心跳数据包
			}else {
				deviceControlData(bodyCommonData, strFromMac, controlType,session);
			}
		}else {
			session.write(message);
		}
	}
	
	/**
	 * 设备功能数据处理
	 * @author GDQ
	 * 2015年10月20日 上午8:57:20 
	 * @param bodyCommonData
	 * @param strFromMac 设备MAC地址字符串形式
	 * @param controlType 数据类型
	 */
	public void deviceControlData(BodyCommonData bodyCommonData,String strFromMac,byte controlType,IoSession session){
		DataHandle.logDeviceOutput(controlType,strFromMac);
		if(bodyCommonData.getHead18BData().getVersion()==ConstantsUtil.VERSION_ZHIMAYUN){
			responseCmdByDoor(bodyCommonData, strFromMac, controlType,session);
		}
	}
	
	/**
	 * 处理芝麻云设备返回数据
	 * @author GDQ
	 * 2016年3月10日 下午3:28:35 
	 * @param bodyCommonData 数据包
	 * @param strFromMac mac地址
	 * @param controlType 控制命令
	 */
	public void responseCmdByDoor(BodyCommonData bodyCommonData,String strFromMac,byte controlType,IoSession session){
			byte[] dataPackets=bodyCommonData.getDataPackets();
			byte[] fromMacToMac=bodyCommonData.getHead18BData().getFromMac();
			switch (controlType) {//响应
			case ConstantsUtil.CMD_QUERY_ZHIMAYUN:
				
				dataPackets=new byte[4];
				dataPackets[0]=1;//开门状态
				dataPackets[1]=1;//继电器闭合时间
				dataPackets[2]=1;//进入地磁状态
				dataPackets[3]=1;//离开地磁状态
				session.write(Tasks.sendData(controlType, (byte)0x20, dataPackets,fromMacToMac));
				
				break;
			case ConstantsUtil.CMD_OPEN_ZHIMAYUN:
				dataPackets=new byte[4];
				dataPackets[0]=1;//驶入状况（0x00 进入0x01离开）
				dataPackets[1]=1;//触发地磁状态（0x00 未触发0x01已触发0x02触发离开）
				dataPackets[2]=1;//门禁状态（0x00 关0x01开）
				dataPackets[3]=1;//控制操作状态（0x00 开门失败0x01开门成功）
				session.write(Tasks.sendData(controlType, (byte)0x20, dataPackets,fromMacToMac));
				break;
			case ConstantsUtil.CMD_SET_ZHIMAYUN:
				dataPackets=new byte[2];
				dataPackets[0]=1;//驶入状况（0x00 进入0x01离开）
				dataPackets[1]=1;//触发地磁状态（0x00 未触发0x01已触发0x02触发离开）
				session.write(Tasks.sendData(controlType, (byte)0x1E, dataPackets,fromMacToMac));
				break;
			case ConstantsUtil.CMD_RESTART_ZHIMAYUN:
				dataPackets=new byte[1];
				dataPackets[0]=1;//驶入状况（0x00 进入0x01离开）
				session.write(Tasks.sendData(controlType, (byte)0x1D, dataPackets,fromMacToMac));
				break;
			default:
				break;
			}
	}
	
	public static ConcurrentMap<String, Set<IoSession>> getSessions_cz() {
		/*Element element = cache.get("sessions_cz");
    	if (element != null) {
    		sessions_cz=(ConcurrentMap<String, Set<IoSession>>) element.getValue();
    	}else {
    		cache.put(new Element("sessions_cz",sessions_cz));
		}*/
		return sessions_cz;
	}
	
	/**
	 * session集合添加元素
	 * @author GDQ
	 * 2016年4月25日 下午2:01:33 
	 * @param strFromMac
	 * @param session
	 */
	public static void sessions_czAddSet(String strFromMac,IoSession session){
		sessions_cz=getSessions_cz();
		Set<IoSession> set=new HashSet<IoSession>();
		if(sessions_cz!=null&&!sessions_cz.isEmpty()){
			set=getSessions_cz().get(strFromMac);
			if(set==null||set.size()==0){
				set=new HashSet<IoSession>();
			}
		}
		set.add(session);
		getSessions_cz().put(strFromMac, set);
	}
	/**
	 * 移除集合元素
	 * @author GDQ
	 * 2016年4月25日 下午2:10:53 
	 * @param strFromMac
	 * @param session
	 */
	public static void sessions_czRemoveSet(String strFromMac,IoSession session){
		Set<IoSession> set=getSessions_cz().get(strFromMac);
		if(set!=null&&set.size()>0){
			int size=set.size();
			Iterator<IoSession> it = set.iterator();
			while(it.hasNext()){
				IoSession ss= it.next();
				if (ss != null&&ss==session) {
					it.remove();
					if(size==1){
						getSessions_cz().put(strFromMac, set);
					}else {
						getSessions_cz().remove(strFromMac);
					}
					break;
				}
			}
		}
	}
	

	public static void setSessions_cz(ConcurrentMap<String, Set<IoSession>> sessions_cz) {
		SomeServer.sessions_cz = sessions_cz;
	}

	public ConcurrentMap<String, IoSession> getSessions_sj() {
		return sessions_sj;
	}

	public void setSessions_sj(ConcurrentMap<String, IoSession> sessions_sj) {
		this.sessions_sj = sessions_sj;
	}
	
	public ConcurrentMap<String, Set<String>> getSet_cz() {
		return set_cz;
	}

	public void setSet_cz(ConcurrentMap<String, Set<String>> set_cz) {
		this.set_cz = set_cz;
	}

	public ConcurrentMap<String, Set<String>> getSet_sj() {
		return set_sj;
	}

	public void setSet_sj(ConcurrentMap<String, Set<String>> set_sj) {
		this.set_sj = set_sj;
	}

	public ConcurrentMap<String, Set<IoSession>> getSessions_web() {
		return sessions_web;
	}

	public void setSessions_web(ConcurrentMap<String, Set<IoSession>> sessions_web) {
		this.sessions_web = sessions_web;
	}

	public ConcurrentMap<String, Set<String>> getWebMacMap() {
		return webMacMap;
	}

	public void setWebMacMap(ConcurrentMap<String, Set<String>> webMacMap) {
		this.webMacMap = webMacMap;
	}

	public ConcurrentMap<String, String> getDevMacMap() {
		return devMacMap;
	}

	public void setDevMacMap(ConcurrentMap<String, String> devMacMap) {
		this.devMacMap = devMacMap;
	}
	
	
	
	public static ConcurrentMap<String, Set<Map<String, Object>>> getHttp_zmy() {
		return http_zmy;
	}


	public static void setHttp_zmy(
			ConcurrentMap<String, Set<Map<String, Object>>> http_zmy) {
		SomeServer.http_zmy = http_zmy;
	}


	public static ConcurrentMap<String, Map<String, Object>> getHttp_dev_zmy() {
		return http_dev_zmy;
	}


	public static void setHttp_dev_zmy(ConcurrentMap<String, Map<String, Object>> http_dev_zmy) {
		SomeServer.http_dev_zmy = http_dev_zmy;
	}


	public static ConcurrentMap<String, List<String>> getWebip_cz() {
		return webip_cz;
	}


	public static void setWebip_cz(ConcurrentMap<String, List<String>> webip_cz) {
		SomeServer.webip_cz = webip_cz;
	}


	

}
