
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;


import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteNotificationListener;
import weblogic.management.WebLogicObjectName;
import weblogic.management.logging.WebLogicLogNotification;
import weblogic.management.runtime.LogBroadcasterRuntimeMBean;

/**
 * 
 * @author oss
 *
 */
public class WebLogicRemoteNotificationListener extends Thread implements RemoteNotificationListener{
	
	
	public static final int INFO = 64;
	public static final int WARNING = 32;
	public static final int ERROR = 16;
	public static final int NOTICE = 8;
	public static final int CRITICAL = 4;
	public static final int ALERT = 2;
	public static final int EMERGENCY = 1;
	
	public void run(){
		while(true){
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		try{
			String url = "t3://10.192.34.55:7001";
			String username = "weblogic";
			String password = "weblogic";
			
			Properties ht = new Properties();
			ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			ht.put(Context.PROVIDER_URL, url);
			ht.put(Context.SECURITY_PRINCIPAL, username);
			ht.put(Context.SECURITY_CREDENTIALS, password);
			
			Context ctx = new InitialContext(ht);
			
			//Retrieving the Administration MBeanHome interface
			MBeanHome serverSpecificHome = (MBeanHome)ctx.lookup(MBeanHome.ADMIN_JNDI_NAME);
			System.out.println("Got the Admin MBeanHome:"+serverSpecificHome+" from the Admin Server");
			//use MBeanHome to get the server`s MBeanServer Interface.
			MBeanServer mServer = serverSpecificHome.getMBeanServer();
			
			//Use getMBeanByClass to retrieve the object name.
			LogBroadcasterRuntimeMBean logObjName = (LogBroadcasterRuntimeMBean)serverSpecificHome.getMBean(LogBroadcasterRuntimeMBean.BROADCASTER_NAME, "LogBroadcasterRuntime");
			System.out.println(logObjName.getName());
			WebLogicRemoteNotificationListener listener = new WebLogicRemoteNotificationListener();
			//注册监听器
			mServer.addNotificationListener(logObjName.getObjectName(), listener, null, null);
			
			listener.start();
			
		}catch (Exception e) {
//			System.out.println("Exception caught:"+e);
			e.printStackTrace();
		}
		
	}

	@Override
	public void handleNotification(Notification notification, Object obj) {
		WebLogicLogNotification wln = (WebLogicLogNotification)notification;
		System.out.println("WebLogicLogNotification");        
		System.out.println(" type = " + wln.getType());        
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(" [Date = "+ sdf.format(new Date(wln.getTimeStamp()))+"]");
		System.out.println(" timestamp = " + wln.getTimeStamp());
		System.out.println(" severity = "+ wln.getSeverity());
		System.out.println(" subsystem = " + wln.getSubsystem());
		System.out.println(" servername = " + wln.getServername());
		System.out.println(" message id = " + wln.getMessageId());
		System.out.println(" message = " + wln.getMessage() + "\n");
	}
}
