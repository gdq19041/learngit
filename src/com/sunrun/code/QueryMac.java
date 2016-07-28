package com.sunrun.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class QueryMac {
	/** 
	  * 获取当前操作系统名称. return 操作系统名称 例如:windows 7/XP,Linux 等. 
	  */ 	 
   public static String getOSName() {  
   	//toLowerCase()把字符串的大写字母转换为小写字母
       return System.getProperty("os.name").toLowerCase();  
   }  
   /** 
    * 获取Unix网卡的MAC地址. 非windows的系统默认调用本方法获取. 
    * 如果有特殊系统请继续扩充新的取MAC地址方法. 
    * @return MAC地址 
    */  
   public  byte[] getUnixMACAddress() {  
       String mac = null;  
       BufferedReader bufferedReader = null;  
       Process process = null; 
       byte src[]=new byte[6]; 
       String srcs[];
       try {  
           // Linux下的命令，一般取eth0作为本地主网卡  
           process = Runtime.getRuntime().exec("ifconfig eth0");  
           // 显示信息中包含有MAC地址信息  
           bufferedReader = new BufferedReader(new InputStreamReader(  
                   process.getInputStream(),"GBK"));  
           String line = null;  
           int index = -1;  
           while ((line = bufferedReader.readLine()) != null) {  
               // 寻找标示字符串[hwaddr]
           	//line.toLowerCase()让字符串转换为小写字母
               index = line.toLowerCase().indexOf("hwaddr");  
               if (index >= 0) {// 找到了  
                   // 取出MAC地址并去除2边空格  
                   mac = line.substring(index + "hwaddr".length() + 1).trim();
                   //字符数组
                   srcs=mac.split(":",6);
                   String src_mac ="";
                   for(int si=0;si<srcs.length;si++){
                	   src_mac=src_mac+srcs[si];
                   }
                   //src_mac=src_mac+"0000";
                   //字符数组转换为字节
                   src=HexstringTobytes(src_mac);
                   break;  
               }  
           }  
       } catch (IOException e) {  
           e.printStackTrace();  
       } finally {  
           try {  
               if (bufferedReader != null) {  
                   bufferedReader.close();  
               }  
           } catch (IOException e1) {  
               e1.printStackTrace();  
           }  
           bufferedReader = null;  
           process = null;  
       }  
       return src;  
   }  
   /** 
    * 获取Widnows网卡的MAC地址. 
    * @return MAC地址 
    */  
   public  byte[] getWindowsMACAddress() {  
       String mac = null;  
       BufferedReader bufferedReader = null;  
       Process process = null;
       byte src[]=new byte[6];
       String srcs[]; 
       try {  
           // windows下的命令，显示信息中包含有MAC地址信息  
           process = Runtime.getRuntime().exec("ipconfig /all");  
           bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));  
           String line = null;  
           int index = -1;  
           while ((line = bufferedReader.readLine()) != null) {  
               //System.out.println(line);  
               // 寻找标示字符串[physical  
               index = line.toLowerCase().indexOf("物理地址");  
                 
               if (index >= 0) {// 找到了  
                   index = line.indexOf(":");// 寻找":"的位置  
                   if (index >= 0) {  
                       // 取出MAC地址并去除2边空格  
                       mac = line.substring(index + 1).trim();
                      //字符数组
                       srcs=mac.split("-",6);
                       String src_mac ="";
                       for(int si=0;si<srcs.length;si++){
                    	   src_mac=src_mac+srcs[si];
                       }
                       //src_mac=src_mac+"0000";
                       //字符数组转换为字节
                       System.out.println(src_mac);
                       src=HexstringTobytes(src_mac);
                       
                   } 
                   break;  
               }  
           }  
       } catch (IOException e) {  
           e.printStackTrace();  
       } finally {  
           try {  
               if (bufferedReader != null) {  
                   bufferedReader.close();  
               }  
           } catch (IOException e1) {  
               e1.printStackTrace();  
           }  
           bufferedReader = null;  
           process = null;  
       }  
       return src;  
   }  
   /** 
    * windows 7 专用 获取MAC地址 
    */  
   public static byte[] getMACAddress() throws Exception {  
         
       // 获取本地IP对象  
       InetAddress ia = InetAddress.getLocalHost();  
       // 获得网络接口对象（即网卡），并得到MAC地址，MAC地址存在于一个byte数组中。  
       byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();  
       /*byte[]src_zero={00,00};
       byte[]src=new byte[8];
		for(int i=0;i<src_zero.length+mac.length;i++){			
			if(i<mac.length){
				src[i]=mac[i];
			}
			if(i>=mac.length){
				 src[i]=src_zero[i-mac.length];
			}
		}*/
       // 把字符串所有小写字母改为大写成为正规的mac地址并返回  
       return mac;  
   }
   
   public static byte[] HexstringTobytes(String mac){
		byte mac_byte_Array[]=new byte[6];
		if(mac==null||mac.length()<0){
			return null;
		}
		for(int i=0;i<mac.length();i++){
			String every_byte=mac.substring(i,i+2);
			int word_mac = Integer.parseInt(every_byte, 16);
			mac_byte_Array[i/2]=(byte)word_mac;
			//让MAC字符串走到下一个字节
			i++;
		}
		return mac_byte_Array;
	}
   
   public byte[] getMacAddress() throws Exception{
	   String os = getOSName();
	   byte mac[]=new byte[6];
	   if (os.equals("windows 7")) {  
           mac = getMACAddress();  
          // System.out.println(mac);  
       } else if (os.startsWith("windows")) {  
           // 本地是windows  
    	  mac= getWindowsMACAddress();  
       } else {  
           // 本地是非windows系统 一般就是Unix/Linux  
    	   mac = getUnixMACAddress();  
       }
	return mac;  
   }
   
// 16进制字节数组转字符串
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
					stringBuilder.append("-");
				}
			}
			return stringBuilder.toString().toUpperCase();
		}
}
