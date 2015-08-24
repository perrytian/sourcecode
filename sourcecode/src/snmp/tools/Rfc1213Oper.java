package snmp.tools;


import java.io.IOException;

import snmp.tools.objects.IPAddress;
import snmp.tools.objects.IPRoute;
import snmp.tools.objects.IfEntry;
import snmp.tools.objects.IfFlow;
import snmp.tools.objects.IfStatus;

/**
 * RFC1213采集适配器服务接口
 * @author mouse
 *
 */
public interface Rfc1213Oper extends SnmpV1Oper{
	
	/**
	 * Des:sysDescr 系统描述
	 * OID:1.3.6.1.2.1.1.1.0
	 * @return
	 */
	public String getSysDescr()throws IOException;
	
	/**
	 * Des:sysObjectID  
	 * OID:1.3.6.1.2.1.1.2.0
	 * @return
	 */
	public String getSysObjectID()throws IOException;
	
	/**
	 * Des:sysUpTime
	 * OID:1.3.6.1.2.1.1.3.0
	 * @return
	 */
	public String getSysUpTime()throws IOException;
	
	/**
	 * Des:sysContact
	 * OID:1.3.6.1.2.1.1.4.0
	 * @return
	 */
	public String getSysContact()throws IOException;
	
	/**
	 * Des:sysName 系统名称
	 * OID:1.3.6.1.2.1.1.5.0
	 * @return
	 */
	public String getSysName()throws IOException;
	
	/**
	 * Des:sysLocation
	 * OID:1.3.6.1.2.1.1.6.0
	 * @return
	 */
	public String getSysLocation()throws IOException;
	
	
	/**
	 * Des:sysServices 
	 * OID:1.3.6.1.2.1.1.7.0
	 * @return
	 */
	public String getSysServices()throws IOException;
	
	/**
	 * 取MIB中system的所有信息 
	 * @return
	 */
	public String[] getSystem()throws IOException;
	
	/**
	 * Des:ifNumber  端口数目
	 * OID:1.3.6.1.2.1.2.1
	 * @return
	 */
	public int getIfNumber()throws IOException;
	
	/**
	 * Des:ifIndex  端口索引
	 * OID:1.3.6.1.2.1.2.2.1.1
	 * @return 返回端口索引的列表
	 */
	public long[] getIfIndex()throws IOException;
	
	/**
	 * 获取端口静态信息
	 * OID:1.3.6.1.2.1.2.2
	 * @return
	 */
	public IfEntry[] getIfEntriesTable()throws IOException;
	
	/**
	 * 获取IpTable信息
	 * @return
	 * @throws IOException
	 */
	public IPAddress[] getIPAddressTable() throws IOException ;
	
	/**
	 * 获取ipRouteTable信息
	 * @return
	 */
	public IPRoute[] getIPRouteTable()throws IOException;
	
	
	/**
	 * 返回ifIndex为索引的端口表某一行的所有数据
	 * @return 
	 */
	public IfEntry getIfEntry(long ifIndex)throws IOException;
	
	public IfStatus[] getIfStatusTable()throws IOException;
	
	public IfStatus getIfStatus(long ifIndex)throws IOException;
	
	public IfFlow[] getIfFlowTable()throws IOException;
	
	public IfFlow getIfFlow(long ifIndex)throws IOException;
	
//	public IPAddress[] getIPAddressTable() throws IOException;
	
	
	
	/**
	 * 获取以ifIndex为索引的端口描述信息
	 * OID:1.3.6.1.2.1.2.2.1.2
	 * @return
	 */
	public String getIfDescr(long ifIndex)throws IOException;
	
	/**
	 * 获取以ifIndex为索引的端口类型
	 * OID:1.3.6.1.2.1.2.2.1.3
	 * @return
	 */
	public int getIfType(long ifIndex)throws IOException;
	
