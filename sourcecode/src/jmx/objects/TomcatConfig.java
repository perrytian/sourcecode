package jmx.objects;

package com.cusi.cnms3.adapter.object;

import java.io.Serializable;

public class TomcatConfig implements Serializable{

	private String tomcatVersion;
	
	private String jvmVersion;
	
	private String jvmVendor;
	
	private String startTime;
	
	private String timeSpan;
	
	private String[] inputArguments;//Tomcat启动参数
	
	private JMXOperatingSystem operatingSystem;
	
	private TomcatUsersInfo usersInfo;
	
	
	public TomcatUsersInfo getUsersInfo() {
		return usersInfo;
	}

	public void setUsersInfo(TomcatUsersInfo usersInfo) {
		this.usersInfo = usersInfo;
	}

	public JMXOperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(JMXOperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String[] getInputArguments() {
		return inputArguments;
	}

	public void setInputArguments(String[] inputArguments) {
		this.inputArguments = inputArguments;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTimeSpan() {
		return timeSpan;
	}

	public void setTimeSpan(String timeSpan) {
		this.timeSpan = timeSpan;
	}

	public String getTomcatVersion() {
		return tomcatVersion;
	}

	public void setTomcatVersion(String tomcatVersion) {
		this.tomcatVersion = tomcatVersion;
	}

	public String getJvmVersion() {
		return jvmVersion;
	}

	public void setJvmVersion(String jvmVersion) {
		this.jvmVersion = jvmVersion;
	}

	public String getJvmVendor() {
		return jvmVendor;
	}

	public void setJvmVendor(String jvmVendor) {
		this.jvmVendor = jvmVendor;
	}

}
