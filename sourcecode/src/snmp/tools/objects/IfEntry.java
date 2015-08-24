package snmp.tools.objects;

public class IfEntry {
	private long ifIndex;
	private String ifDescr;
	private int ifType;
	private long ifMtu;
	private long ifSpeed;
	private String ifPhysAddress;
	
	public long getIfIndex() {
		return ifIndex;
	}
	public void setIfIndex(long ifIndex) {
		this.ifIndex = ifIndex;
	}
	public String getIfDescr() {
		return ifDescr;
	}
	public void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}
	public int getIfType() {
		return ifType;
	}
	public void setIfType(int ifType) {
		this.ifType = ifType;
	}
	public long getIfMtu() {
		return ifMtu;
	}
	public void setIfMtu(long ifMtu) {
		this.ifMtu = ifMtu;
	}
	public long getIfSpeed() {
		return ifSpeed;
	}
	public void setIfSpeed(long ifSpeed) {
		this.ifSpeed = ifSpeed;
	}
	public String getIfPhysAddress() {
		return ifPhysAddress;
	}
	public void setIfPhysAddress(String ifPhysAddress) {
		this.ifPhysAddress = ifPhysAddress;
	}
}
