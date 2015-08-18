package syslog;

import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerEventHandlerIF;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.SyslogServerSessionlessEventHandlerIF;

public class Syslog4jServerDemo implements SyslogServerSessionlessEventHandlerIF{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//http://www.syslog4j.org/docs/javadoc/
	private static Logger logger = Logger.getLogger(Syslog4jServerDemo.class);
	
	public static final String SYSLOG_HOST = "localhost";
	public static final int SYSLOG_PORT = 514;
	public static final String UDP_PROTOCOL = "udp";
	    
	public static final int DEFAULT_QUEUE_SIZE = 1000;
	public static final int DEFAULT_BATCH_SIZE = 10;

	private transient ArrayBlockingQueue<String> syslog;
	//private transient HashMap<Long, List<String>> emittedButNonAcked;
	private transient SyslogServerIF server;
	
	public Syslog4jServerDemo(){
		syslog = new ArrayBlockingQueue<String>(DEFAULT_QUEUE_SIZE);
	    //emittedButNonAcked = Maps.newHashMapWithExpectedSize(DEFAULT_QUEUE_SIZE);
	    server = SyslogServer.getThreadedInstance(UDP_PROTOCOL.toLowerCase());
	    SyslogServerConfigIF config = server.getConfig();
	    config.setHost(SYSLOG_HOST);
		config.setPort(SYSLOG_PORT);
		config.addEventHandler(this);
	}

	
	
	public void emit(){
		 String message;
			try {
				message = syslog.take();
				logger.info(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	

	@Override
	public void destroy(SyslogServerIF server) {
		return;
	}

	@Override
	public void initialize(SyslogServerIF server) {
		return;
	}

	@Override
	public void event(SyslogServerIF server, SocketAddress socket,
			SyslogServerEventIF event) {
		String date = (event.getDate() == null ? new Date() : event.getDate()).toString();
		String facility = SyslogUtility.getFacilityString(event.getFacility());
		String level = SyslogUtility.getLevelString(event.getLevel());
		
		boolean interrupted = false;
		do {
			
			if (!syslog.offer(event.getMessage())) {
				try {
					syslog.take();
					syslog.offer(event.getMessage());
					interrupted = false;
					
				} catch (InterruptedException e) {
					// shouldnt happen, we take() if the queue full, so no waiting
					interrupted = true;
				}
			}
		} while (interrupted); // retry loop
		
		logger.info("接收到SYSLOG消息：{" + facility + "} " + date + " " + level + " " + event.getMessage());
	}

	@Override
	public void exception(SyslogServerIF server, SocketAddress socket,
			Exception exception) {
		return;
	}

	
	public static void main(String[] args) {
		Syslog4jServerDemo demoServer = new Syslog4jServerDemo();
		while(true){
			demoServer.emit();
		}
	}
}
