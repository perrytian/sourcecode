package snmp.tools.objects;

public class SnmpErrorCode {
	
	public static final int noError = 0;//一切正常
	public static final int tooBig = 1;//代理无法将回答装入到一个SNMP报文之中
	public static final int noSuchName = 2;//操作指明了一个不存在的变量
	public static final int badValue = 3;//一个set操作指明了一个无效值或无效语法
	public static final int readOnly = 4;//管理进程试图修改一个只读变量
	public static final int genErr = 5;//某些其他的差错
	public static final int noResponse = 6;//SNMP没有响应
	public static final int commitFailed = 14;
	public static final int noSuchObject = 128;//SNMP v2
	public static final int noSuchInstance = 129;//SNMP v2
	
	public static final int unknown = -1;//未知

}
