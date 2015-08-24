package snmp.trap;

import org.apache.log4j.Logger;
import org.snmp4j.mp.SnmpConstants;



public class SnmpTrapEngine{
	
	private static Logger logger = Logger.getLogger(SnmpTrapEngine.class);
	
	public static void main(String[] args){
		try {
			new SnmpTrapEngine().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void start() throws Exception {
		
		logger.info("===========  SNMP TRAP 监听模块启动[端口:"+SnmpConstants.DEFAULT_NOTIFICATION_RECEIVER_PORT+"]  ==============");
		TrapHandler trap = new TrapHandler(SnmpConstants.DEFAULT_NOTIFICATION_RECEIVER_PORT);
		trap.listen();
		logger.info("===========  SNMP TRAP 监听模块启动成功  ==============");
	}
}
