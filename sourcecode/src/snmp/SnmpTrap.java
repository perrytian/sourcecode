package snmp;

import java.io.Serializable;
import java.util.Date;

public class SnmpTrap  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int GENERIC_COLDSTART = 0;//代理进行了初始化
	public static final int GENERIC_WARMSTART = 1;//代理进行了重新初始化
	public static final int GENERIC_LINKDOWN = 2;//一个接口从工作状态变为故障状态
	public static final int GENERIC_LINKUP = 3;//一个接口从故障状态变为工作状态
	public static final int GENERIC_AUTHENTICATIONFAILURE = 4;//从SNMP管理进程接收到具有一个无效Community的报文
	public static final int GENERIC_EGPNEIGHBORLOSS = 5;//一个EGP相邻路由器变为故障状态
	public static final int GENERIC_ENTERPRISESPECIFIC = 6; //代理自定义的事件，需要用后面的特定代码来指明
	
	public static final int SOURCETYPE_ME = 0;
	public static final int SOURCETYPE_PORT =1;
	
	private String oriText;
	private String title;
	private String sourceIP;
	private int genericTrap;
	private long ifIndex;
	private int sourceType = SOURCETYPE_ME;
	private String trapOID;
	private Date startTime;
	
	private boolean isClearAlarm;
	
	public boolean isClearAlarm() {
		return isClearAlarm;
	}
	public void setClearAlarm(boolean isClearAlarm) {
		this.isClearAlarm = isClearAlarm;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getTrapOID() {
		return trapOID;
	}
	public void setTrapOID(String trapOID) {
		this.trapOID = trapOID;
	}
	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}
	public long getIfIndex() {
		return ifIndex;
	}
	public void setIfIndex(long ifIndex) {
		this.ifIndex = ifIndex;
	}
	public int getGenericTrap() {
		return genericTrap;
	}
	public void setGenericTrap(int genericTrap) {
		this.genericTrap = genericTrap;
	}
	public String getOriText() {
		return oriText;
	}
	public void setOriText(String oriText) {
		this.oriText = oriText;
	}
	public String getSourceIP() {
		return sourceIP;
	}
	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}
	
}
