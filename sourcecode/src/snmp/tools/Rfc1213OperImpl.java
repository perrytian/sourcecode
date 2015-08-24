package snmp.tools;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.snmp4j.mp.SnmpConstants;

import snmp.tools.objects.IPAddress;
import snmp.tools.objects.IPRoute;
import snmp.tools.objects.IfEntry;
import snmp.tools.objects.IfFlow;
import snmp.tools.objects.IfStatus;
import snmp.tools.objects.SnmpTable;

public class Rfc1213OperImpl extends SnmpV1OperImpl implements Rfc1213Oper{

	private static Logger logger = Logger.getLogger(Rfc1213OperImpl.class);
			
	
	private final static String sysDescr = "1.3.6.1.2.1.1.1.0";
	private final static String sysObjectID = "1.3.6.1.2.1.1.2.0";
	private final static String sysUpTime = "1.3.6.1.2.1.1.3.0";
	private final static String sysContact = "1.3.6.1.2.1.1.4.0";
	private final static String sysName = "1.3.6.1.2.1.1.5.0";
	private final static String sysLocation = "1.3.6.1.2.1.1.6.0";
	private final static String sysServices = "1.3.6.1.2.1.1.7.0";
	private final static String ifNumber = "1.3.6.1.2.1.2.1";
	private final static String ifIndex = "1.3.6.1.2.1.2.2.1.1";
	private final static String ifDescr = "1.3.6.1.2.1.2.2.1.2";
	private final static String ifType = "1.3.6.1.2.1.2.2.1.3";
	private final static String ifMtu = "1.3.6.1.2.1.2.2.1.4";
	private final static String ifSpeed = "1.3.6.1.2.1.2.2.1.5";
	private final static String ifPhysAddress = "1.3.6.1.2.1.2.2.1.6";
	private final static String ifAdminStatus = "1.3.6.1.2.1.2.2.1.7";
	private final static String ifOperStatus = "1.3.6.1.2.1.2.2.1.8";
	private final static String ifLastChange = "1.3.6.1.2.1.2.2.1.9";
	private final static String ifInOctets = "1.3.6.1.2.1.2.2.1.10";
	private final static String ifInUcastPkts = "1.3.6.1.2.1.2.2.1.11";
	private final static String ifInNUcastPkts = "1.3.6.1.2.1.2.2.1.12";
	private final static String ifInDiscards = "1.3.6.1.2.1.2.2.1.13";
	private final static String ifInErrors = "1.3.6.1.2.1.2.2.1.14";
	private final static String ifInUnknownProtos = "1.3.6.1.2.1.2.2.1.15";
	
	private final static String ifOutOctets = "1.3.6.1.2.1.2.2.1.16";
	
	private final static String ifOutUcastPkts = "1.3.6.1.2.1.2.2.1.17";
	private final static String ifOutNUcastPkts = "1.3.6.1.2.1.2.2.1.18";
	private final static String ifOutDiscards = "1.3.6.1.2.1.2.2.1.19";
	private final static String ifOutErrors = "1.3.6.1.2.1.2.2.1.20";
	private final static String ifOutQLen = "1.3.6.1.2.1.2.2.1.21";
	private final static String ifSpecific = "1.3.6.1.2.1.2.2.1.22";
	
	private final static String ifHCOutOctets = "1.3.6.1.2.1.31.1.1.1.10";//mod
	private final static String ifHCInOctets = "1.3.6.1.2.1.31.1.1.1.6";//mod
//	private final static String ifHCInUcastPkts = "1.3.6.1.2.1.31.1.1.1.7";//mod
	
	//IP Address Table
	private final static String ipAdEntAddr = "1.3.6.1.2.1.4.20.1.1";
	private final static String ipAdEntIfIndex = "1.3.6.1.2.1.4.20.1.2";
	private final static String ipAdEntNetMask = "1.3.6.1.2.1.4.20.1.3";
	private final static String ipAdEntBcastAddr = "1.3.6.1.2.1.4.20.1.4";
	private final static String ipAdEntReasmMaxSize = "1.3.6.1.2.1.4.20.1.5";
	//IP Route Table
	private final static String ipRouteDest = "1.3.6.1.2.1.4.21.1.1";
	private final static String ipRouteIfIndex = "1.3.6.1.2.1.4.21.1.2";
	private final static String ipRouteNextHop = "1.3.6.1.2.1.4.21.1.7";
	private final static String ipRouteType = "1.3.6.1.2.1.4.21.1.8";
	private final static String ipRouteProto = "1.3.6.1.2.1.4.21.1.9";
	private final static String ipRouteAge = "1.3.6.1.2.1.4.21.1.10";
	private final static String ipRouteMask = "1.3.6.1.2.1.4.21.1.11";
	
	
	
	public Rfc1213OperImpl(String ip, String readCommunity,String writeCommunity, int retries,long timeout){
		this(ip, readCommunity, writeCommunity, SnmpConstants.version2c,retries,timeout);//默认使用SNMP V2C
	}
	public Rfc1213OperImpl(String ip, String readCommunity,String writeCommunity,int version,int retries,long timeout){
		super(ip, readCommunity, writeCommunity, retries,timeout);
	}
	

	/**
	 * Des:sysDescr 系统描述
	 * OID:1.3.6.1.2.1.1.1.0
	 * @return
	 */
	public String getSysDescr()throws IOException{
		return snmpGet(sysDescr);
	}
	
	/**
	 * Des:sysObjectID  
	 * OID:1.3.6.1.2.1.1.2.0
	 * @return
	 */
	public String getSysObjectID()throws IOException{
		return snmpGet(sysObjectID);
	}
	
	/**
	 * Des:sysUpTime
	 * OID:1.3.6.1.2.1.1.3.0
	 * @return
	 */
	public String getSysUpTime()throws IOException{
		return snmpGet(sysUpTime);
	}
	
