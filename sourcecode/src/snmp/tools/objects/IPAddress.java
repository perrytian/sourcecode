package snmp.tools.objects;

public class IPAddress {
	private String ipAdEntAddr;
	private long ipAdEntIfIndex;
	private String ipAdEntNetMask;
	private String ipAdEntBcastAddr;
	private int ipAdEntReasmMaxSize;
	public String getIpAdEntAddr() {
		return ipAdEntAddr;
	}
	public void setIpAdEntAddr(String ipAdEntAddr) {
		this.ipAdEntAddr = ipAdEntAddr;
	}
	public long getIpAdEntIfIndex() {
		return ipAdEntIfIndex;
	}
	public void setIpAdEntIfIndex(long ipAdEntIfIndex) {
		this.ipAdEntIfIndex = ipAdEntIfIndex;
	}
	public String getIpAdEntNetMask() {
		return ipAdEntNetMask;
	}
	public void setIpAdEntNetMask(String ipAdEntNetMask) {
		this.ipAdEntNetMask = ipAdEntNetMask;
	}
	public String getIpAdEntBcastAddr() {
		return ipAdEntBcastAddr;
	}
	public void setIpAdEntBcastAddr(String ipAdEntBcastAddr) {
		this.ipAdEntBcastAddr = ipAdEntBcastAddr;
	}
	public int getIpAdEntReasmMaxSize() {
		return ipAdEntReasmMaxSize;
	}
	public void setIpAdEntReasmMaxSize(int ipAdEntReasmMaxSize) {
		this.ipAdEntReasmMaxSize = ipAdEntReasmMaxSize;
	}
	
	
}
