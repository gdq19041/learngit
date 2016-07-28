package com.sunrun.entity;

import java.io.Serializable;

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

	public Head18BData() {

	}

	public Head18BData(byte devType, byte[] fromMac, byte[] toMac) {
		super();
		this.checkcode = 0xF5F5F5F5;
		this.version = 0x03;
		this.devType = devType;
		this.fromMac = fromMac;
		this.toMac = toMac;
	}
	
	public Head18BData(byte devType,byte version, byte[] fromMac, byte[] toMac) {
		super();
		this.checkcode = 0xF5F5F5F5;
		this.version = version;
		this.devType = devType;
		this.fromMac = fromMac;
		this.toMac = toMac;
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

}