	/**
	 * Des:sysContact
	 * OID:1.3.6.1.2.1.1.4.0
	 * @return
	 */
	public String getSysContact()throws IOException{
		return snmpGet(sysContact);
	}
	
	/**
	 * Des:sysName 系统名称
	 * OID:1.3.6.1.2.1.1.5.0
	 * @return
	 */
	public String getSysName()throws IOException{
		return snmpGet(sysName);
	}
	
	/**
	 * Des:sysLocation
	 * OID:1.3.6.1.2.1.1.6.0
	 * @return
	 */
	public String getSysLocation()throws IOException{
		return snmpGet(sysLocation);
	}
	
	/**
	 * Des:sysServices 
	 * OID:1.3.6.1.2.1.1.7.0
	 * @return
	 */
	public String getSysServices()throws IOException{
		return snmpGet(sysServices);
	}
	
	/**
	 * 取MIB中system的所有信息 
	 * @return
	 */
	public String[] getSystem()throws IOException{
		String[] ret = new String[7];
		ret[0] = getSysDescr();
		ret[1] = getSysObjectID();
		ret[2] = getSysUpTime();
		ret[3] = getSysContact();
		ret[4] = getSysName();
		ret[5] = getSysLocation();
		ret[6] = getSysServices();
	
		return ret;
	}
	
	/**
	 * Des:ifNumber  端口数目
	 * OID:1.3.6.1.2.1.2.1
	 * @return
	 */
	public int getIfNumber()throws IOException{
		return Integer.parseInt(snmpGet(ifNumber));
	}
	
	/**
	 * Des:ifIndex  端口索引
	 * OID:1.3.6.1.2.1.2.2.1.1
	 * @return 返回端口索引的列表
	 */
	public long[] getIfIndex()throws IOException{
		String[] oids = {ifIndex};
		String[] strIndexs = snmpGetTable(oids).getIndexs();
		long[] indexs = new long[strIndexs.length];
		for(int i=0;i<indexs.length;i++){
			indexs[i] = Long.parseLong(strIndexs[i]);
		}
		return indexs;
	}
	
	/**
	 * 获取端口静态信息
	 * OID:1.3.6.1.2.1.2.2
	 * @return
	 */
	public IfEntry[] getIfEntriesTable()throws IOException{
		String[] oids = {ifDescr,
				ifType,
				ifMtu,
				ifSpeed,
				ifPhysAddress};
		SnmpTable snmpTable = snmpGetTable(oids);
		
		String[] indexs = snmpTable.getIndexs();
		
//		Logger.out(Logger.DEBUG, "===========采集网元IP["+getIPAddress()+"] 端口索引数["+indexs.length+"]===========");
		
		IfEntry[] ret = new IfEntry[indexs.length];
		
		for(int i=0;i<indexs.length;i++){
			IfEntry ifEntry = new IfEntry();
			ifEntry.setIfIndex(Integer.parseInt(indexs[i]));
			ifEntry.setIfDescr(snmpTable.getValue(indexs[i], ifDescr));
			
			try{
				ifEntry.setIfType(Integer.parseInt(snmpTable.getValue(indexs[i], ifType)));
			}catch (NumberFormatException e) {
				ifEntry.setIfType(0);
				logger.warn("通过SNMP获取IP["+getIPAddress()+"] IfEntriesTable出错:ifType["+snmpTable.getValue(indexs[i], ifType)+"]");
			}
			try{
				ifEntry.setIfMtu(Long.parseLong(snmpTable.getValue(indexs[i], ifMtu)));
			}catch (NumberFormatException e) {
				ifEntry.setIfMtu(0);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]IfEntriesTable出错:ifMtu["+snmpTable.getValue(indexs[i], ifMtu)+"]");
			}
			try{
				ifEntry.setIfSpeed(Long.parseLong(snmpTable.getValue(indexs[i], ifSpeed)));
			}catch (NumberFormatException e) {
				ifEntry.setIfSpeed(0);
				logger.warn("通过SNMP获取IP["+getIPAddress()+"]IfEntriesTable出错:ifSpeed["+snmpTable.getValue(indexs[i], ifSpeed)+"]");
			}
			
			ifEntry.setIfPhysAddress(snmpTable.getValue(indexs[i], ifPhysAddress));
			
			ret[i] = ifEntry;
		}
		
