package snmp.tools.objects;

import java.io.Serializable;

/**
 * IP配置信息
 * @author oss
 *
 */
public class IPConfig implements Serializable{
	
	private String ipAdEntAddr;
	
	private String ipAdEntIfIndex;
	
	private String ipAdEntNetMask;

	public String getIpAdEntAddr() {
		return ipAdEntAddr;
	}

	public void setIpAdEntAddr(String ipAdEntAddr) {
		this.ipAdEntAddr = ipAdEntAddr;
	}

	public String getIpAdEntIfIndex() {
		return ipAdEntIfIndex;
	}

	public void setIpAdEntIfIndex(String ipAdEntIfIndex) {
		this.ipAdEntIfIndex = ipAdEntIfIndex;
	}

	public String getIpAdEntNetMask() {
		return ipAdEntNetMask;
	}

	public void setIpAdEntNetMask(String ipAdEntNetMask) {
		this.ipAdEntNetMask = ipAdEntNetMask;
	}
	

}
