package snmp.tools.objects;

public class SnmpValue {

	private String oid;
	private String value;
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getOid() {
		return oid;
	}
}
