package jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;



/**
 * 
 * @author jianghao
 *
 */
public class JMXConnection {
	
	private static Logger logger = Logger.getLogger(JMXConnection.class);
			
	
	private JMXServiceURL serviceURL;
	private JMXConnector connector;
	private MBeanServerConnection  mbsc;
	
	private boolean authenticate = false;
	
	private String username;
	
	private String password;
	
	private String jmxURL;
	
	public JMXConnection(String host,int port,boolean authenticate,String username,String password){
		this.jmxURL = "service:jmx:rmi:///jndi/rmi://"+host+":"+port+"/jmxrmi";//tomcat jmx url 
		this.authenticate = authenticate;
		this.username = username;
		this.password = password;
	}
	
	public JMXConnection(String jmxURL){
		this.jmxURL = jmxURL;
	}
	
	public MBeanServerConnection getMBeanServerConnection(){
		try {
			serviceURL = new JMXServiceURL(jmxURL);
			if(authenticate == true){
			    //适用密码认证
			    Map map = new HashMap();   
			    String[] credentials = new String[] { username , password };   
			    map.put("jmx.remote.credentials", credentials);   
			    
			    try{
			    	connector = JMXConnectorFactory.connect(serviceURL, map);
			    }catch (IOException e) {
			    	e.printStackTrace();
//			    	Logger.out(Logger.ERROR, "JMX连接错误："+e.getMessage());
				}
			    
			}else{
				
				try{
					connector = JMXConnectorFactory.connect(serviceURL);//tomcat没启动或者没有启动jmx，此处会异常
				}catch(IOException e){
					e.printStackTrace();
//					Logger.out(Logger.ERROR, "JMX连接错误："+e.getMessage());
				}
			}
			if(connector != null){
				mbsc = connector.getMBeanServerConnection();
			}else{
				logger.error("远端JMX服务ServierURL:["+serviceURL+"] 没有启动");
			}
			
			return mbsc;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
//			e.printStackTrace();
			logger.error("JMX连接错误："+e.getMessage());
			return null;
		}   
	}
	
	public void close(){
		if(connector != null){
			try {
				connector.close();
				mbsc = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
