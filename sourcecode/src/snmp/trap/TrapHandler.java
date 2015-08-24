package snmp.trap;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;

public class TrapHandler{
	private static Logger logger = Logger.getLogger(TrapHandler.class);

	private SnmpUtil util;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private long _lastInitNeListTime = System.currentTimeMillis();

	// private static final ReentrantReadWriteLock _lock = new
	// ReentrantReadWriteLock();

	
	public TrapHandler(int port){
		util = new SnmpUtil("0.0.0.0", Integer.toString(port), null, true, 0);
		util.registerTrapHandler(this);
	}

	@SuppressWarnings("unchecked")
	public void commonTrapHandler(PDU pdu, String ip){
		
		SnmpTrap snmpTrap = new SnmpTrap();
		
		Vector<VariableBinding> vbs = (Vector<VariableBinding>) pdu.getVariableBindings();
		PDUv1 pduV1 = (PDUv1) pdu;
		int generic_trap = pduV1.getGenericTrap();
		
		snmpTrap.setOriText(pdu.toString());
		snmpTrap.setSourceIP(ip);
		
		long ifIndex = -1; // 端口index
		// 当收到的是端口发过来的linkUp或者linkDown告警时,通过遍历vbs找到其端口号
		if (generic_trap == PDUv1.LINKDOWN || generic_trap == PDUv1.LINKUP){
			for (int i = 0; i < vbs.size(); i++){
				VariableBinding vb = (VariableBinding) vbs.get(i);
				String tempOidStr = vb.getOid().toString();
				String ifIndexOid = "1.3.6.1.2.1.2.2.1.1";
				if (tempOidStr.length() > ifIndexOid.length()){
					tempOidStr = tempOidStr.substring(0, ifIndexOid.length());
					if (tempOidStr.equals("1.3.6.1.2.1.2.2.1.1")){
						ifIndex = Long.parseLong(vb.getVariable().toString());
						break;
					}
				}
			}
			if(ifIndex >= 0){//linkdown,linkup 
				snmpTrap.setIfIndex(ifIndex);
				snmpTrap.setSourceType(SnmpTrap.SOURCETYPE_PORT);
				if(generic_trap == PDUv1.LINKDOWN){
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_LINKDOWN);
				}else if(generic_trap == PDUv1.LINKUP){
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_LINKUP);
					snmpTrap.setClearAlarm(true);
				}
			}
		}
		
		if (generic_trap == PDUv1.ENTERPRISE_SPECIFIC){
			snmpTrap.setTitle("enterprise specific");
			snmpTrap.setGenericTrap(SnmpTrap.GENERIC_ENTERPRISESPECIFIC);
			snmpTrap.setTrapOID( pduV1.getEnterprise().toString());//厂家的trapoid
		}else{
			switch (generic_trap){
				case PDUv1.COLDSTART:
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_COLDSTART);
					snmpTrap.setTitle("cold start");
					break;
				case PDUv1.WARMSTART:
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_WARMSTART);
					snmpTrap.setTitle("warn start");
					break;
				case PDUv1.LINKDOWN:
					snmpTrap.setTitle("link down");
					snmpTrap.setSourceType(SnmpTrap.SOURCETYPE_PORT);
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_LINKDOWN);
					break;
				case PDUv1.LINKUP:
					snmpTrap.setTitle("link up");
					snmpTrap.setSourceType(SnmpTrap.SOURCETYPE_PORT);
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_LINKUP);
					//清除linkDown
					break;
				case PDUv1.AUTHENTICATIONFAILURE:
					snmpTrap.setTitle("authentication failure");
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_AUTHENTICATIONFAILURE);
					break;
				default:
					//当不是以上几种告警时，设置告警原因为告警PDU号
					logger.warn("SNMPTRAP[:162]接收到TRAP,未识别GENERIC_TRAP["+generic_trap+"]");
			}
		}
		
		//设置TRAP生成时间
		if(pduV1.getTimestamp() > 0){
			Date startTime = new Date(pduV1.getTimestamp());
			snmpTrap.setStartTime(startTime);
		}else{
			//设置TRAP生成时间设置为当前时间
			snmpTrap.setStartTime(new Date());
		}
		StringBuffer output = new StringBuffer();
		output.append("\n=============接受到v1版本Trap:来源ip["+ip+"]===============");
		output.append("\n原因:"+snmpTrap.getTitle());
		output.append("\nOriText:"+snmpTrap.getOriText());
		if(snmpTrap.getGenericTrap() == SnmpTrap.GENERIC_LINKDOWN 
				|| snmpTrap.getGenericTrap() == SnmpTrap.GENERIC_LINKUP){
			output.append("\n关联端口Index:"+snmpTrap.getIfIndex());
		}else if(snmpTrap.getGenericTrap() == SnmpTrap.GENERIC_ENTERPRISESPECIFIC){
			output.append("\nTRAPOID:"+snmpTrap.getTrapOID());
		}
		output.append("\n产生时间:"+sdf.format(snmpTrap.getStartTime()));
		output.append("\n内容:"+pdu.toString());
		output.append("\n========================================================");
		
		logger.info(output.toString());
		//TODO:send snmptrap
	}
	
	public void commonTrapHandlerV2(PDU pdu, Variable var, String ip,final Vector vbs){
		
		SnmpTrap snmpTrap = new SnmpTrap();
		snmpTrap.setOriText(pdu.toString());
		snmpTrap.setSourceIP(ip);
		
		long ifIndex = -1; // 端口index
		// 当收到的是端口发过来的linkUp或者linkDown告警时,通过遍历vbs找到其端口号
		
		if (var.equals(SnmpConstants.linkDown)
				|| var.equals(SnmpConstants.linkUp)){
			// 收到linkDowntrap
			for (int i = 0; i < vbs.size(); i++){
				VariableBinding vb = (VariableBinding) vbs.get(i);
				String tempOidStr = vb.getOid().toString();
				String ifIndexOid = "1.3.6.1.2.1.2.2.1.1";
				if (tempOidStr.length() >= ifIndexOid.length()){
					tempOidStr = tempOidStr.substring(0, ifIndexOid.length());
					if (tempOidStr.equals("1.3.6.1.2.1.2.2.1.1")){
						ifIndex = Long.parseLong(vb.getVariable().toString());
						break;
					}
				}
			}
			if(ifIndex >= 0){//linkdown,linkup 
				snmpTrap.setIfIndex(ifIndex);
				snmpTrap.setSourceType(SnmpTrap.SOURCETYPE_PORT);
				
				if(var.equals(SnmpConstants.linkDown)){
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_LINKDOWN);
				}else if(var.equals(SnmpConstants.linkUp)){
					snmpTrap.setClearAlarm(true);
					snmpTrap.setGenericTrap(SnmpTrap.GENERIC_LINKUP);
				}
			}
		}
		if(var.equals(SnmpConstants.coldStart)){
			snmpTrap.setTitle("Cold Start"); 
		}else if(var.equals(SnmpConstants.warmStart)){
			snmpTrap.setTitle("Warm Start"); 
		}else if(var.equals(SnmpConstants.authenticationFailure)){
			snmpTrap.setTitle("Authentication Failure"); 
		}else if(var.equals(SnmpConstants.linkDown)){
			snmpTrap.setTitle("LinkDown"); 
		}else if(var.equals(SnmpConstants.linkUp)){
			snmpTrap.setTitle("LinkUp"); 
		}else{
			snmpTrap.setTitle(var.toString()); 
		}
		
		//设置TRAP生成时间设置为当前时间
		snmpTrap.setStartTime(new Date());
		
		StringBuffer output = new StringBuffer();
		output.append("\n=============接受到v2版本Trap:来源ip["+ip+"]===============");
		output.append("\n原因:"+snmpTrap.getTitle());
		output.append("\nOriText:"+snmpTrap.getOriText());
		if(snmpTrap.getGenericTrap() == SnmpTrap.GENERIC_LINKDOWN 
				|| snmpTrap.getGenericTrap() == SnmpTrap.GENERIC_LINKUP){
			output.append("\n关联端口Index:"+snmpTrap.getIfIndex());
		}
		output.append("\n产生时间:"+sdf.format(snmpTrap.getStartTime()));
		output.append("\n内容:"+pdu.toString());
		output.append("\n========================================================");
		logger.info(output.toString());
	}

	public void process(PDU pdu, String srcIp)
	{
			long currTime = System.currentTimeMillis();
			if (currTime - _lastInitNeListTime > 60 * 60 * 1000)
			{
				_lastInitNeListTime = currTime;
				// initNeList();
			}
			Vector<VariableBinding> vbs = (Vector<VariableBinding>) pdu.getVariableBindings();
			
			if (pdu.getType() == PDU.V1TRAP){// 如果是snmpv1的trap
				//PDUv1 pduV1 = (PDUv1) pdu;
				//int generic_trap = pduV1.getGenericTrap();
				commonTrapHandler(pdu, srcIp);
			}else{
				for (int i = 0; i < vbs.size(); i++){
					if (((VariableBinding) vbs.get(i)).getOid().equals(
						SnmpConstants.snmpTrapOID)){
						Variable var = ((VariableBinding) vbs.get(i))
								.getVariable();
						commonTrapHandlerV2(pdu, var, srcIp, vbs);
					}
				}
			}
	}

	public static String convertUTF8Str(String hexStr)
	{
		String rtn = hexStr.trim();
		String[] tempByte = hexStr.split(":");
		// byte[] hexStrBytes = new byte[tempByte.length];

		if (tempByte.length > 3)
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			for (int i = 0; i < tempByte.length; i++)
			{
				int temp = '.';
				try
				{
					temp = Integer.parseInt(tempByte[i], 16);
				}
				catch (NumberFormatException e)
				{
					System.out.println("ERROR: deocde data error ---" + hexStr);
					e.printStackTrace();
				}

				os.write(Integer.parseInt(tempByte[i], 16) & 0xFF);
			}
			byte[] hexStrBytes = os.toByteArray();
			try
			{
				String tempUTF8Str = new String(hexStrBytes, "UTF-8");
				byte[] GBKBytes = tempUTF8Str.getBytes("GBK");
				rtn = new String(GBKBytes, "GBK");
			}catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}
		return rtn;
	}

	public static String convertHexStr(String hexStr)
	{
		String rtn = hexStr.trim();
		String[] tempByte = hexStr.split(":");
		if (tempByte.length > 3 && hexStr.length() == (tempByte.length * 3 - 1))
		// if(hexStr.length() == (tempByte.length * 3 -1))
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			for (int i = 0; i < tempByte.length; i++)
			{
				int temp = '.';
				try
				{
					temp = Integer.parseInt(tempByte[i], 16);
				}
				catch (NumberFormatException e)
				{
					System.out.println("ERROR: deocde data error ---" + hexStr);
					e.printStackTrace();
				}
				os.write(Integer.parseInt(tempByte[i], 16) & 0xFF);
			}
			rtn = os.toString();
		}
		return rtn.trim();
	}

	/**
	 * @param timeStr
	 *            格式化时间字符串,格式必须为：yyyy-MM-dd HH:mm:ss
	 * @return Date类型数据
	 */
	public static Date getDateTime(String timeStr)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date parseDate;
		Date rtn = null;
		try
		{
			parseDate = dateFormat.parse(timeStr);
			rtn = new Date(parseDate.getTime());
		}catch (ParseException e){
			e.printStackTrace();
		}
		return rtn;
	}

	/**
	 * @param timeStr
	 *            格式化时间字符串,格式必须为：yyyy-MM-dd HH:mm:ss
	 * @param dura
	 *            时长，单位为秒
	 * @return 返回日期类型，返回的数据是在timeStr的时刻再延迟dura的秒数的对应的时刻
	 */
	public static Date getDateTime(String timeStr, int dura)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date parseDate;
		Date rtn = null;
		try
		{
			parseDate = dateFormat.parse(timeStr);
			rtn = new Date(parseDate.getTime() + dura * 1000);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return rtn;
	}

	public void listen()
	{
		// initNeList();
		// start();
		util.listen();
	}

//	public static void main(String args[])
//	{
//		// String inStr =
//		// "32:30:30:37:2D:30:37:2D:32:34:20:31:35:3A:31:37:3A:32:31";
//		// String inStr =
//		// "53:4E:35:37:32:30:30:37:37:32:34:31:35:31:37:32:31:31";
//		String inStr = "E7:83:9F:E8:8D:89:E6:80:BB:E5:B1:80:31:35:35:E7:94:B5:E8:B7:AF";
//		System.out.println(convertHexStr(inStr));
//
//		System.out.println("------" + convertUTF8Str(inStr));
//
//		System.out.println(getDateTime("2007-08-21 00:00:00", 5));
//	}

}
