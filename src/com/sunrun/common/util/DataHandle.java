package com.sunrun.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataHandle {
	private static final Logger log = LoggerFactory.getLogger(DataHandle.class);
	private static SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 16进制字节数组转-型大写字符串
	 * 
	 * @author GDQ 2015年10月19日 下午1:56:25
	 * @param switchMac
	 *            字节数组
	 * @return 字符串 xx-xx-xx-xx
	 */
	public static String bytesToHexString(byte[] switchMac) {
		StringBuilder stringBuilder = new StringBuilder();
		if (switchMac == null || switchMac.length <= 0) {
			return null;
		}
		for (int i = 0; i < switchMac.length; i++) {
			int v = switchMac[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			if (i < (switchMac.length - 1)) {
				stringBuilder.append(":");
			}
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * 字符串转换成字节数组
	 * 
	 * @author GDQ 2015年10月19日 下午1:56:07
	 * @param str
	 * @return
	 */
	public static byte[] HexStringToBytes(String str) {

		if (str == null || str.length() < 0) {
			return null;
		}
		String[] strArray = str.split(":");
		byte[] num = new byte[strArray.length];// 定义整型数组用来接收转换的字符串数组
		// 将字符串数组转换成整型数组
		for (int i = 0; i < strArray.length; i++) {
			// 注意转换写法
			num[i] = (byte) Integer.parseInt(strArray[i], 16);
		}
		return num;
	}

	/**
	 * 主要根据WEB服务器做相应转换，开关插座为同一WEB服务器，各种类型的配电箱为同一WEB服务器
	 * 
	 * @author GDQ 2015年10月19日 下午3:41:12
	 * @param devType
	 *            设备类型
	 * @return 设备类型
	 */
	public static byte devTypeStans(byte devType) {
		if (devType == 0x02) {
			devType = 0x01;
		} else if (devType >= 0x04 && devType <= 0x07) {
			devType = 0x04;
		}
		return devType;
	}

	/**
	 * 打印设备命令（数据包）类型
	 * 
	 * @author GDQ 2015年10月19日 下午2:10:17
	 * @param controlType
	 *            命令（数据包）类型
	 */
	public static void logDeviceOutput(byte controlType,String macAddr) {
		switch (controlType) {
		case 0x01:
			log.info("设备"+macAddr+"返回查询状态数据");
			break;
		case 0x02:
			log.info("设备"+macAddr+"返回控制状态数据");
			break;
		case 0x03:
			log.info("设备"+macAddr+"返回设置数据");
			break;
		case 0x04:
			log.info("设备"+macAddr+"返回重启状态数据");
			break;
		case 0x06:
			log.info("设备"+macAddr+"登录数据包");
			break;
		case 0x07:
			log.info("设备"+macAddr+"心跳数据包");
			break;
		case 0x0D:
			log.info("设备"+macAddr+"离线数据包");
			break;
		default:
			log.info("设备"+macAddr+" 发送" + controlType + "指令");
			break;
		}
	}

	/**
	 * 打印手机命令（数据包）类型
	 * 
	 * @author GDQ 2015年10月19日 下午2:10:17
	 * @param controlType
	 *            命令（数据包）类型
	 */
	public static void logPhoneOutput(byte controlType,String macAddr) {
		switch (controlType) {
		case 0x01:
			log.info("用户"+macAddr+"查询设备指令");
			break;
		case 0x02:
			log.info("用户"+macAddr+"控制设备指令");
			break;
		case 0x03:
			log.info("用户"+macAddr+"发送设备设置指令");
			break;
		case 0x04:
			log.info("用户"+macAddr+"发送重启设备指令");
			break;
		default:
			log.info("用户 发送" + controlType + "指令");
			break;
		}
	}
	
	/**
	 * 控制指令转字符串
	 * @author GDQ
	 * 2016年4月15日 上午8:48:50 
	 * @param controlType 指令类型
	 * @return
	 */
	public static String devCmdToString(int controlType) {
		switch (controlType) {
		case 0x01:
			return "查询指令";
		case 0x02:
			return "控制指令";
		case 0x03:
			return "设置指令";
		case 0x04:
			return "重启指令";
		default:
			return controlType + "指令";
		}
	}
	/**
	 * 随机数选择
	 * @author GDQ
	 * 2015年10月20日 下午1:28:37 
	 * @param size 数量
	 * @return 随机数
	 */
	public static int ranDomSelect(int size){
		return new Random().nextInt(size) + 1;
	}
	
	/**
	 * 校验接收数据格式是否正确
	 * @author GDQ
	 * 2016年3月8日 下午1:44:13 
	 * @param buf 接收到数据包
	 * @param length 数据包长度
	 * @param ccode 校验码
	 * @return true/false
	 */
	public static boolean checkCode(IoBuffer buf,int length,int ccode){
		buf.position(0);
		byte[] b = new byte[length-4];
		buf.get(b);
		int sum=sumBytesToASCII(b);
		if(sum==ccode){
			return true;
		}
		return false;
	}
	/**
	 *  计算校验码
	 * @author GDQ
	 * 2016年3月14日 下午3:56:24 
	 * @param buf 
	 * @param length
	 * @return
	 */
	public static int comuCode(IoBuffer buf,int length){
		buf.position(0);
		byte[] b = new byte[length-4];
		buf.get(b);
		return sumBytesToASCII(b);
	}
	
	/**
	 * 将字节数组转换成ASCII，计算相加之和
	 * @author GDQ
	 * 2016年3月8日 下午2:21:10 
	 * @param bb 字节数组
	 * @return sum 字节数组转换为ASCII相加之和
	 */
	public static int sumBytesToASCII(byte[] bb){
		int sum=0;
		if(bb!=null&&bb.length>0){
			byte[] eb; int v =0;
			for (int i = 0; i < bb.length; i++) {
				v = bb[i] & 0xFF; 
				//System.out.println(v);
				/* 
				eb=Integer.toHexString(v).getBytes();
				for (byte b : eb) {
					System.out.println(b);
					sum+=b;
				}*/
				sum+=v;
			}
		}
		return sum;
	}
	/**
	 * 字符串转指定长度byte数组
	 * @author GDQ
	 * 2016年3月8日 下午4:55:29 
	 * @param macAddr 唯一标识
	 * @param length 指定长度
	 * @return
	 */
	public static byte[] StringToBytes(String macAddr,Integer length){
		if (macAddr == null || macAddr.length() < 0) {
			return null;
		}
		byte[] num = new byte[length];// 定义整型数组用来接收转换的字符串数组
		byte[] strArray=macAddr.getBytes();
		
		System.arraycopy(strArray, 0, num, 0, strArray.length>length?length:strArray.length);
		return num;
	}
	
}
