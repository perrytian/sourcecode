package snmp.tools.objects;

import java.io.Serializable;

public class PortPerformance implements Serializable{
	
	private long neID;
	private long phyLinkID;
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
	private long phLinkWidth;
	
	private double inRate;//流入流速
	private double outRate;//流出流速
	private double inErrorRate;//流入误码率
	private double outErrorRate;//流出误码率
	private double inLossRate;//流出丢包率
	private double outLossRate;//流出丢包率
	
	private long now;
	
	public long getNeID() {
		return neID;
	}
	public void setNeID(long neID) {
		this.neID = neID;
	}
	public long getPhyLinkID() {
		return phyLinkID;
	}
	public void setPhyLinkID(long phyLinkID) {
		this.phyLinkID = phyLinkID;
	}
	
	public long getIfIndex() {
		return ifIndex;
	}
	public void setIfIndex(long ifIndex) {
		this.ifIndex = ifIndex;
	}
	public long getIfInOctets() {
		return ifInOctets;
	}
	public void setIfInOctets(long ifInOctets) {
		this.ifInOctets = ifInOctets;
	}
	public long getIfInUcastPkts() {
		return ifInUcastPkts;
	}
	public void setIfInUcastPkts(long ifInUcastPkts) {
		this.ifInUcastPkts = ifInUcastPkts;
	}
	public long getIfInNUcastPkts() {
		return ifInNUcastPkts;
	}
	public void setIfInNUcastPkts(long ifInNUcastPkts) {
		this.ifInNUcastPkts = ifInNUcastPkts;
	}
	public long getIfInDiscards() {
		return ifInDiscards;
	}
	public void setIfInDiscards(long ifInDiscards) {
		this.ifInDiscards = ifInDiscards;
	}
	public long getIfInErrors() {
		return ifInErrors;
	}
	public void setIfInErrors(long ifInErrors) {
		this.ifInErrors = ifInErrors;
	}
	public long getIfInUnknownProtos() {
		return ifInUnknownProtos;
	}
	public void setIfInUnknownProtos(long ifInUnknownProtos) {
		this.ifInUnknownProtos = ifInUnknownProtos;
	}
	public long getIfOutOctets() {
		return ifOutOctets;
	}
	public void setIfOutOctets(long ifOutOctets) {
		this.ifOutOctets = ifOutOctets;
	}
	public long getIfOutUcastPkts() {
		return ifOutUcastPkts;
	}
	public void setIfOutUcastPkts(long ifOutUcastPkts) {
		this.ifOutUcastPkts = ifOutUcastPkts;
	}
	public long getIfOutNUcastPkts() {
		return ifOutNUcastPkts;
	}
	public void setIfOutNUcastPkts(long ifOutNUcastPkts) {
		this.ifOutNUcastPkts = ifOutNUcastPkts;
	}
	public long getIfOutDiscards() {
		return ifOutDiscards;
	}
	public void setIfOutDiscards(long ifOutDiscards) {
		this.ifOutDiscards = ifOutDiscards;
	}
	public long getIfOutErrors() {
		return ifOutErrors;
	}
	public void setIfOutErrors(long ifOutErrors) {
		this.ifOutErrors = ifOutErrors;
	}
	public long getIfOutQLen() {
		return ifOutQLen;
	}
	public void setIfOutQLen(long ifOutQLen) {
		this.ifOutQLen = ifOutQLen;
	}
	public long getPhLinkWidth() {
		return phLinkWidth;
	}
	public void setPhLinkWidth(long phLinkWidth) {
		this.phLinkWidth = phLinkWidth;
	}
	public long getNow() {
		return now;
	}
	public void setNow(long now) {
		this.now = now;
	}
	
	public double getInRate() {
		return inRate;
	}
	public void setInRate(double inRate) {
		this.inRate = inRate;
	}
	public double getOutRate() {
		return outRate;
	}
	public void setOutRate(double outRate) {
		this.outRate = outRate;
	}
	public double getInErrorRate() {
		return inErrorRate;
	}
	public void setInErrorRate(double inErrorRate) {
		this.inErrorRate = inErrorRate;
	}
	public double getOutErrorRate() {
		return outErrorRate;
	}
	public void setOutErrorRate(double outErrorRate) {
		this.outErrorRate = outErrorRate;
	}
	public double getInLossRate() {
		return inLossRate;
	}
	public void setInLossRate(double inLossRate) {
		this.inLossRate = inLossRate;
	}
	public double getOutLossRate() {
		return outLossRate;
	}
	public void setOutLossRate(double outLossRate) {
		this.outLossRate = outLossRate;
	}
	
}
