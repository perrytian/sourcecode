package snmp.tools.objects;


import snmp.tools.Rfc1213Oper;
import snmp.tools.Rfc1213OperImpl;
import snmp.tools.SnmpV1Oper;
import snmp.tools.SnmpV1OperImpl;

public class SnmpFactory {
	
	public static int SNMP_VERSION_V1 = 0;
	public static int SNMP_VERSION_V2C = 1;
	
	public SnmpV1Oper createSnmp(String ip,String readCommunity,String writeCommunity,int version,int retries,long timeout){
		SnmpV1OperImpl snmpV1Oper = new SnmpV1OperImpl(ip,readCommunity,writeCommunity,version,retries,timeout);
		return snmpV1Oper;
	}
	
	public SnmpV1Oper createSnmp(String ip,String readCommunity,String writeCommunity,int retries,long timeout){
		SnmpV1OperImpl snmpV1Oper = new SnmpV1OperImpl(ip,readCommunity,writeCommunity,retries,timeout);
		return snmpV1Oper;
	}
	
	public Rfc1213Oper createRfc1213(String ip,String readCommunity,String writeCommunity,int version,int retries,long timeout){
		Rfc1213OperImpl rfcOperImpl = new Rfc1213OperImpl(ip,readCommunity,writeCommunity,version,retries,timeout);
		return rfcOperImpl;
	}
	
	public Rfc1213Oper createRfc1213(String ip,String readCommunity,String writeCommunity,int retries,long timeout){
		Rfc1213OperImpl rfcOperImpl = new Rfc1213OperImpl(ip,readCommunity,writeCommunity,retries,timeout);
		return rfcOperImpl;
	}
}
