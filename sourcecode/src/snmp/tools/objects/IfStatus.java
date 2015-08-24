package snmp.tools.objects;

public class IfStatus {
	private long ifIndex;
	private int ifAdminStatus;
	private int ifOperStatus;
	private String ifLastChange;
	
	public long getIfIndex() {
		return ifIndex;
	}
	public void setIfIndex(long ifIndex) {
		this.ifIndex = ifIndex;
	}
	public int getIfAdminStatus() {
		return ifAdminStatus;
	}
	public void setIfAdminStatus(int ifAdminStatus) {
		this.ifAdminStatus = ifAdminStatus;
	}
	public int getIfOperStatus() {
		return ifOperStatus;
	}
	public void setIfOperStatus(int ifOperStatus) {
		this.ifOperStatus = ifOperStatus;
	}
	public String getIfLastChange() {
		return ifLastChange;
	}
	public void setIfLastChange(String ifLastChange) {
		this.ifLastChange = ifLastChange;
	}
}
