package snmp.tools.objects;

import java.io.Serializable;

/**
 * 路由表表项
 * @author oss
 *
 */
public class IPRouteTableItem implements Serializable{
	
	private String ipRouteDest;
	
	private String ipRouteIfIndex;
	
	private String ipRouteNextHop;
	
	private String ipRouteType;
	
	private String ipRouteProto;
	
	private String ipRouteAge;
	
	private String ipRouteMask;
	
	private String ipRouteInfo;

	public String getIpRouteDest() {
		return ipRouteDest;
	}

	public void setIpRouteDest(String ipRouteDest) {
		this.ipRouteDest = ipRouteDest;
	}

	public String getIpRouteIfIndex() {
		return ipRouteIfIndex;
	}

	public void setIpRouteIfIndex(String ipRouteIfIndex) {
		this.ipRouteIfIndex = ipRouteIfIndex;
	}

	public String getIpRouteNextHop() {
		return ipRouteNextHop;
	}

	public void setIpRouteNextHop(String ipRouteNextHop) {
		this.ipRouteNextHop = ipRouteNextHop;
	}

	public String getIpRouteType() {
		return ipRouteType;
	}

	public void setIpRouteType(String ipRouteType) {
		this.ipRouteType = ipRouteType;
	}

	public String getIpRouteProto() {
		return ipRouteProto;
	}

	public void setIpRouteProto(String ipRouteProto) {
		this.ipRouteProto = ipRouteProto;
	}

	public String getIpRouteAge() {
		return ipRouteAge;
	}

	public void setIpRouteAge(String ipRouteAge) {
		this.ipRouteAge = ipRouteAge;
	}

	public String getIpRouteMask() {
		return ipRouteMask;
	}

	public void setIpRouteMask(String ipRouteMask) {
		this.ipRouteMask = ipRouteMask;
	}

	public String getIpRouteInfo() {
		return ipRouteInfo;
	}

	public void setIpRouteInfo(String ipRouteInfo) {
		this.ipRouteInfo = ipRouteInfo;
	}
	
}