	/**
	 * 获取以ifIndex为索引的端口MTU
	 * OID:1.3.6.1.2.1.2.2.1.4
	 * @return
	 */
	public long getIfMtu(long ifIndex)throws IOException;
	
	/**
	 * 获取以ifIndex为索引的端口带宽
	 * OID:1.3.6.1.2.1.2.2.1.5
	 * @return
	 */
	public long getIfSpeed(long ifIndex)throws IOException;
	
	/**
	 * 获取以ifIndex为索引的端口物理地址
	 * OID:1.3.6.1.2.1.2.2.1.6
	 * @return
	 */
	public String getIfPhysAddress(long ifIndex)throws IOException;
	
	/**
	 * 获取以ifIndex为索引的端口管理状态
	 * OID:1.3.6.1.2.1.2.2.1.7
	 * @return
	 */
	public int getIfAdminStatus(long ifIndex)throws IOException;
	
	/**
	 * 获取以ifIndex为索引的端口操作状态
	 * OID:1.3.6.1.2.1.2.2.1.8
	 * @return
	 */
	public int getIfOperStatus(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.9
	 * @return
	 */
	public String getIfLastChange(long ifIndex)throws IOException;
	
	/**
	 * Des:端口流入流量
	 * OID:1.3.6.1.2.1.2.2.1.10
	 * @return
	 */
	public long getIfInOctets(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.11
	 * @return
	 */
	public long getIfInUcastPkts(long ifIndex)throws IOException;

	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.12
	 * @return
	 */
	public long getIfInNUcastPkts(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.13
	 * @return
	 */
	public long getIfInDiscards(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.14
	 * @return
	 */
	public long getIfInErrors(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.15
	 * @return
	 */
	public long getIfInUnknownProtos(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.16
	 * @return
	 */
	public long getIfOutOctets(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.17
	 * @return
	 */
	public long getIfOutUcastPkts(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.18
	 * @return
	 */
	public long getIfOutNUcastPkts(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.19
	 * @return
	 */
	public long getIfOutDiscards(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.20
	 * @return
	 */
	public long getIfOutErrors(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.21
	 * @return
	 */
	public long getIfOutQLen(long ifIndex)throws IOException;
	
	/**
	 * Name:
	 * OID:1.3.6.1.2.1.2.2.1.22
	 * @return
	 */
	public String getIfSpecific(long ifIndex)throws IOException;
	
	/**
	 * 端口的流入流速，计算得到 单位为bps
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return
	 * @throws IOException
	 */
	public long getPortInRate(long ifIndex,int delay)throws IOException;
	
	/**
	 * 端口的流出流速，计算得到 单位为bps
	 * @return
	 */
	public long getPortOutRate(long ifIndex,int delay)throws IOException;
	
	public long getIfHCInOctets(long ifIndex) throws IOException;
	
	public long getIfHCOutOctets(long ifIndex)throws IOException;
	
	public long getHCPortInRate(long ifIndex,int delay)throws IOException;
	
	public long getHCPortOutRate(long ifIndex,int delay)throws IOException;
	
	public long getCiscoPortInRate(long ifIndex) throws IOException;
	
	/**
	 * 获取CISCO端口流出流速，单位bps
	 * @param ifIndex
	 * @return
	 * @throws IOException
	 */
	public long getCiscoPortOutRate(long ifIndex) throws IOException;
	
	/**
	 * 端口流入丢包率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getInLossRate(long ifIndex,int delay)throws IOException;
	
	/**
	 * 端口流出丢包率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getOutLossRate(long ifIndex,int delay)throws IOException;
	
	/**
	 * 端口流入误码率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getInErrorRate(long ifIndex,int delay)throws IOException;
	
	/**
	 * 端口流出误码率
	 * @param ifIndex 端口索引
	 * @param delay 延时时间单位为秒
	 * @return 返回值为百分数（乘100后的值）
	 * @throws IOException
	 */
	public double getOutErrorRate(long ifIndex,int delay)throws IOException;
}

