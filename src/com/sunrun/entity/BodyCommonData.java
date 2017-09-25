package com.sunrun.entity;

import java.io.Serializable;

public class BodyCommonData  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6395960254086624167L;
	private Head18BData head18BData;
	private int rcheckcode;//校验码 0x5f5f5f5f
	private byte channelType;//通道类型， 1，表示通道。2表示通道2，服务器发送的填0
	private byte controlType;//命令类型 
	private int dataLength;//数据长度：0x0d
	private byte[] dataPackets;//数据包体，具体数量由命令类型决定
	
	public BodyCommonData() {
		super();
	}
	/**
	 * 初始化数据
	 * @param head18bData 数据包头
	 * @param controlType 命令类型
	 * @param dataLength 数据包长度
	 * @param dataPackets 
	 */
	public BodyCommonData(Head18BData head18bData,	byte controlType, int dataLength, byte[] dataPackets) {
		super();
		head18BData = head18bData;
		this.rcheckcode = 0x5F5F5F5F;
		this.controlType = controlType;
		this.dataLength = dataLength;
		this.dataPackets = dataPackets;
		this.channelType = 1;
	}
	
	public Head18BData getHead18BData() {
		return head18BData;
	}
	public void setHead18BData(Head18BData head18bData) {
		head18BData = head18bData;
	}
	public int getRcheckcode() {
		return rcheckcode;
	}
	public void setRcheckcode(int rcheckcode) {
		this.rcheckcode = rcheckcode;
	}
	public byte getControlType() {
		return controlType;
	}
	public void setControlType(byte controlType) {
		this.controlType = controlType;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public byte[] getDataPackets() {
		return dataPackets;
	}
	public void setDataPackets(byte[] dataPackets) {
		this.dataPackets = dataPackets;
	}
	public byte getChannelType() {
		return channelType;
	}
	public void setChannelType(byte channelType) {
		this.channelType = channelType;
	}
	
}
