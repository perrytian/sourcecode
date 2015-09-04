package snmp.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.*;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.*;

import snmp.tools.objects.SnmpTable;
import snmp.tools.objects.SnmpValue;

public class SnmpV1OperImpl implements SnmpV1Oper {
	
	private static Logger logger = Logger.getLogger(SnmpV1OperImpl.class);
			

	public static final int DEFAULT = 0;

	public static final int WALK = 1;
	public static final int LISTEN = 2;
	public static final int TABLE = 3;
	public static final int CVS_TABLE = 4;
	public static final int TIME_BASED_CVS_TABLE = 5;
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
	public static final String MAC_OID = "1.3.6.1.2.1.2.2.1.6";

	private Address address;
	private Snmp snmp = null;
	private OID privProtocol;
	private OctetString privPassphrase;
	private OctetString authPassphrase;

	private OctetString readCommunity = new OctetString("public");
	private OctetString writeCommunity = new OctetString("private");

	// private OctetString authoritativeEngineID;

	// private OctetString contextEngineID;
	private OctetString contextName = new OctetString();
	private OctetString securityName = new OctetString();

	// private TimeTicks sysUpTime = new TimeTicks(0);
	//
	// private OID trapOID = SnmpConstants.coldStart;

	private PDUv1 v1TrapPDU = new PDUv1();

	private int version = SnmpConstants.version2c;

	private int retries = 1;

	private long timeout = 2000;

	private int maxRepetitions = 10;

	private int nonRepeaters = 0;

	protected int operation = DEFAULT;
	
	protected String ipAddress;
	
	public SnmpV1OperImpl(String ip, String readCommunity,String writeCommunity,int retries, long timeout) {
		this(ip, readCommunity, writeCommunity, SnmpConstants.version2c,retries,timeout);//默认使用SNMP V2版本
	}

	public SnmpV1OperImpl(String ip, String readCommunity,String writeCommunity, int version,int retries, long timeout) {
		
		ipAddress = ip;
		
		if(ip.indexOf(':')<0)
			ip = "udp:" + ip;
		if(ip.indexOf('/')<0)
			ip = ip + "/161";
		
		this.address = GenericAddress.parse(ip);
		if (readCommunity != null)
			this.readCommunity = new OctetString(readCommunity);
		if (writeCommunity != null)
			this.writeCommunity = new OctetString(writeCommunity);

		this.retries = retries;
		this.timeout = timeout;
		
		this.version = version;
	}
	
	public String getIPAddress(){
		return this.ipAddress;
	}

	public void setCommunity(String readComm, String writeComm) {
		if (readComm != null) {
			this.readCommunity = new OctetString(readComm);
		}
		if (writeComm != null) {
			this.writeCommunity = new OctetString(writeComm);
		}
	}

	protected void printVariableBindings(PDU response) {
		if (response.getErrorIndex() != 0) {
			System.out.println("Response received with requestID="
					+ response.getRequestID() + ", errorIndex="
					+ response.getErrorIndex() + ", " + "errorStatus="
					+ response.getErrorStatusText() + "("
					+ response.getErrorStatus() + ")");
			for (int i = 0; i < response.size(); i++) {
				VariableBinding vb = response.get(i);
				System.out.println(vb.toString());
			}
		}
	}

	public void open() throws IOException {
		AbstractTransportMapping transport = null;

		if (address instanceof TcpAddress) {
			transport = new DefaultTcpTransportMapping();
		} else {
			transport = new DefaultUdpTransportMapping();
		}
		snmp = new Snmp(transport);

		if (version == SnmpConstants.version3) {
			byte[] localEngineID = MPv3.createLocalEngineID();
			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(
					localEngineID), 0);
			SecurityModels.getInstance().addSecurityModel(usm);
			snmp.setLocalEngine(localEngineID, 0, 0);
			snmp.getUSM().addUser(
					securityName,
					new UsmUser(securityName, AuthMD5.ID, authPassphrase,
							PrivDES.ID, privPassphrase));
		}
		
