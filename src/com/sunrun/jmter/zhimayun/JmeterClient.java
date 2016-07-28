package com.sunrun.jmter.zhimayun;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.sunrun.common.util.DataHandle;

public class JmeterClient extends AbstractJavaSamplerClient {
	private String hostip = "192.168.1.101";
	private int port = 9955;
	private String macAddr="";
	private SampleResult result; 
	private Tasks tasks=new Tasks();
	
	// private String path;  
    // private  
    // 设置传入的参数，可以设置多个，已设置的参数会显示到Jmeter的参数列表中  
    public Arguments getDefaultParameters() {  
        Arguments args = new Arguments();  
        args.addArgument("hostip", hostip);  
        args.addArgument("port", port+"");  
        args.addArgument("macAddr", macAddr);  
        return args;  
    }  
	
    // 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行  
    public void setupTest(JavaSamplerContext context) {  
        // 加载当前目录下的logback配置文件  
        result = new SampleResult();  
        result.sampleStart(); // 事务的起点  
        hostip = context.getParameter("hostip");  
        port = context.getIntParameter("port");  
        Tasks.hostMAC=DataHandle.HexStringToBytes(context.getParameter("macAddr"));
        //Tasks.execute(hostip, port);
    
    }  
  
    @Override  
    // 测试执行的循环体，根据线程数和循环次数的不同可执行多次  
    public SampleResult runTest(JavaSamplerContext arg) {  
        boolean success = true;  
        // result.sampleStart(); // 事务的起点  
        try {  
        	 hostip = arg.getParameter("hostip");  
             port = arg.getIntParameter("port");  
             tasks.execute(hostip, port);
        } catch (Exception e) {  
            success = false;  
        } finally {  
            // result.sampleEnd(); // 事务的终点  
            result.setSuccessful(success); // 设置本次事务成功或失败  
        }  
        return result;  
    }  
    
    
 // 结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行  
    public void teardownTest(JavaSamplerContext context) { 
        result.sampleEnd(); // 事务的终点  
        tasks.close();
    }  
	

}
