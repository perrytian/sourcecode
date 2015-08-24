package snmp.tools.objects;

public class IfFlow {
	private long ifIndex;
	private long ifInOctets;
	private long ifInUcastPkts;
	private long ifInNUcastPkts;
	private long ifInDiscards;
	private long ifInErrors;
	private long ifInUnknownProtos;
	private long ifOutOctets;
	private long ifOutUcastPkts;
	private long ifOutNUcastPkts;
	private long ifOutDiscards;
	private long ifOutErrors;
	private long ifOutQLen;
	private long now;
	public void setIfIndex(long ifIndex) {
		this.ifIndex = ifIndex;
	}
	public long getIfIndex() {
		return ifIndex;
	}
	public void setIfInOctets(long ifInOctets) {
		this.ifInOctets = ifInOctets;
	}
	public long getIfInOctets() {
		return ifInOctets;
	}
	public void setIfInUcastPkts(long ifInUcastPkts) {
		this.ifInUcastPkts = ifInUcastPkts;
	}
	public long getIfInUcastPkts() {
		return ifInUcastPkts;
	}
	public void setIfInNUcastPkts(long ifInNUcastPkts) {
		this.ifInNUcastPkts = ifInNUcastPkts;
	}
	public long getIfInNUcastPkts() {
		return ifInNUcastPkts;
	}
	public void setIfInDiscards(long ifInDiscards) {
		this.ifInDiscards = ifInDiscards;
	}
	public long getIfInDiscards() {
		return ifInDiscards;
	}
	public void setIfInErrors(long ifInErrors) {
		this.ifInErrors = ifInErrors;
	}
	public long getIfInErrors() {
		return ifInErrors;
	}
	public void setIfInUnknownProtos(long ifInUnknownProtos) {
		this.ifInUnknownProtos = ifInUnknownProtos;
	}
	public long getIfInUnknownProtos() {
		return ifInUnknownProtos;
	}
	public void setIfOutOctets(long ifOutOctets) {
		this.ifOutOctets = ifOutOctets;
	}
	public long getIfOutOctets() {
		return ifOutOctets;
	}
	public void setIfOutUcastPkts(long ifOutUcastPkts) {
		this.ifOutUcastPkts = ifOutUcastPkts;
	}
	public long getIfOutUcastPkts() {
		return ifOutUcastPkts;
	}
	public void setIfOutNUcastPkts(long ifOutNUcastPkts) {
		this.ifOutNUcastPkts = ifOutNUcastPkts;
	}
	public long getIfOutNUcastPkts() {
		return ifOutNUcastPkts;
	}
	public void setIfOutDiscards(long ifOutDiscards) {
		this.ifOutDiscards = ifOutDiscards;
	}
	public long getIfOutDiscards() {
		return ifOutDiscards;
	}
	public void setIfOutErrors(long ifOutErrors) {
		this.ifOutErrors = ifOutErrors;
	}
	public long getIfOutErrors() {
		return ifOutErrors;
	}
	public void setIfOutQLen(long ifOutQLen) {
		this.ifOutQLen = ifOutQLen;
	}
	public long getIfOutQLen() {
		return ifOutQLen;
	}
	public void setNow(long now) {
		this.now = now;
	}
	public long getNow() {
		return now;
	}
}
