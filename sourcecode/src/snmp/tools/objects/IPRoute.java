package snmp.tools.objects;

public class IPRoute {
	
	public static final int ROUTETYPE_OTHER = 1;
	public static final int ROUTETYPE_INVALID = 2;
	public static final int ROUTETYPE_DIRECT = 3;
	public static final int ROUTETYPE_INDIRECT = 4;
	
	public static final int ROUTEPROTO_OTHER = 1;
	public static final int ROUTEPROTO_LOCAL = 2;
	public static final int ROUTEPROTO_NETMGMT = 3;
	public static final int ROUTEPROTO_ICMP = 4;
	public static final int ROUTEPROTO_EGP = 5;
	public static final int ROUTEPROTO_GGP = 6;
	public static final int ROUTEPROTO_HELLO = 7;
	public static final int ROUTEPROTO_RIP = 8;
	public static final int ROUTEPROTO_ISIS = 9;
	public static final int ROUTEPROTO_CISCOIGRP = 11;
	public static final int ROUTEPROTO_BBNSPIGP = 12;
	public static final int ROUTEPROTO_OSPF = 13;
	public static final int ROUTEPROTO_BGP = 14;
	
	private String ipRouteDest;
	private long ipRouteIfIndex;
	private String ipRouteNextHop;
	private int ipRouteType;//{other(1),invalid(2),direct(3),indirect(4)}
	private int ipRouteProto;//{other(1),local(2),netmgmt(3),icmp(4),egp(5),ggp(6),hello(7),rip(8),is-is(9),ciscoIgrp(11),bbnSpfIgp(12),ospf(13),bgp(14)}
	private int ipRouteAge;
	private String ipRouteMask;
	public String getIpRouteDest() {
		return ipRouteDest;
	}
	public void setIpRouteDest(String ipRouteDest) {
		this.ipRouteDest = ipRouteDest;
	}
	public long getIpRouteIfIndex() {
		return ipRouteIfIndex;
	}
	public void setIpRouteIfIndex(long ipRouteIfIndex) {
		this.ipRouteIfIndex = ipRouteIfIndex;
	}
	public String getIpRouteNextHop() {
		return ipRouteNextHop;
	}
	public void setIpRouteNextHop(String ipRouteNextHop) {
		this.ipRouteNextHop = ipRouteNextHop;
	}
	public int getIpRouteType() {
		return ipRouteType;
	}
	public void setIpRouteType(int ipRouteType) {
		this.ipRouteType = ipRouteType;
	}
	public int getIpRouteProto() {
		return ipRouteProto;
	}
	public void setIpRouteProto(int ipRouteProto) {
		this.ipRouteProto = ipRouteProto;
	}
	public int getIpRouteAge() {
		return ipRouteAge;
	}
	public void setIpRouteAge(int ipRouteAge) {
		this.ipRouteAge = ipRouteAge;
	}
	public String getIpRouteMask() {
		return ipRouteMask;
	}
	public void setIpRouteMask(String ipRouteMask) {
		this.ipRouteMask = ipRouteMask;
	}
	
}
