package socket.demo;


/**
 * 
 * @author  jianghao
 */
public class H2Header{
	
	/** 数据包大小(5位)：以字节为单位，左对齐，不足补空格，如大小为256个bytes,应为:256   */
	private int a1PacketSize;

	/** 标志(1位) :1表示成功 0表示失败，仅适用于响应包*/
	private boolean a2Tag;
	
	/** 服务类型(6位)-前四位0000-9999标志各种服务,后两位00-99标示服务序号 */
	private int a3ServiceCode;

	/** 包编号(5位)-标志该笔流水的第几包数据：从00001开始 */
	private int a4Seq;

	/** 最后一包标志(1位):在进行多包发送的情况下，该标志用以标明是否为最后一数据包：1最后一包数据，无后续包，0非最后一包数据，有后续包。连接错误，IO错误等 */
	private boolean a5IsEnd;
	
	/** 错误码(5位) :在标志为失败时需检查该错误码。错误码包括系统操作错误和业务处理错误*/
	private int a6ErrorCode;
	
	private byte[] bytes = new byte[H2Constant.HEADER_LENGTH];
	
	public H2Header(){
		
	}
	
	public H2Header(byte[] data){
		setHeader(data);
	}
	
	public void setHeader(byte[] data){
		System.arraycopy(data, 0, bytes, 0, H2Constant.HEADER_LENGTH);
		
		a1PacketSize = 0;
		for(int i=0;i<5;i++){
			if(bytes[i]>=0x30&&bytes[i]<=0x39){
				a1PacketSize = a1PacketSize*10+(bytes[i]-0x30);
			}else
				break;
		}
		a2Tag = bytes[5]=='1';
		
		a3ServiceCode = 0;
		for(int i=6;i<12;i++){
			if(bytes[i]>=0x30&&bytes[i]<=0x39){
				a3ServiceCode = a3ServiceCode*10+(bytes[i]-0x30);
			}else
				break;
		}
		
		a4Seq = 0;
		for(int i=12;i<17;i++){
			if(bytes[i]>=0x30&&bytes[i]<=0x39){
				a4Seq = a4Seq*10+(bytes[i]-0x30);
			}else
				break;
		}
		
		a5IsEnd = bytes[17]=='1';
		
		a6ErrorCode = 0;
		for(int i=18;i<23;i++){
			if(bytes[i]>=0x30&&bytes[i]<=0x39){
				a6ErrorCode = a6ErrorCode*10+(bytes[i]-0x30);
			}else
				break;
		}
	}

	
	public int getA1PacketSize() {
		return a1PacketSize;
	}
	
	public void setA1PacketSize(int packsize){
		a1PacketSize = packsize;
		int t = a1PacketSize;
		int len = 0;
		while((t/10)>0){
			t /= 10;
			len++;
		}
		int i;
		t = a1PacketSize;
		for(i = len;i>=0;i--){
			bytes[i] = (byte) (t%10+0x30);
			t /= 10;
		}
		for(i=len+1;i<5;i++){
			bytes[i] = 0x20;
		}
	}
	
	public boolean getA2Tag() {
		return a2Tag;
	}
	
	public void setA2Tag(boolean tag) {
		a2Tag = tag;
		bytes[5] = a2Tag?(byte)'1':(byte)'0';
	}

	public int getA3ServiceCode() {
		return a3ServiceCode;
	}
	public void setA3ServiceCode(int a3ServiceCode) {
		this.a3ServiceCode = a3ServiceCode;
		int t = a3ServiceCode;
		for(int i=11;i>=6;i--){
			bytes[i] = (byte)((t%10)+0x30);
			t = t/10;
		}
	}
	public int getA4Seq() {
		return a4Seq;
	}
	public void setA4Seq(int a4Seq) {
		this.a4Seq = a4Seq;
		int t = this.a4Seq;
		for(int i=16;i>=12;i--){
			bytes[i] = (byte)((t%10)+0x30);
			t = t/10;
		}
	}
	public boolean getA5IsEnd() {
		return a5IsEnd;
	}
	public void setA5IsEnd(boolean a5IsEnd) {
		this.a5IsEnd = a5IsEnd;
		bytes[17] = a5IsEnd?(byte)'1':(byte)'0';
	}
	public int getA6ErrorCode() {
		return a6ErrorCode;
	}
	public void setA6ErrorCode(int a6ErrorCode) {
		this.a6ErrorCode = a6ErrorCode;
		int t = this.a6ErrorCode;
		for(int i=22;i>=18;i--){
			bytes[i] = (byte)((t%10)+0x30);
			t = t/10;
		}
	}
	@Override
    public String toString()
    {
        StringBuffer output = new StringBuffer();
		output.append("\n======H2协议包头信息========");
	    output.append("\n数据包大小："+getA1PacketSize());
	    if(a2Tag){
	    	//A2=1
	    	 output.append("\n响应标志:1(成功)");
	    }else{
	    	//A2=0
	    	 output.append("\n响应标志:0(失败)");
	    }
	    output.append("\n服务类型:"+a3ServiceCode);
	    output.append("\n包编号:"+a4Seq);
	    output.append("\n最后一包标志:"+a5IsEnd);
	    output.append("\n错误码:"+a6ErrorCode);
	    output.append("\n=====================");
	    return output.toString();
    }
	
	public byte[] getBytes(){
		return bytes;
	}
	
	/**
	 * 获取固定长度的字符串，左对齐右补空格
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	protected String getFixedLenStr(String str, int len) {
		String retStr = str;
		String ch = " ";
		if (str.length() < len) {
			StringBuffer sb=new StringBuffer(retStr);
			for (int i = str.length(); i < len; i++) {
				// 右补空格
				sb.append(ch);
				//retStr = retStr + ch;
			}
			retStr=sb.toString();
		} else {
			retStr = retStr.substring(0, len);
		}
		return retStr;
	}
}