package snmp.tools;

import java.io.IOException;

import org.snmp4j.mp.SnmpConstants;

/**
 * @author wcye
 *
 */
public interface SnmpV1Oper{
	
	public static final byte ASN_UNIVERSAL = 0x00;

	public static final byte ASN_CONSTRUCTOR = (byte) 0x20;

	public static final byte ASN_APPLICATION = 0x40;

	public static final byte INTEGER = ASN_UNIVERSAL | 0x02;

	public static final byte INTEGER32 = ASN_UNIVERSAL | 0x02;

	public static final byte OCTETSTRING = ASN_UNIVERSAL | 0x04;

	public static final byte NULL = ASN_UNIVERSAL | 0x05;

	public static final byte OID = ASN_UNIVERSAL | 0x06;

	public static final byte SEQUENCE = ASN_CONSTRUCTOR | 0x10;

	public static final byte IPADDRESS = ASN_APPLICATION | 0x00;

	public static final byte COUNTER = ASN_APPLICATION | 0x01;

	public static final byte COUNTER32 = ASN_APPLICATION | 0x01;

	public static final byte GAUGE = ASN_APPLICATION | 0x02;

	public static final byte GAUGE32 = ASN_APPLICATION | 0x02;

	public static final byte TIMETICKS = ASN_APPLICATION | 0x03;

	public static final byte OPAQUE = ASN_APPLICATION | 0x04;

	public static final byte COUNTER64 = ASN_APPLICATION | 0x06;
	
	public static final String NA = "not available";

	/**
	 * 打开snmp
	 * @throws IOException
	 */
	public void open()throws IOException;
	
	/**
	 * 关闭snmp
	 */
	public void close();
	
	/**
	 * 得到指定oid的value
	 * @param oid
	 * @return
	 * @throws IOException
	 */
	public String snmpGet(String oid)throws IOException;
	
	/**
	 * 得到若干指定oid的value
	 * @param oids
	 * @return
	 * @throws IOException
	 */
	public String[] snmpGet(String[] oids)throws IOException;
	
	/**
	 * 得到指定oid的next value
	 * @param oid
	 * @return 长度为2的字符串数组 第一个为next oid 第2个为next value;
	 * @throws IOException
	 */
	public SnmpValue snmpGetNext(String oid)throws IOException;	

	/**
	 * 得到若干指定oid的next value
	 * @param oids
	 * @return 长度为2的字符串2维数组 第一个为next oid 第2个为next value;
	 * @throws IOException
	 */
	public SnmpValue[] snmpGetNext(String[] oids)throws IOException;
	
	/**
	 * 得到若干指定oid的table value
	 * @param oids
	 * @return 长度为2的字符串2维数组 第一个为next oid 第2个为next value;
	 * @throws IOException
	 */
	public SnmpTable snmpGetTable(String[] oids)throws IOException;
	
	/**
	 * 得到指定oid的value
	 * @param oid
	 * @return
	 * @throws IOException
	 */
	public void snmpSet(String oid,String value,int type)throws IOException;
	
	/**
	 * 得到若干指定oid的value
	 * @param oids
	 * @return
	 * @throws IOException
	 */
	public void snmpSet(String[] oids,String[] values,int[] types)throws IOException;
	
}
