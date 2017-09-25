package com.sunrun.entity;

import java.io.Serializable;
import java.util.Calendar;

import com.sunrun.common.util.ConstantsUtil;

public class Head18BData implements Serializable {

	/**
	 * 设备端数据包头（4+1+1+6+6）18个字节
	 */
	private static final long serialVersionUID = 762851054332727477L;

	private int checkcode;// 校验码:0Xf5f5f5f5
	private byte version;// 版本号: 0x01
	private byte devType;// 设备类型: 手机0x00开关0x01其他待定
	private byte[] fromMac;// 源Mac地址：6bytes 开关Mac地址
	private byte[] toMac;// 目的Mac地址：6bytes 手机Mac地址（主动发送可写）
	private byte[] serverTime;//服务器发送数据时间
	private byte[] devTime;//设备发送数据时间
	

	public Head18BData() {

	}

	public Head18BData(byte devType, byte[] fromMac, byte[] toMac) {
		super();
		this.checkcode = 0xF5F5F5F5;
		this.version =  (byte) ConstantsUtil.VERSION_ZHIMAYUN;
		this.devType = devType;
		this.fromMac = fromMac;
		this.toMac = toMac;
		this.serverTime = new byte[5];
		this.devTime = getTimestamp();
	}
	
	public Head18BData(byte devType,byte version, byte[] fromMac, byte[] toMac) {
		super();
		this.checkcode = 0xF5F5F5F5;
		this.version = version;
		this.devType = devType;
		this.fromMac = fromMac;
		this.toMac = toMac;
		
		this.serverTime = new byte[5];
		this.devTime = getTimestamp();
	}
	
	public Head18BData(byte devType, byte[] fromMac, byte[] toMac,byte[] serverTime) {
		super();
		this.checkcode = 0xF5F5F5F5;
		this.version =  (byte) ConstantsUtil.VERSION_ZHIMAYUN;
		this.devType = devType;
		this.fromMac = fromMac;
		this.toMac = toMac;
		
		this.serverTime = serverTime;
		this.devTime = getTimestamp();
	}

	public int getCheckcode() {
		return checkcode;
	}

	public void setCheckcode(int checkcode) {
		this.checkcode = checkcode;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public byte getDevType() {
		return devType;
	}

	public void setDevType(byte devType) {
		this.devType = devType;
	}

	public byte[] getFromMac() {
		return fromMac;
	}

	public void setFromMac(byte[] fromMac) {
		this.fromMac = fromMac;
	}

	public byte[] getToMac() {
		return toMac;
	}

	public void setToMac(byte[] toMac) {
		this.toMac = toMac;
	}

	public byte[] getServerTime() {
		return serverTime;
	}

	public void setServerTime(byte[] serverTime) {
		this.serverTime = serverTime;
	}

	public byte[] getDevTime() {
		return devTime;
	}

	public void setDevTime(byte[] devTime) {
		this.devTime = devTime;
	}
	
	/**
	 * 获取服务器时间
	 * @return 字节数组 时，分，秒，毫秒/10 ，毫秒%10
	 */
	public static byte[] getTimestamp(){
		Calendar Cld = Calendar.getInstance();
		byte HH = (byte) Cld.get(Calendar.HOUR_OF_DAY);
		byte mm = (byte) Cld.get(Calendar.MINUTE);
		byte SS = (byte) Cld.get(Calendar.SECOND);
		int MILL =  Cld.get(Calendar.MILLISECOND);
		byte hm=(byte) (MILL/10);
		byte lm=(byte) (MILL%10);
		return new byte[]{HH,mm,SS,hm,lm};//时，分，秒，毫秒/10 ，毫秒%10
	}
	
	/**
	 * 发送获取时间字符串
	 * @param sendTime 时间字节数组
	 * @return 时间字符串
	 */
	public String getSendTimes(byte[] sendTime){
		String sendTimes=sendTime[0]+":"+sendTime[1]+":"+sendTime[2]+":"+(sendTime[0]*10+sendTime[0]);
		return sendTimes;
	}
	
}
