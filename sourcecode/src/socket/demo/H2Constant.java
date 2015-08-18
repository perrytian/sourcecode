package socket.demo;

public class H2Constant {
	
    /** 固定头长度 23字节*/
    public static final int HEADER_LENGTH = 23;
	  /** 包结束符 */
	public static final byte PKG_END_BYTE = 0x1a;
     /** 字段字段分隔符*/
	public static final byte FIELD_SEPERATOR_BYTE = 0x09;
     /** 记录分隔符*/
	public static final byte[] RECORD_SEPERATOR = {0x0d,0x0a};
	/**包大小限制在20K字节*/
	public static final int PACK_SIZE_THRESHOLD = 20*1000;
	
	/** 标志(1位) :1表示成功 0表示失败，仅适用于响应包*/
	public static final byte TAG_SUCCESS =0x31; 
	public static final byte TAG_FAILURE = 0x30;
	
	/** 最后一包标志(1位):在进行多包发送的情况下，该标志用以标明是否为最后一数据包：1最后一包数据，无后续包，0非最后一包数据，有后续包。连接错误，IO错误等 */
	public static final byte LAST_PACK = 0x31;
	public static final byte NOT_LAST_PACK = 0x30;
	
	/** 错误码(5位) :在标志为失败时需检查该错误码。错误码包括系统操作错误和业务处理错误*/
//	public static final byte[] ErrorCode_NOERROR = {'0','0','0','0','0'};//数据包正确
//	public static final byte[] ErrorCode_EXCEPTION = {'0','0','0','0','1'};//ODS服务程序异常
//	public static final byte[] ErrorCode_SOCKETERR = {'0','0','0','0','2'};//Socket异常
//	public static final byte[] ErrorCode_NODATA =  {'0','0','0','0','3'};//无数据记录
//	public static final byte[] ErrorCode_TIMEOUT = {'0','0','0','0','4'};//ODS服务程序操作超时	
//	public static final byte[] ErrorCode_PARERROR = {'1','0','0','0','1'};//取请求包中参数错误！
	
	public static final int ErrorCode_NOERROR = 0;//数据包正确
	public static final int ErrorCode_EXCEPTION = 1;//ODS服务程序异常
	public static final int ErrorCode_SOCKETERR = 2;//Socket异常
	public static final int ErrorCode_NODATA =  3;//无数据记录
	public static final int ErrorCode_TIMEOUT = 4;//ODS服务程序操作超时	
	public static final int ErrorCode_PARERROR = 10001;//取请求包中参数错误！
	
	/**空数据*/
	public static final byte NULL_VALUE = 0x00;
	
	
	
}
