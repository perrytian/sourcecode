package syslog;

import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogIF;

public class Syslog4jClientDemo {

	public static void main(String[] args) {
		 SyslogIF syslog = Syslog.getInstance("udp");
		 syslog.getConfig().setHost("10.124.3.1");
		 syslog.getConfig().setPort(514);
		 syslog.info("Today is good day!!");
	}

}