		snmp.listen();
	}
	
	
	public void close(){
		try {
			if (snmp != null) {
				snmp.close();
				snmp = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PDU createPDU(Target target, int type) {
		PDU request;
		if (target.getVersion() == SnmpConstants.version3) {
			request = new ScopedPDU();
			ScopedPDU scopedPDU = (ScopedPDU) request;
			// if (contextEngineID != null) {
			// scopedPDU.setContextEngineID(contextEngineID);
			// }
			if (contextName != null) {
				scopedPDU.setContextName(contextName);
			}
		} else {
			if (type == PDU.V1TRAP) {
				request = v1TrapPDU;
			} else {
				request = new PDU();
			}
		}
		request.setType(type);
		return request;
	}

	/**
	 * @param ip
	 * @return SNMP4J包中的Target对象句柄
	 */
	private Target createTarget(OctetString community) {
		Target target;
		if (version == SnmpConstants.version3) {
			UserTarget userTarget = new UserTarget();
			if (authPassphrase != null && privPassphrase != null) {
				userTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
			} else {
				userTarget.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
			}
			userTarget.setSecurityName(securityName);
			target = userTarget;
		} else {
			CommunityTarget commtarget = new CommunityTarget();
			commtarget.setCommunity(community);
			target = commtarget;
		}
		target.setVersion(version);
		target.setAddress(address);
		target.setRetries(retries);
		target.setTimeout(timeout);
		return target;
	}

	public String[] snmpGet(String[] oidList) throws IOException {
		if(snmp==null)
			throw new IOException("Snmp is not open");
		
		String[] result = new String[oidList.length];
		Target target = createTarget(readCommunity);
		
		PDU request = createPDU(target, PDU.GET);
		request.setType(PDU.GET);

		for (String oidStr : oidList) {
			OID tempOid = new OID(oidStr);
			VariableBinding tempVB = new VariableBinding(tempOid);
			request.add(tempVB);

			// result.addTitle(key);
		}
		PDU response = null;

		ResponseEvent responseEvent;

		responseEvent = snmp.send(request, target);

		if (responseEvent != null) {
			response = responseEvent.getResponse();

			if (response != null){
				if(response.getErrorIndex()==PDU.noError && response.getErrorStatus()==PDU.noError){
					//成功
					for (int i = 0; i < response.size(); i++) {
						VariableBinding tempVB = response.get(i);
						String newOid = tempVB.getOid().toString();
						String value = tempVB.getVariable().toString();
						//TODO
						if("noSuchInstance".equals(value)){
							logger.warn( "----[noSuchInstance] 不支持OID:["+newOid+"],target:"+target);
							value="0";//TODO
						}
						
						if (newOid.equals(oidList[i])) {
							if(newOid.contains(MAC_OID)){
								result[i] = value;
							}else{
								result[i] = convertHexStr(value);
							}
						} else {
							result[i] = NA;
						}
					}
				}else{
					//失败
					if(oidList.length>1){
						for (int i = 0; i < oidList.length; i++) {
							try{
								result[i] = snmpGet(oidList[i]);
							}catch(IOException e){
								if(e.getLocalizedMessage().contains("No such name OID")){
									logger.error( "Snmp Get Error:"+e.getMessage());
								}else{
									logger.error( "Snmp Get Error:"+e.getMessage(),e);
								}
								result[i] = null;
							}
						}
					}else{
						if(response.getVariableBindings().size() >= response.getErrorIndex()){
							VariableBinding varBinding = (VariableBinding)response.getVariableBindings().get(response.getErrorIndex()-1);
							OID oid = varBinding.getOid();
							throw new IOException("IP["+target.getAddress().toString()+"] readComm["+readCommunity+"] "+response.getErrorStatusText()+" OID["+oid+"]");
						}else{
							throw new IOException("IP["+target.getAddress().toString()+"]"+response.getErrorStatusText());
						}
					}
				}
			}else{
//				logger.error( "SNMP ERROR");
				//snmp 不通，网元SNMP服务没有开启
				throw new IOException("IP["+target.getAddress().toString()+"] SNMP没有响应");
			}
		} else {
			// responseEvent == null
			//超时
			throw new IOException("发送SNMP没有响应 request info:"
					+ request.toString());
		}

		return result;
	}

	public String snmpGet(String oid) throws IOException {
		if(snmp==null)
			throw new IOException("Snmp is not open");
		
		String result = null;
		Target target = createTarget(readCommunity);
		
		PDU request = createPDU(target, PDU.GET);
		request.setType(PDU.GET);

		OID tempOid = new OID(oid);
		VariableBinding tempVB = new VariableBinding(tempOid);
		request.add(tempVB);

		PDU response = null;

		ResponseEvent responseEvent;

		responseEvent = snmp.send(request, target);

		if (responseEvent != null) {
			response = responseEvent.getResponse();

			if (response != null){
				if(response.getErrorIndex()==PDU.noError && response.getErrorStatus()==PDU.noError){
					//成功

					VariableBinding responseVB = response.get(0);
					String newOid = responseVB.getOid().toString();
					if (newOid.equals(oid)) {
						if(newOid.contains(MAC_OID)){
							result = responseVB.getVariable().toString();
						}else{
							result = convertHexStr(responseVB.getVariable().toString());
						}
					} else {
						result = NA;
					}

				}else{
//					Logger.out(Logger.DEBUG, "输出SNMP出错信息：SNMP ERROR INDEX=［"+response.getErrorIndex()+"］ SNMP ERROR STATUS=［"+response.getErrorStatus()+"］");
					
					//失败
					if(response.getVariableBindings().size() >= response.getErrorIndex()){
						VariableBinding varBinding = (VariableBinding)response.getVariableBindings().get(response.getErrorIndex()-1);
						OID errOid = varBinding.getOid();
						throw new IOException("IP["+target.getAddress().toString()+"] readComm["+readCommunity+"] "+response.getErrorStatusText()+" OID["+errOid+"]");
					}else{
						throw new IOException("IP["+target.getAddress().toString()+"]"+response.getErrorStatusText());
					}
				}
			}else{
//				logger.error( "SNMP ERROR");
				//snmp 不通，网元SNMP服务没有开启
				throw new IOException("IP["+target.getAddress().toString()+"] SNMP没有响应");
			}
		} else {
			// responseEvent == null
			//超时
			throw new IOException("发送SNMP没有响应 request info:"
					+ request.toString());
		}

		return result;
	}

	public SnmpValue[] snmpGetNext(String[] oids)throws IOException {
		if(snmp==null)
			throw new IOException("Snmp is not open");
		
		SnmpValue[] result = new SnmpValue[oids.length];

		Target target = createTarget(readCommunity);
		PDU request = createPDU(target, PDU.GETNEXT);

		if (request.getType() == PDU.GETBULK) {
			request.setMaxRepetitions(maxRepetitions);
			request.setNonRepeaters(nonRepeaters);
		}
		for (String oid : oids) {
			VariableBinding tempVB = new VariableBinding(new OID(oid));
			request.add(tempVB);
		}

		PDU response = null;

		ResponseEvent responseEvent;
		// long startTime = System.currentTimeMillis();
		responseEvent = snmp.send(request, target);
		if (responseEvent != null) {
			response = responseEvent.getResponse();

			if (response != null){
				if(response.getErrorStatus() > 0) {
					throw new IOException(response.getErrorStatusText());
				}
				for (int i = 0; i < response.size(); i++) {
					VariableBinding tempVB = response.get(i);
					SnmpValue snmpValue = new SnmpValue();
					snmpValue.setOid(tempVB.getOid().toString());
					if(tempVB.getOid().toString().contains(MAC_OID)){
						snmpValue.setValue(tempVB.getVariable().toString());
					}else{
						snmpValue.setValue(convertHexStr(tempVB.getVariable().toString()));
					}
					result[i] = snmpValue;
				}
			}else{
				throw new IOException("IP["+target.getAddress().toString()+"] SNMP没有响应");
			}
		} else {
			// responseEvent == null
			throw new IOException("发送SNMP没有响应 request info:"
					+ request.toString());
		}
		
		return result;
	}

	public SnmpValue snmpGetNext(String oid) throws IOException {
		String[] oids = new String[1];
		oids[0] = oid;
		return snmpGetNext(oids)[0];
	}
	
	/**
	 * code by zhengbin
	 */
//	public SnmpTable snmpGetTable(String[] oidList)throws IOException {
//
//		SnmpTable snmpTable = new SnmpTable();
//		
//		while(true){
//			SnmpValue[] values = snmpGetNext(oidList);
//			
//			String oid = values[0].getOid();
//			
//			if(oid.startsWith(oidList[0]))
//				break;
//		
//			snmpTable.setRows(values);
//		}
//		return snmpTable;
//	}
	
	/**
	 * Modify by jianghao
	 */
	public SnmpTable snmpGetTable(String[] columns)throws IOException {
		
		if(columns==null||columns.length<=0){
			throw new IOException("Snmp table parameter error");
		}
		SnmpTable snmpTable = new SnmpTable(columns);

		ArrayList<String> cols = new ArrayList<String>();
		ArrayList<String> oids = new ArrayList<String>();
		for(String oid:columns){
			cols.add(oid);
			oids.add(oid);
		}
		
		
		ArrayList<String> oldOids = new ArrayList<String>();
		
		while(cols.size()>0){
			SnmpValue[] values = snmpGetNext(oids.toArray(new String[oids.size()]));
			
			
			oldOids.clear();
			oldOids.contains(oids);
			
			oids.clear();
			
			String[] c = cols.toArray(new String[cols.size()]);
			
			for(int index = 0;index < values.length;index++){
				String oid = values[index].getOid();
				
				if(oid.startsWith(c[index])){
					snmpTable.setCell(values[index]);
					oids.add(oid);
				}else{
					cols.remove(c[index]);
				}
			}
		}
		return snmpTable;
	}

	private String convertUTF8Str(String hexStr) {
		String rtn = hexStr.trim();
		String[] tempByte = hexStr.split(":");
		// byte[] hexStrBytes = new byte[tempByte.length];

		if (tempByte.length > 3) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			for (int i = 0; i < tempByte.length; i++) {
				os.write(Integer.parseInt(tempByte[i], 16) & 0xFF);
			}
			byte[] hexStrBytes = os.toByteArray();
			try {
				String tempUTF8Str = new String(hexStrBytes, "UTF-8");
				byte[] GBKBytes = tempUTF8Str.getBytes("GBK");
				rtn = new String(GBKBytes, "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return rtn;
	}

	private String convertHexStr(String hexStr) {
		String rtn = hexStr.trim();
		String[] tempByte = hexStr.split(":");
		// if(temp)
		if (tempByte.length > 3 && hexStr.length() == (tempByte.length * 3 - 1))
		// if(hexStr.length() == (tempByte.length * 3 -1))
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			for (int i = 0; i < tempByte.length; i++) {
				os.write(Integer.parseInt(tempByte[i], 16) & 0xFF);
			}
			rtn = os.toString();
		}

		return rtn.trim();
	}

	public void snmpSet(String oid, String value,int type) throws IOException {
		String[] oids = new String[1];
		String[] vaules = new String[1];
		int[] types = new int[1];
		oids[0] = oid;
		vaules[0] = value;
		types[0] = type;
		snmpSet(oids,vaules,types);
	}

	public void snmpSet(String[] oids, String[] values,int[] types) throws IOException {
		if(snmp==null)
			throw new IOException("Snmp is not open");
		
		Target target = createTarget(writeCommunity);

		PDU request = createPDU(target, PDU.SET);
		request.setType(PDU.SET);

		for (int i=0;i<oids.length;i++) {
			OID tempOid = new OID(oids[i]);
			String value = values[i];
			int type = types[i];
			
			if(type == OCTETSTRING)
			{
				Variable var = null;
				if(value.startsWith("0x"))
				{
					var = OctetString.fromHexString(value.substring(2), ':');
				}
				else
				{
					var = new OctetString(value);
				}
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else if(type == INTEGER)
			{
				Variable var = new Integer32(Integer.parseInt(value));
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else if(type == COUNTER32)
			{
				Variable var = new Counter32(Integer.parseInt(value));
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else if(type == GAUGE32)
			{
				Variable var = new Gauge32(Integer.parseInt(value));
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else if(type == COUNTER64)
			{
				Variable var = new Counter64(Integer.parseInt(value));
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else if(type == OID)
			{
				Variable var = new OID(value);
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else if(type == NULL)
			{
				Variable var = new Null();
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else if(type == TIMETICKS)
			{
				Variable var = new TimeTicks(Long.parseLong(value));
				VariableBinding tempVB = new VariableBinding(tempOid, var);
				request.add(tempVB);
			}
			else
			{
				throw new IOException("目前不支持的SNMPSet数据类型：oid : " + tempOid
						+ ", value : " + value + ", type : " + type);
			}
			
		}
		PDU response = null;

		ResponseEvent responseEvent;
		
		responseEvent = snmp.send(request, target);

		if (responseEvent != null) {
			response = responseEvent.getResponse();
			if(response != null){
				if(response.getErrorIndex()==response.noError && response.getErrorStatus()==response.noError){
					//成功
//					Logger.out(Logger.DEBUG,"IP["+target.getAddress().toString()+"] SET操作成功");
				}else{
					//失败
					throw new IOException("IP["+target.getAddress().toString()+"] "+response.getErrorStatusText());
				}
			}else{
				logger.debug("IP["+target.getAddress().toString()+"]SNMP SET操作返回的PDU为空");
			}

//			if (response != null){
//				if(response.getErrorStatus() > 0) {
//					throw new IOException(response.getErrorStatusText());
//				}
//			}else{
//				//snmpSet没有返回信息
////				Logger.out(Logger.DEBUG,"返回消息为空");
////				throw new IOException("Snmp error");
//			}
		} else {
			//超时
			throw new IOException("发送SNMP没有响应 request info:"
					+ request.toString());
		}
	}
}
