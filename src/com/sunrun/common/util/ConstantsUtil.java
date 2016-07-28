package com.sunrun.common.util;

/**
 * 定义相关使用常量，以及默认的一些数值
 * 
 * @author GDQ 2016年3月8日 上午10:49:17
 */
public class ConstantsUtil {
	
	
	/**
	 * 包头起始校验码
	 */
	public final static byte[] checkcode = { (byte) 0xf5, (byte) 0xf5,
			(byte) 0xf5, (byte) 0xf5 };
	/**
	 * 包体起始校验码
	 */
	public final static byte[] rcheckcode = { 0x5f, 0x5f, 0x5f, 0x5f };

	/**
	 * 最短通信协议长度
	 */
	public static int MINILENGTH = 24;

	/**
	 * 芝麻云协议版本号
	 */
	public static int VERSION_ZHIMAYUN = 0x03;

	/**
	 * 芝麻云设备类型
	 */
	public static int DEVTYPE_ZHIMAYUN = 0x0A;
	
	/**
	 * 芝麻云设备类型
	 */
	public static int DEVTYPE_SERVER_ZHIMAYUN = 0;
	/**
	 * MACADDR长度
	 */
	public static int LENGTH_MACADDR = 6;
	/**
	 * 芝麻云开门命令
	 */
	public final static byte CMD_OPEN_ZHIMAYUN = 2;
	/**
	 * 芝麻云查询命令
	 */
	public final static byte CMD_QUERY_ZHIMAYUN = 1;

	/**
	 * 芝麻云设置命令
	 */
	public final static byte CMD_SET_ZHIMAYUN = 3;

	/**
	 * 芝麻云重启命令
	 */
	public final static byte CMD_RESTART_ZHIMAYUN = 4;

	/**
	 * 芝麻云开门状态
	 */
	public final static byte STATE_OPEN_ZHIMAYUN = 1;

	/**
	 * 芝麻云关门状态
	 */
	public final static byte STATE_CLOSE_ZHIMAYUN = 0;

	/**
	 * 芝麻云进门状态
	 */
	public final static byte IN_DOOR_ZHIMAYUN = 0;

	/**
	 * 芝麻云出门状态
	 */
	public final static byte OUT_DOOR_ZHIMAYUN = 1;

	/**
	 * 芝麻云地磁未触发状态
	 */
	public final static byte TRIGGER_NOT_ZHIMAYUN = 0;

	/**
	 * 芝麻云地磁已触发状态
	 */
	public final static byte TRIGGER_ON_ZHIMAYUN = 1;

	/**
	 * 芝麻云触发离开状态
	 */
	public final static byte TRIGGER_OFF_ZHIMAYUN = 2;

	/**
	 * 芝麻云开门失败状态
	 */
	public final static byte OPEN_ERROR_ZHIMAYUN = 0;

	/**
	 * 芝麻云开门成功状态
	 */
	public final static byte OPEN_SECCESS_ZHIMAYUN = 1;

	/**
	 * 芝麻云设置成功状态
	 */
	public final static byte SET_SECCESS_ZHIMAYUN = 1;

	/**
	 * 芝麻云设置失败状态
	 */
	public final static byte SET_ERROR_ZHIMAYUN = 0;

	/**
	 * 芝麻云重启成功状态
	 */
	public final static byte RESTART_SECCESS_ZHIMAYUN = 1;

	/**
	 * 芝麻云重启失败状态
	 */
	public final static byte RESTART_ERROR_ZHIMAYUN = 0;

	/**
	 * 芝麻云用户异步通知地址
	 */
	public static String USER_NOTIFY_ADDR_ZHIMAYUN = null;
	
	/**
	 * 芝麻云用户异步通知用户标识参数
	 */
	public static String USER_NOTIFY_ADDR_REMARK_ZHIMAYUN = null;

	/**
	 * 芝麻云发送邮箱SMTP
	 */
	//public static String MAIL_SMTP_ZHIMAYUN = "smtp.163.com";
	public static String MAIL_SMTP_ZHIMAYUN = "smtp.qq.com";

	/**
	 * 芝麻云发送邮箱账号
	 */
	//public static String MAIL_SEND_USER_ZHIMAYUN = "zhimayun@163.com";
	public static String MAIL_SEND_USER_ZHIMAYUN = "3028260341@qq.com";

	/**
	 * 芝麻云发送邮箱密码
	 */
	//public static String MAIL_SEND_PASSWORD_ZHIMAYUN = "sunrun2016";
	public static String MAIL_SEND_PASSWORD_ZHIMAYUN = "qfasmkskuokcdehd";

	/**
	 * 芝麻云接收邮箱列表
	 */
	public static String[] MAILS_TO_ZHIMAYUN = null;
	
	/**
	 * 芝麻云邮箱初始化
	 */
	public static boolean MAIL_INIT_ZHIMAYUN = false;
	
	/**
	 * 芝麻云异步通知地址初始化
	 */
	public static boolean USER_NOTIFY_ADDR_INIT_ZHIMAYUN = false;
	
	/**
	 * 芝麻云开门失败
	 */
	public final static String ERROR_OPEN_ZHIMAYUN = "ERROR_OPEN";
	
	/**
	 * 芝麻云查询失败
	 */
	public final static String ERROR_QUERY_ZHIMAYUN = "ERROR_QUERY";
	
	/**
	 * 芝麻云设置失败
	 */
	public final static String ERROR_SET_ZHIMAYUN = "ERROR_SET";
	
	/**
	 * 芝麻云重启失败
	 */
	public final static String ERROR_RESTART_ZHIMAYUN = "ERROR_RESTART";
	
	/**
	 * 芝麻云绑定邮箱失败
	 */
	public final static String ERROR_BUNDMAILBOX_ZHIMAYUN = "ERROR_BUNDMAILBOX";
	
	/**
	 * 芝麻云查询邮箱列表失败
	 */
	public final static String ERROR_MAILBOXLIST_ZHIMAYUN = "ERROR_MAILBOXLIST";
	
	/**
	 * 芝麻云发送邮件失败
	 */
	public final static String ERROR_MAIL_SEND_ZHIMAYUN = "ERROR_MAIL_SEND";
	
	/**
	 * 芝麻云设置异步通知地址失败
	 */
	public final static String ERROR_ASYNNOTIFY_ZHIMAYUN = "ERROR_ASYNNOTIFY";
	
	/**
	 * 芝麻云异步通知失败
	 */
	public final static String ERROR_ASYNNOTIFY_SEND_ZHIMAYUN = "ERROR_ASYNNOTIFY_SEND";
	
	/**
	 * 芝麻云操作设备失败
	 */
	public final static String ERROR_DEVICE_ZHIMAYUN = "ERROR_DEVICE";
	
	/**
	 * 芝麻云操作成功
	 */
	public final static String SUCCESS_ZHIMAYUN = "SUCCESS";
	
	
}
