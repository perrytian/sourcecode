package jmx.objects;

import java.io.Serializable;

/**
 * Tomcat应用的性能
 * @author oss
 *
 */
public class TomcatWebAppPerf implements Serializable{
	
	private String webAppName;//应用名
	
	private int maxActiveSessions;//maxium numbers of activeSessions allowed or -1 for no limit
	
	private int maxActive;//maximum number of active sessions so far
	
	private int activeSessions;//活动会话数
	
	private int sessionCounter;//total number of sessions
	
	private int rejectedSessions;//number of sessions we rejected due to maxActive beeing reached
	
	private int expiredSessions;//number of sessions that expired.
	
	private int maxInactiveInterval;//The default maximum inactive interval for Sessions
	
	private int sessionIdLength;//the session id length(in bytes) of Sessions
	

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getRejectedSessions() {
		return rejectedSessions;
	}

	public void setRejectedSessions(int rejectedSessions) {
		this.rejectedSessions = rejectedSessions;
	}

	public int getExpiredSessions() {
		return expiredSessions;
	}

	public void setExpiredSessions(int expiredSessions) {
		this.expiredSessions = expiredSessions;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public int getSessionIdLength() {
		return sessionIdLength;
	}

	public void setSessionIdLength(int sessionIdLength) {
		this.sessionIdLength = sessionIdLength;
	}

	public String getWebAppName() {
		return webAppName;
	}

	public void setWebAppName(String webAppName) {
		this.webAppName = webAppName;
	}

	public int getMaxActiveSessions() {
		return maxActiveSessions;
	}

	public void setMaxActiveSessions(int maxActiveSessions) {
		this.maxActiveSessions = maxActiveSessions;
	}

	public int getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(int activeSessions) {
		this.activeSessions = activeSessions;
	}

	public int getSessionCounter() {
		return sessionCounter;
	}

	public void setSessionCounter(int sessionCounter) {
		this.sessionCounter = sessionCounter;
	}
	
}