		return ret;
	}
	
	/**
	 * 返回ifIndex为索引的端口表某一行的所有数据
	 * @return 
	 */
	public IfEntry getIfEntry(long ifIndex)throws IOException{
		String[] oids = {ifDescr+"."+ifIndex,
				ifType+"."+ifIndex,
				ifMtu+"."+ifIndex,
				ifSpeed+"."+ifIndex,
				ifPhysAddress+"."+ifIndex};
		String[] values = snmpGet(oids);
		IfEntry ifEntry = new IfEntry();
		ifEntry.setIfIndex(ifIndex);
		ifEntry.setIfDescr(values[0]);
		try {
			if(values[1]!= null){
				ifEntry.setIfType(Integer.parseInt(values[1]));
			}else{
				ifEntry.setIfType(-1);
			}
		} catch (NumberFormatException e) {
			ifEntry.setIfType(-1);
			logger.warn("通过SNMP获取IP["+getIPAddress()+"]IfEntry出错: ifIndex["+ifIndex+"]ifSpeed["+values[1]+"]");
		}
		try {
			if(values[2] != null){
				ifEntry.setIfMtu(Long.parseLong(values[2]));
			}else{
				ifEntry.setIfMtu(-1);
			}
			
		} catch (NumberFormatException e) {
			ifEntry.setIfMtu(-1);
			logger.warn("通过SNMP获取IP["+getIPAddress()+"]IfEntry出错: ifIndex["+ifIndex+"]ifMtu["+values[2]+"]");
		}	
		try {
			if(values[3] != null){
				ifEntry.setIfSpeed(Long.parseLong(values[3]));
			}else{
				ifEntry.setIfSpeed(-1);
			}
			
		} catch (NumberFormatException e) {
			ifEntry.setIfSpeed(-1);
			logger.warn("通过SNMP获取IP["+getIPAddress()+"]IfEntry出错: ifIndex["+ifIndex+"]ifSpeed["+values[3]+"]");
		}
		
		ifEntry.setIfPhysAddress(values[4]);
		
		return ifEntry;
	}
	
	public IfStatus[] getIfStatusTable()throws IOException{
		String[] oids = {ifAdminStatus,
				ifOperStatus,
				ifLastChange};
		SnmpTable snmpTable = snmpGetTable(oids);
		
		String[] indexs = snmpTable.getIndexs();
		
		IfStatus[] ret = new IfStatus[indexs.length];
		
		for(int i=0;i<indexs.length;i++){
			IfStatus ifStatus = new IfStatus();
			ifStatus.setIfIndex(Integer.parseInt(indexs[i]));
			ifStatus.setIfAdminStatus(Integer.parseInt(snmpTable.getValue(indexs[i], ifAdminStatus)));
			ifStatus.setIfOperStatus(Integer.parseInt(snmpTable.getValue(indexs[i], ifOperStatus)));
			ifStatus.setIfLastChange(snmpTable.getValue(indexs[i], ifLastChange));
			ret[i] = ifStatus;
		}
		
		return ret;
	}
	
	public IfStatus getIfStatus(long ifIndex)throws IOException{
		String[] oids = {ifAdminStatus+"."+ifIndex,
				ifOperStatus+"."+ifIndex,
				ifLastChange+"."+ifIndex};
		String[] values = null;
		try{
			values = snmpGet(oids);
		}catch(IOException ex){
			throw new IOException(ex.getLocalizedMessage());
		}
		
		if(values == null){
			return null;
		}
		
		IfStatus ifStatus = new IfStatus();
		ifStatus.setIfIndex(ifIndex);
		if(values[0] != null && !values[0].equals("")){
			ifStatus.setIfAdminStatus(Integer.parseInt(values[0]));
		}
		if(values[1] != null && !values[1].equals("")){
			ifStatus.setIfOperStatus(Integer.parseInt(values[1]));
		}
		if(values[2] != null && !values[2].equals("")){
			ifStatus.setIfLastChange(values[2]);
		}
		return ifStatus;
	}
	
	public IfFlow[] getIfFlowTable()throws IOException{
		
		String[] oids = {ifInOctets,
				ifInUcastPkts,
				ifInNUcastPkts,
				ifInDiscards,
				ifInErrors,
				ifInUnknownProtos,
				ifOutOctets,
				ifOutUcastPkts,
				ifOutNUcastPkts,
				ifOutDiscards,
				ifOutErrors,
				ifOutQLen};
		SnmpTable snmpTable = snmpGetTable(oids);
		
		String[] indexs = snmpTable.getIndexs();
		
		IfFlow[] ret = new IfFlow[indexs.length];
		long now = System.currentTimeMillis();
		for(int i=0;i<indexs.length;i++){
			IfFlow ifFlow = new IfFlow();
			ifFlow.setIfIndex(Long.parseLong(indexs[i]));
			try{
				ifFlow.setIfInOctets(Long.parseLong(snmpTable.getValue(indexs[i], ifInOctets)));
			}catch (NumberFormatException e) {
				ifFlow.setIfInOctets(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifInOctets["+snmpTable.getValue(indexs[i], ifInOctets)+"]");
			}
			try{
				
				ifFlow.setIfInUcastPkts(Long.parseLong(snmpTable.getValue(indexs[i], ifInUcastPkts)));
			}catch (NumberFormatException e) {
				ifFlow.setIfInUcastPkts(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"]ifInUcastPkts["+snmpTable.getValue(indexs[i], ifInUcastPkts)+"]");
			}
			try{
				ifFlow.setIfInNUcastPkts(Long.parseLong(snmpTable.getValue(indexs[i], ifInNUcastPkts)));
			}catch (NumberFormatException e) {
				ifFlow.setIfInNUcastPkts(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"]ifInNUcastPkts["+snmpTable.getValue(indexs[i], ifInNUcastPkts)+"]");
			}
			try{
				ifFlow.setIfInDiscards(Long.parseLong(snmpTable.getValue(indexs[i], ifInDiscards)));
			}catch (NumberFormatException e) {
				ifFlow.setIfInDiscards(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifInDiscards["+snmpTable.getValue(indexs[i], ifInDiscards)+"]");
			}
			try{
				ifFlow.setIfInErrors(Long.parseLong(snmpTable.getValue(indexs[i], ifInErrors)));
			}catch (NumberFormatException e) {
				ifFlow.setIfInErrors(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifInErrors["+snmpTable.getValue(indexs[i], ifInErrors)+"]");
			}
			try{
				ifFlow.setIfInUnknownProtos(Long.parseLong(snmpTable.getValue(indexs[i], ifInUnknownProtos)));
			}catch (NumberFormatException e) {
				ifFlow.setIfInUnknownProtos(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifInUnknownProtos["+snmpTable.getValue(indexs[i], ifInUnknownProtos)+"]");
			}
			try{
				ifFlow.setIfOutOctets(Long.parseLong(snmpTable.getValue(indexs[i], ifOutOctets)));
			}catch (NumberFormatException e) {
				ifFlow.setIfOutOctets(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifOutOctets["+snmpTable.getValue(indexs[i], ifOutOctets)+"]");
			}
			try{
				ifFlow.setIfOutUcastPkts(Long.parseLong(snmpTable.getValue(indexs[i], ifOutUcastPkts)));
			}catch (NumberFormatException e) {
				ifFlow.setIfOutUcastPkts(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifOutUcastPkts["+snmpTable.getValue(indexs[i], ifOutUcastPkts)+"]");
			}
			try{
				ifFlow.setIfOutNUcastPkts(Long.parseLong(snmpTable.getValue(indexs[i], ifOutNUcastPkts)));
			}catch (NumberFormatException e) {
				ifFlow.setIfOutNUcastPkts(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifOutNUcastPkts["+snmpTable.getValue(indexs[i], ifOutNUcastPkts)+"]");
			}
			try{
				ifFlow.setIfOutDiscards(Long.parseLong(snmpTable.getValue(indexs[i], ifOutDiscards)));
			}catch (NumberFormatException e) {
				ifFlow.setIfOutDiscards(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifOutDiscards["+snmpTable.getValue(indexs[i], ifOutDiscards)+"]");
			}
			try{
				ifFlow.setIfOutErrors(Long.parseLong(snmpTable.getValue(indexs[i], ifOutErrors)));
			}catch (NumberFormatException e) {
				ifFlow.setIfOutErrors(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifOutErrors["+snmpTable.getValue(indexs[i], ifOutErrors)+"]");
			}
			try{
				ifFlow.setIfOutQLen(Long.parseLong(snmpTable.getValue(indexs[i], ifOutQLen)));
			}catch (NumberFormatException e) {
				ifFlow.setIfOutQLen(-1);
//				logger.warn("通过SNMP获取IP["+getIPAddress()+"]ifFlowTable出错:index["+indexs[i]+"] ifOutQLen["+snmpTable.getValue(indexs[i], ifOutQLen)+"]");
			}
			ifFlow.setNow(now);
			ret[i] = ifFlow;

		}
		return ret;
	}
	
	public IfFlow getIfFlow(long ifIndex)throws IOException{
		String[] oids = {ifInOctets+"."+ifIndex,
				ifInUcastPkts+"."+ifIndex,
				ifInNUcastPkts+"."+ifIndex,
				ifInDiscards+"."+ifIndex,
				ifInErrors+"."+ifIndex,
				ifInUnknownProtos+"."+ifIndex,
				ifOutOctets+"."+ifIndex,
				ifOutUcastPkts+"."+ifIndex,
				ifOutNUcastPkts+"."+ifIndex,
				ifOutDiscards+"."+ifIndex,
				ifOutErrors+"."+ifIndex,
				ifOutQLen+"."+ifIndex};
		String[] values = snmpGet(oids);
		
		IfFlow ifFlow = new IfFlow();
		
		try {
			if(values[0] != null){
				ifFlow.setIfInOctets(Long.parseLong(values[0]));
			}else{
				ifFlow.setIfInOctets(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfInOctets(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfInOctets["+values[0]+"] OID["+oids[0]+"]");
		}
		try{
			if(values[1] != null){
				ifFlow.setIfInUcastPkts(Long.parseLong(values[1]));
			}else{
				ifFlow.setIfInUcastPkts(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfInUcastPkts(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfInUcastPkts["+values[1]+"] OID["+oids[1]+"]");
		}
		try{
			if(values[2] != null){
				ifFlow.setIfInNUcastPkts(Long.parseLong(values[2]));
			}else{
				ifFlow.setIfInNUcastPkts(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfInNUcastPkts(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfInNUcastPkts["+values[2]+"] OID["+oids[2]+"]");
		}
		try{
			if(values[3] != null){
				ifFlow.setIfInDiscards(Long.parseLong(values[3]));
			}else{
				ifFlow.setIfInDiscards(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfInDiscards(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfInDiscards["+values[3]+"] OID["+oids[3]+"]");
		}
		try{
			if(values[4] != null){
				ifFlow.setIfInErrors(Long.parseLong(values[4]));
			}else{
				ifFlow.setIfInErrors(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfInErrors(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfInErrors["+values[4]+"] OID["+oids[4]+"]");
		}
		try{
			if(values[5] != null){
				ifFlow.setIfInUnknownProtos(Long.parseLong(values[5]));
			}else{
				ifFlow.setIfInUnknownProtos(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfInUnknownProtos(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfInUnknownProtos["+values[5]+"] OID["+oids[5]+"]");
		}
		try{
			if(values[6] != null){
				ifFlow.setIfOutOctets(Long.parseLong(values[6]));
			}else{
				ifFlow.setIfOutOctets(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfOutOctets(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfOutOctets["+values[6]+"] OID["+oids[6]+"]");
		}
		try{
			if(values[7] != null){
				ifFlow.setIfOutUcastPkts(Long.parseLong(values[7]));
			}else{
				ifFlow.setIfOutUcastPkts(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfOutUcastPkts(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfOutUcastPkts["+values[7]+"] OID["+oids[7]+"]");
		}
		try{
			if(values[8] != null){
				ifFlow.setIfOutNUcastPkts(Long.parseLong(values[8]));
			}else{
				ifFlow.setIfOutNUcastPkts(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfOutNUcastPkts(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfOutNUcastPkts["+values[8]+"] OID["+oids[8]+"]");
		}
		try{
			if(values[9] != null){
				ifFlow.setIfOutDiscards(Long.parseLong(values[9]));
			}else{
				ifFlow.setIfOutDiscards(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfOutDiscards(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfOutDiscards["+values[9]+"] OID["+oids[9]+"]");
		}
		try{
			if(values[10] != null){
				ifFlow.setIfOutErrors(Long.parseLong(values[10]));
			}else{
				ifFlow.setIfOutErrors(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfOutErrors(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfOutErrors["+values[10]+"] OID["+oids[10]+"]");
		}
		try{
			if(values[11] != null){
				ifFlow.setIfOutQLen(Long.parseLong(values[11]));
			}else{
				ifFlow.setIfOutQLen(-1);
			}
		} catch (NumberFormatException e) {
			ifFlow.setIfOutQLen(-1);
			logger.warn("获取IP["+getIPAddress()+"]ifFlow出错：ifIndex["+ifIndex+"] IfOutQLen["+values[11]+"] OID["+oids[11]+"]");
		}
		ifFlow.setNow(System.currentTimeMillis());
		return ifFlow;
	}
	
	/**
	 * 获取以ifIndex为索引的端口描述信息
	 * OID:1.3.6.1.2.1.2.2.1.2
	 * @return
	 */
	public String getIfDescr(long ifIndex)throws IOException{
		return snmpGet(ifDescr+"."+ifIndex);
	}
	
	/**
	 * 获取以ifIndex为索引的端口类型
	 * OID:1.3.6.1.2.1.2.2.1.3
	 * @return
	 */
	public int getIfType(long ifIndex)throws IOException{
		return Integer.parseInt(snmpGet(ifType+"."+ifIndex));
	}
	
	/**
	 * 获取以ifIndex为索引的端口MTU
	 * OID:1.3.6.1.2.1.2.2.1.4
	 * @return
	 */
	public long getIfMtu(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifMtu+"."+ifIndex));
	}
	
	/**
	 * 获取以ifIndex为索引的端口带宽
	 * OID:1.3.6.1.2.1.2.2.1.5
	 * @return
	 */
	public long getIfSpeed(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifSpeed+"."+ifIndex));
	}
	
	/**
	 * 获取以ifIndex为索引的端口物理地址
	 * OID:1.3.6.1.2.1.2.2.1.6
	 * @return
	 */
	public String getIfPhysAddress(long ifIndex)throws IOException{
		return snmpGet(ifPhysAddress+"."+ifIndex);
	}
	
	/**
	 * 获取以ifIndex为索引的端口管理状态
	 * OID:1.3.6.1.2.1.2.2.1.7
	 * @return
	 */
	public int getIfAdminStatus(long ifIndex)throws IOException{
		return Integer.parseInt(snmpGet(ifAdminStatus+"."+ifIndex));
	}
	
	/**
	 * 获取以ifIndex为索引的端口操作状态
	 * OID:1.3.6.1.2.1.2.2.1.8
	 * @return
	 */
	public int getIfOperStatus(long ifIndex)throws IOException{
		return Integer.parseInt(snmpGet(ifOperStatus+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.9
	 * @return
	 */
	public String getIfLastChange(long ifIndex)throws IOException{
		return snmpGet(ifLastChange+"."+ifIndex);
	}
	
	/**
	 * Des:端口流入流量
	 * OID:1.3.6.1.2.1.2.2.1.10
	 * @return
	 */
	public long getIfInOctets(long ifIndex)throws IOException{
		String oid = ifInOctets+"."+ifIndex;
		String ret = snmpGet(oid);
		if(isNumeric(ret)){
			return Long.parseLong(ret);
		}else if(ret.contains("noSuchInstance")){
			throw new IOException("noSuchInstance:OID["+oid+"]");
		}else{
			return -1;
		}
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.11
	 * @return
	 */
	public long getIfInUcastPkts(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifInUcastPkts+"."+ifIndex));
	}

	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.12
	 * @return
	 */
	public long getIfInNUcastPkts(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifInNUcastPkts+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.13
	 * @return
	 */
	public long getIfInDiscards(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifInDiscards+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.14
	 * @return
	 */
	public long getIfInErrors(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifInErrors+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.15
	 * @return
	 */
	public long getIfInUnknownProtos(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifInUnknownProtos+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.16
	 * @return
	 */
	public long getIfOutOctets(long ifIndex)throws IOException{
		String res = snmpGet(ifOutOctets+"."+ifIndex);
		if(res.equals("noSuchInstance")){
			return -1;
		}else{
			return Long.parseLong(snmpGet(ifOutOctets+"."+ifIndex));
		}
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.17
	 * @return
	 */
	public long getIfOutUcastPkts(long ifIndex)throws IOException{
		String res = snmpGet(ifOutUcastPkts+"."+ifIndex);
		if(res.equals("noSuchInstance")){
			return -1;
		}else{
			return Long.parseLong(snmpGet(ifOutUcastPkts+"."+ifIndex));
		}
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.18
	 * @return
	 */
	public long getIfOutNUcastPkts(long ifIndex)throws IOException{
		String res = snmpGet(ifOutNUcastPkts+"."+ifIndex);
		if(res.equals("noSuchInstance")){
			return -1;
		}else{
			return Long.parseLong(snmpGet(ifOutNUcastPkts+"."+ifIndex));
		}
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.19
	 * @return
	 */
	public long getIfOutDiscards(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifOutDiscards+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.20
	 * @return
	 */
	public long getIfOutErrors(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifOutErrors+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.21
	 * @return
	 */
	public long getIfOutQLen(long ifIndex)throws IOException{
		return Long.parseLong(snmpGet(ifOutQLen+"."+ifIndex));
	}
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.22
	 * @return
	 */
	public String getIfSpecific(long ifIndex)throws IOException{
		return snmpGet(ifSpecific+"."+ifIndex);
	}
	
	/**
	 * 端口的流入流速，计算得到 单位为bps
	 * @return
	 * @throws InterruptedException 
	 */
	public long getPortInRate(long ifIndex,int delay)throws IOException{
		if(delay<=0)
			delay = 20;
		long now = System.currentTimeMillis();
		long begin = getIfInOctets(ifIndex);
		long beginTime = System.currentTimeMillis();
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		long end = getIfInOctets(ifIndex);
		long endTime = System.currentTimeMillis()+delay;
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//Logger.out(Logger.DEBUG, "getPortInRate: BeginTime-["+sdf.format(new Date(beginTime))+"] EndTime-["+sdf.format(new Date(endTime))+"] Delta-["+(endTime-beginTime)/1000+"]");
		return (end - begin)*8000/(endTime-beginTime);
	}
	
	/**
	 * 端口的流出流速，计算得到 单位为bps
	 * @return
	 */
	public long getPortOutRate(long ifIndex,int delay)throws IOException{
		if(delay<=0)delay = 20;
		long now = System.currentTimeMillis();
		long begin = getIfOutOctets(ifIndex);
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		return (getIfOutOctets(ifIndex) - begin)*8/delay;
	}
	
	/**
	 * 从ifMIB中获取ifHCInOctets = "1.3.6.1.2.1.31.1.1.1.6"
	 * @param ifIndex
	 * @return 
	 * @throws IOException
	 */
	public long getIfHCInOctets(long ifIndex)throws IOException{
		String oid = ifHCInOctets+"."+ifIndex;
		String ret = snmpGet(oid);
		if(isNumeric(ret)){
			return Long.parseLong(ret);
		}else if(ret.contains("noSuchInstance")){
			throw new IOException("noSuchInstance:OID["+oid+"]");
		}else{
			return -1;
		}
	}
	
	/**
	 * 从ifMIB中获取ifHCOutOctets = "1.3.6.1.2.1.31.1.1.1.10"
	 * @param ifIndex
	 * @return 
	 * @throws IOException
	 */
	public long getIfHCOutOctets(long ifIndex)throws IOException{
		String oid = ifHCOutOctets+"."+ifIndex;
		String ret = snmpGet(oid);
		if(isNumeric(ret)){
			return Long.parseLong(ret);
		}else if(ret.contains("noSuchInstance")){
			throw new IOException("noSuchInstance:OID["+oid+"]");
		}else{
			return -1;
		}
		
	}
	
	/**
	 * 获取CISCO端口流入流速,单位bps
	 * @param ifIndex
	 * @return
	 * @throws IOException
	 */
	public long getCiscoPortInRate(long ifIndex) throws IOException{
		String oid = "1.3.6.1.4.1.9.2.2.1.1.6"+"."+ifIndex;
		String ret = snmpGet(oid);
		if(isNumeric(ret)){
			return Long.parseLong(ret);
		}else if(ret.contains("noSuchInstance")){
			throw new IOException("noSuchInstance:OID["+oid+"]");
		}else{
			return -1;
		}
	}
	
	/**
	 * 获取CISCO端口流出流速，单位bps
	 * @param ifIndex
	 * @return
	 * @throws IOException
	 */
	public long getCiscoPortOutRate(long ifIndex) throws IOException{
		String oid = "1.3.6.1.4.1.9.2.2.1.1.8"+"."+ifIndex;
		String ret = snmpGet(oid);
		if(isNumeric(ret)){
			return Long.parseLong(ret);
		}else if(ret.contains("noSuchInstance")){
			throw new IOException("noSuchInstance:OID["+oid+"]");
		}else{
			return -1;
		}
	}
	/**
	 * 从ifMIB中获取
	 * 	ifHCOutOctets = "1.3.6.1.2.1.31.1.1.1.10"
	 *	
	 * @return
	 */
	public long getHCPortInRate(long ifIndex,int delay) throws IOException{
//		if(delay<=0)
//			delay = 20;
//		long now = System.currentTimeMillis();
//		long begin = getIfHCInOctets(ifIndex);
//		try {
//			while((System.currentTimeMillis()-now)<delay*1000){
//				Thread.sleep(delay*100);
//			}
//		} catch (InterruptedException e) {
//			throw new IOException (e.getMessage());
//		}
//		return (getIfHCInOctets(ifIndex) - begin)*8/delay;
		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if(delay<=0)
			delay = 20;
		long now = System.currentTimeMillis();
		long begin = getIfHCInOctets(ifIndex);
		long beginTime = System.currentTimeMillis();
//		Logger.out(Logger.DEBUG, "第一次获取端口流入流量="+begin+" time="+sdf.format(new Date(beginTime)));
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		long end = getIfHCInOctets(ifIndex);
		
		long endTime = System.currentTimeMillis();
//		Logger.out(Logger.DEBUG, "第二次获取端口流入流量="+end+" time="+sdf.format(new Date(endTime)));
//		Logger.out(Logger.DEBUG, "getHCPortInRate: BeginTime-["+sdf.format(new Date(beginTime))+"] EndTime-["+sdf.format(new Date(endTime))+"] Delta-["+(endTime-beginTime)/1000+"]");
		long ret = (end - begin)*8000/(endTime-beginTime);
//		Logger.out(Logger.DEBUG, "端口["+ifIndex+"]的流入流速HCPortInRate="+ret);
		return ret;
	}
	
	public long getHCPortOutRate(long ifIndex,int delay) throws IOException{
		if(delay<=0)
			delay = 20;
		long now = System.currentTimeMillis();
		long begin = getIfHCOutOctets(ifIndex);
		long beginTime = System.currentTimeMillis();
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		long end = getIfHCOutOctets(ifIndex);
		long endTime = System.currentTimeMillis();
		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		Logger.out(Logger.DEBUG, "getHCPortInRate: BeginTime-["+sdf.format(new Date(beginTime))+"] EndTime-["+sdf.format(new Date(endTime))+"] Delta-["+(endTime-beginTime)/1000+"]");
		return (end - begin)*8000/(endTime-beginTime);
	}

	
	
	/**
	 * 端口流入丢包率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getInLossRate(long ifIndex,int delay)throws IOException{
		if(delay<=0)delay = 5;
		String[] oidList = {ifInDiscards+"."+ifIndex,
				ifInUcastPkts+"."+ifIndex,
				ifInNUcastPkts+"."+ifIndex};
		long now = System.currentTimeMillis();
		String[] begins = snmpGet(oidList);
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		String[] ends = snmpGet(oidList);
		try{
			long beginIfInDiscards = 0;
			long beginIfInUcastPkts = 0;
			long beginIfInNUcastPkts = 0;
			long endIfInDiscards = 0;
			long endIfInUcastPkts = 0;
			long endIfInNUcastPkts = 0;
			if(begins[0] != null && ends[0] != null){
				beginIfInDiscards = Long.parseLong(begins[0]);
				endIfInDiscards = Long.parseLong(ends[0]);
			}
			
			if(begins[1] != null && ends[1] != null){
				beginIfInUcastPkts = Long.parseLong(begins[1]);
				endIfInUcastPkts = Long.parseLong(ends[1]);
			}
			
			if(begins[2] != null && ends[2] != null){
				beginIfInNUcastPkts = Long.parseLong(begins[2]);
				endIfInNUcastPkts = Long.parseLong(ends[2]);
			}
			
			double b = (endIfInUcastPkts-beginIfInUcastPkts+endIfInNUcastPkts-beginIfInNUcastPkts);
			
			double a = (endIfInDiscards - beginIfInDiscards);
			
			if(b==0)return 0;
			return a/b*100<=100?a/b*100:100;
		}catch(NumberFormatException e){
			throw new IOException(e.getMessage()); 
		}
	}
	
	/**
	 * 端口流出丢包率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getOutLossRate(long ifIndex,int delay)throws IOException{
		if(delay<=0)delay = 5;
		String[] oidList = {ifOutDiscards+"."+ifIndex,
				ifOutUcastPkts+"."+ifIndex,
				ifOutNUcastPkts+"."+ifIndex};
		long now = System.currentTimeMillis();
		String[] begins = snmpGet(oidList);
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		String[] ends = snmpGet(oidList);
		try{
			long beginIfOutDiscards = 0;
			long beginIfOutUcastPkts = 0;
			long beginIfOutNUcastPkts = 0;
			long endIfOutDiscards = 0;
			long endIfOutUcastPkts = 0;
			long endIfOutNUcastPkts = 0;
			
			if(begins[0] != null && ends[0] != null){
				beginIfOutDiscards = Long.parseLong(begins[0]);
				endIfOutDiscards = Long.parseLong(ends[0]);
			}
			
			if(begins[1] != null && ends[1] != null){
				beginIfOutUcastPkts = Long.parseLong(begins[1]);
				endIfOutUcastPkts = Long.parseLong(ends[1]);
			}
			
			if(begins[2] != null && ends[2] != null){
				beginIfOutNUcastPkts = Long.parseLong(begins[2]);
				endIfOutNUcastPkts = Long.parseLong(ends[2]);
			}
			
			double b = (endIfOutUcastPkts-beginIfOutUcastPkts+endIfOutNUcastPkts-beginIfOutNUcastPkts);
			
			double a = (endIfOutDiscards - beginIfOutDiscards);
			
			if(b==0)return 0;
			return a/b*100<=100?a/b*100:100;
		}catch(NumberFormatException e){
			throw new IOException(e.getMessage()); 
		}
	}
	
	/**
	 * 端口流入误码率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getInErrorRate(long ifIndex,int delay)throws IOException{
		if(delay<=0)delay = 5;
		String[] oidList = {ifInErrors+"."+ifIndex,
				ifInUcastPkts+"."+ifIndex,
				ifInNUcastPkts+"."+ifIndex};
		long now = System.currentTimeMillis();
		String[] begins = null;
		try{
			begins = snmpGet(oidList);
		}catch(IOException ex){
			if(ex.getLocalizedMessage().contains("noSuchInstance")){
				throw new IOException("IP["+ipAddress+"] noSuchInstance");
			}else{
				throw new IOException(ex.getLocalizedMessage());
			}
		}
		
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		String[] ends = null;
		try{
			ends = snmpGet(oidList);
		}catch(IOException ex){
			if(ex.getLocalizedMessage().contains("noSuchInstance")){
				throw new IOException("IP["+ipAddress+"] noSuchInstance");
			}else{
				throw new IOException(ex.getLocalizedMessage());
			}
		}
		
		
		try{
			
			long beginIfInErrors = 0;
			long beginIfInUcastPkts = 0;
			long beginIfInNUcastPkts = 0;
			long endIfInErrors = 0;
			long endIfInUcastPkts = 0;
			long endIfInNUcastPkts = 0;
			
			if(begins[0] != null && ends[0] != null){
				beginIfInErrors = Long.parseLong(begins[0]);
				endIfInErrors = Long.parseLong(ends[0]);
			}
			
			if(begins[1] != null && ends[1] != null){
				beginIfInUcastPkts = Long.parseLong(begins[1]);
				endIfInUcastPkts = Long.parseLong(ends[1]);
			}
			
			if(begins[2] != null && ends[2] != null){
				beginIfInNUcastPkts = Long.parseLong(begins[2]);
				endIfInNUcastPkts = Long.parseLong(ends[2]);
			}
			
			double b = (endIfInUcastPkts-beginIfInUcastPkts+endIfInNUcastPkts-beginIfInNUcastPkts);
			
			double a = (endIfInErrors - beginIfInErrors);
			
			if(b==0)return 0;
			return a/b*100<=100?a/b*100:100;
		}catch(NumberFormatException e){
			throw new IOException(e.getMessage()); 
		}
	}
	
	/**
	 * 端口流出误码率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getOutErrorRate(long ifIndex,int delay)throws IOException{
		if(delay<=0)delay = 5;
		String[] oidList = {ifOutErrors+"."+ifIndex,
				ifOutUcastPkts+"."+ifIndex,
				ifOutNUcastPkts+"."+ifIndex};
		long now = System.currentTimeMillis();
		String[] begins = snmpGet(oidList);
		try {
			while((System.currentTimeMillis()-now)<delay*1000){
				Thread.sleep(delay*100);
			}
		} catch (InterruptedException e) {
			throw new IOException (e.getMessage());
		}
		String[] ends = snmpGet(oidList);
		try{
			long beginIfOutErrors = 0;
			long beginIfOutUcastPkts = 0;
			long beginIfOutNUcastPkts = 0;
			long endIfOutErrors = 0;
			long endIfOutUcastPkts = 0;
			long endIfOutNUcastPkts = 0;
			
			if(begins[0] != null && ends[0] != null){
				beginIfOutErrors = Long.parseLong(begins[0]);
				endIfOutErrors = Long.parseLong(ends[0]);
			}
			
			if(begins[1] != null && ends[1] != null){
				beginIfOutUcastPkts = Long.parseLong(begins[1]);
				endIfOutUcastPkts = Long.parseLong(ends[1]);
			}
			
			if(begins[2] != null && ends[2] != null){
				beginIfOutNUcastPkts = Long.parseLong(begins[2]);
				endIfOutNUcastPkts = Long.parseLong(ends[2]);
			}
			
			double b = (endIfOutUcastPkts-beginIfOutUcastPkts+endIfOutNUcastPkts-beginIfOutNUcastPkts);
			
			double a = (endIfOutErrors - beginIfOutErrors);
			
			if(b==0)return 0;
			return a/b*100<=100?a/b*100:100;
		}catch(NumberFormatException e){
			throw new IOException(e.getMessage()); 
		}
	}
	
	public IPAddress[] getIPAddressTable() throws IOException {
		String[] oids = {ipAdEntAddr,ipAdEntIfIndex,ipAdEntNetMask,ipAdEntBcastAddr,ipAdEntReasmMaxSize};
		SnmpTable ipTable = snmpGetTable(oids);
		
		List<IPAddress> ipList = new ArrayList<IPAddress>();
		
		String[] indexs = ipTable.getIndexs();
		for(String index:indexs){
			IPAddress ipAddress = new IPAddress();
			String ipAddr = ipTable.getValue(index, ipAdEntAddr);
			long ipIndex = Long.parseLong(ipTable.getValue(index, ipAdEntIfIndex));
			String ipNetMask = ipTable.getValue(index, ipAdEntNetMask);
			String ipBcastAddr = ipTable.getValue(index, ipAdEntBcastAddr);
			int ipReasmMaxSize = Integer.parseInt(ipTable.getValue(index,ipAdEntReasmMaxSize));
			
			ipAddress.setIpAdEntAddr(ipAddr);
			ipAddress.setIpAdEntIfIndex(ipIndex);
			ipAddress.setIpAdEntNetMask(ipNetMask);
			ipAddress.setIpAdEntBcastAddr(ipBcastAddr);
			ipAddress.setIpAdEntReasmMaxSize(ipReasmMaxSize);
			
			ipList.add(ipAddress);
		}

		return ipList.toArray(new IPAddress[ipList.size()]);
		
	}
	
	/**
	 * 获取ipRouteTable信息
	 * @return
	 */
	public IPRoute[] getIPRouteTable()throws IOException{
		String[] oids = {ipRouteDest,ipRouteIfIndex,ipRouteNextHop,ipRouteType,ipRouteProto,ipRouteAge,ipRouteMask};
		SnmpTable ipRouteTable = snmpGetTable(oids);
		List<IPRoute> ipRouteList = new ArrayList<IPRoute>();
		
		String[] indexs = ipRouteTable.getIndexs();
		for(String index:indexs){
			IPRoute ipRoute = new IPRoute();
			String routeDest = ipRouteTable.getValue(index, ipRouteDest);
			String ifIndex_str = ipRouteTable.getValue(index, ipRouteIfIndex);
			if(ifIndex_str == null){
				continue;
				
			}
			long ifIndex = Long.parseLong(ifIndex_str);
			
			String routeNextHop = ipRouteTable.getValue(index, ipRouteNextHop);
			int routeType = Integer.parseInt(ipRouteTable.getValue(index, ipRouteType));
			int routeProto = Integer.parseInt(ipRouteTable.getValue(index,ipRouteProto));
			int routeAge = Integer.parseInt(ipRouteTable.getValue(index,ipRouteAge));
			String routeMask = ipRouteTable.getValue(index,ipRouteMask);
			
			ipRoute.setIpRouteDest(routeDest);
			ipRoute.setIpRouteIfIndex(ifIndex);
			ipRoute.setIpRouteNextHop(routeNextHop);
			ipRoute.setIpRouteType(routeType);
			ipRoute.setIpRouteProto(routeProto);
			ipRoute.setIpRouteAge(routeAge);
			ipRoute.setIpRouteMask(routeMask);
			
			ipRouteList.add(ipRoute);
		}
		return ipRouteList.toArray(new IPRoute[ipRouteList.size()]);
	}
	
	public static void main(String[] args){
		Rfc1213OperImpl snmp = new Rfc1213OperImpl("udp:219.158.65.5/161","cnccngi","cnccngi",1,10000);
		Rfc1213OperImpl snmp2 = new Rfc1213OperImpl("219.158.66.246","public","public",1,10000);
		try {
			snmp.open();
			
			IPRoute[] ipRouteTable = snmp.getIPRouteTable();
			if(ipRouteTable.length > 0){
				
			}
//			IPAddress[] ipAddresses = snmp.getIPAddressTable();
//			IfEntry[] entries = snmp.getIfEntriesTable();
			IfFlow[] flows = snmp.getIfFlowTable();
//			for(IfFlow flow:flows){
//				System.out.println(flow.getIfIndex());
//				System.out.println(flow.getIfInOctets());
//				System.out.println(flow.getIfInUcastPkts());
//			}
			
//			for(IPAddress address:ipAddresses){
//				System.out.println(address.getIpAdEntAddr());
//			}
//			for(IfEntry entry:entries){
//				System.out.println(entry.getIfIndex()+":"+entry.getIfPhysAddress()+":"+entry.getIfDescr());
//			}
//			getIfFlowTable();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			snmp.close();
		}
		
	}

	private boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	

	
}
