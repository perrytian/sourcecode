package jmx.objects;

import java.io.Serializable;

public class JMXOperatingSystem implements Serializable{
	
	private long committedVirtualMemorySize;
	
	private long freePhysicalMemorySize;
	
	private long totalPhysicalMemorySize;
	
	private long freeSwapSpaceSize;
	
	private long totalSwapSpaceSize;
	
	private long processCpuTime;
	
	private String osName;//windows 7
	
	private int availableProcessors;
	
	private String arch;//x86
	
	private double systemLoadAverage;
	
	private String osVersion;

	public long getCommittedVirtualMemorySize() {
		return committedVirtualMemorySize;
	}

	public void setCommittedVirtualMemorySize(long committedVirtualMemorySize) {
		this.committedVirtualMemorySize = committedVirtualMemorySize;
	}

	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}

	public long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}

	public void setTotalPhysicalMemorySize(long totalPhysicalMemorySize) {
		this.totalPhysicalMemorySize = totalPhysicalMemorySize;
	}

	public long getFreeSwapSpaceSize() {
		return freeSwapSpaceSize;
	}

	public void setFreeSwapSpaceSize(long freeSwapSpaceSize) {
		this.freeSwapSpaceSize = freeSwapSpaceSize;
	}

	public long getTotalSwapSpaceSize() {
		return totalSwapSpaceSize;
	}

	public void setTotalSwapSpaceSize(long totalSwapSpaceSize) {
		this.totalSwapSpaceSize = totalSwapSpaceSize;
	}

	public long getProcessCpuTime() {
		return processCpuTime;
	}

	public void setProcessCpuTime(long processCpuTime) {
		this.processCpuTime = processCpuTime;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public int getAvailableProcessors() {
		return availableProcessors;
	}

	public void setAvailableProcessors(int availableProcessors) {
		this.availableProcessors = availableProcessors;
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		this.arch = arch;
	}

	public double getSystemLoadAverage() {
		return systemLoadAverage;
	}

	public void setSystemLoadAverage(double systemLoadAverage) {
		this.systemLoadAverage = systemLoadAverage;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
}
