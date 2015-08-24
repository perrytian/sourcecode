package jmx.objects;

import java.io.Serializable;

import javax.management.ObjectName;

public class TomcatThreadPerf implements Serializable{
	
	private String threadPortName;//端口名
	
	private int maxThreads;//最大线程数
	
	private int currentThreadCount;//当前线程数
	
	private int currentThreadsBusy;//繁忙线程数

	public String getThreadPortName() {
		return threadPortName;
	}

	public void setThreadPortName(String threadPortName) {
		this.threadPortName = threadPortName;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getCurrentThreadCount() {
		return currentThreadCount;
	}

	public void setCurrentThreadCount(int currentThreadCount) {
		this.currentThreadCount = currentThreadCount;
	}

	public int getCurrentThreadsBusy() {
		return currentThreadsBusy;
	}

	public void setCurrentThreadsBusy(int currentThreadsBusy) {
		this.currentThreadsBusy = currentThreadsBusy;
	}
	
}
