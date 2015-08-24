package jmx.objects;

import java.io.Serializable;

public class TomcatPerformance implements Serializable{
	
	private TomcatWebAppPerf[] webAppPerfs;
	
	private TomcatThreadPerf[] threadPerfs;
	
	private TomcatGlobalRequest[] globalRequestInfo;
	
	private int averageResposneTime;
	
	private int requestPerMinute;
	
	private int averageBytesPerMinute;
	
	private String heapMemoryUsage;
	
	private String nonheapMemoryUsage;
	
	private String perGenMemoryUsage;
	
	private String jvmTotalMemory;
//	
	private String jvmMaxMemory;
//	
	private String jvmFreeMemory;
	
	
	
	public String getJvmMaxMemory() {
		return jvmMaxMemory;
	}

	public void setJvmMaxMemory(String jvmMaxMemory) {
		this.jvmMaxMemory = jvmMaxMemory;
	}

	public String getJvmTotalMemory() {
		return jvmTotalMemory;
	}

	public void setJvmTotalMemory(String jvmTotalMemory) {
		this.jvmTotalMemory = jvmTotalMemory;
	}


	public String getJvmFreeMemory() {
		return jvmFreeMemory;
	}

	public void setJvmFreeMemory(String jvmFreeMemory) {
		this.jvmFreeMemory = jvmFreeMemory;
	}

	public TomcatGlobalRequest[] getGlobalRequestInfo() {
		return globalRequestInfo;
	}

	public void setGlobalRequestInfo(TomcatGlobalRequest[] globalRequestInfo) {
		this.globalRequestInfo = globalRequestInfo;
	}

	public String getHeapMemoryUsage() {
		return heapMemoryUsage;
	}

	public void setHeapMemoryUsage(String heapMemoryUsage) {
		this.heapMemoryUsage = heapMemoryUsage;
	}

	public String getNonheapMemoryUsage() {
		return nonheapMemoryUsage;
	}

	public void setNonheapMemoryUsage(String nonheapMemoryUsage) {
		this.nonheapMemoryUsage = nonheapMemoryUsage;
	}

	public String getPerGenMemoryUsage() {
		return perGenMemoryUsage;
	}

	public void setPerGenMemoryUsage(String perGenMemoryUsage) {
		this.perGenMemoryUsage = perGenMemoryUsage;
	}

	public TomcatWebAppPerf[] getWebAppPerfs() {
		return webAppPerfs;
	}

	public void setWebAppPerfs(TomcatWebAppPerf[] webAppPerfs) {
		this.webAppPerfs = webAppPerfs;
	}

	public TomcatThreadPerf[] getThreadPerfs() {
		return threadPerfs;
	}

	public void setThreadPerfs(TomcatThreadPerf[] threadPerfs) {
		this.threadPerfs = threadPerfs;
	}

	public int getAverageResposneTime() {
		return averageResposneTime;
	}

	public void setAverageResposneTime(int averageResposneTime) {
		this.averageResposneTime = averageResposneTime;
	}

	public int getRequestPerMinute() {
		return requestPerMinute;
	}

	public void setRequestPerMinute(int requestPerMinute) {
		this.requestPerMinute = requestPerMinute;
	}

	public int getAverageBytesPerMinute() {
		return averageBytesPerMinute;
	}

	public void setAverageBytesPerMinute(int averageBytesPerMinute) {
		this.averageBytesPerMinute = averageBytesPerMinute;
	}

//	public String getTotalMemory() {
//		return totalMemory;
//	}
//
//	public void setTotalMemory(String totalMemory) {
//		this.totalMemory = totalMemory;
//	}
//
//	public String getUsedMemory() {
//		return usedMemory;
//	}
//
//	public void setUsedMemory(String usedMemory) {
//		this.usedMemory = usedMemory;
//	}
//
//	public String getFreeMemory() {
//		return freeMemory;
//	}
//
//	public void setFreeMemory(String freeMemory) {
//		this.freeMemory = freeMemory;
//	}

}
