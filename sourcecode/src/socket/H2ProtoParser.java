package socket;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bytes.util.ByteArray;


/**
 * H2协议解析器
 * @author jianghao
 *
 */
public class H2ProtoParser {
	
	private static Logger logger = Logger.getLogger(H2ProtoParser.class);
	private ByteArray recieveBuffer = new ByteArray();
	
	public List<Request> parser(byte[] data,int length){
		 List<Request> requestList = new ArrayList<Request>(); 
		for(int i = 0;i<length;i++){
			byte b = data[i];
			recieveBuffer.append(b);
			if(b == H2Constant.PKG_END_BYTE ){
				//包结束符
				byte[] requestBytes = recieveBuffer.getBuffer();
				int reqLen = recieveBuffer.size();
				
				//logger.debug("接收原始数据(未校验):"+new String(requestBytes,0,reqLen));
				//校验数据格式
				if(!validRequest(requestBytes,reqLen)){
					//校验失败
					//logger.debug("数据校验失败:"+new String(requestBytes,0,reqLen));
					recieveBuffer.clear();
					continue;
				}
				//校验成功
				
				//生成请求结构Request
				//生成请求包头
				H2Header header = new H2Header(requestBytes);
				//生成请求包体部分
				List<byte[]> requestParams = getH2RequestParam(requestBytes,H2Constant.HEADER_LENGTH,reqLen-H2Constant.HEADER_LENGTH);
				
				Request request = new Request();
				request.setHeader(header);
				request.setParams(requestParams);
				
				requestList.add(request);
				
				recieveBuffer.clear();
			}
		}
		if(recieveBuffer.size()>H2Constant.PACK_SIZE_THRESHOLD){
			recieveBuffer.clear();
		}
		
	    return requestList;
	}
	
	private List<byte[]> getH2RequestParam(byte[] requestBytes,int pos,int length){
	    List<byte[]> paramList = new ArrayList<byte[]>();
		int start = pos;
		for(int index = pos;index<pos+length;index++){
			byte b = requestBytes[index];
			
			if(b == H2Constant.FIELD_SEPERATOR_BYTE || b==H2Constant.RECORD_SEPERATOR[0]){
				byte[] paramItem= new byte[index-start];
				System.arraycopy(requestBytes, start, paramItem, 0, index-start);
				paramList.add(paramItem);
				if(b == H2Constant.FIELD_SEPERATOR_BYTE)
					start = index+1;
				else
					start = index + H2Constant.RECORD_SEPERATOR.length;
			}
		}
		return paramList;
	}
	
	/**
	 * H2协议格式校验方法
	 * @param requestBytes
	 * @return
	 */
	private boolean validRequest(byte[] requestBytes,int length){
		if(length < H2Constant.HEADER_LENGTH){
			logger.error("[接收到的包格式错误]: 总字节数("+requestBytes.length +")小于规定的包头字节数（"+H2Constant.HEADER_LENGTH+"）: 数据内容["+new String(requestBytes)+"]");
			return false;
		}
		//请求包大小
		byte[] a1 = new byte[5];
		System.arraycopy(requestBytes, 0, a1, 0, 5);
		int packsize = 0;
		for(int i=0;i<5;i++){
			if(a1[i]>=0x30&&a1[i]<=0x39){
				packsize = packsize*10+(a1[i]-0x30);
			}else{
				break;
			}
		}
		if( length != packsize){
			logger.error("[接收到的包格式错误]: 总字节数与协议包头[数据包大小不一致]：实际大小["+length+"]字节,包头信息中的数据包大小["+packsize+"]字节: 数据内容："+new String(requestBytes,0,length));
			return false;
		}
		
//	     //记录分隔符
//		if(requestBytes[length-3] != H2Constant.RECORD_SEPERATOR [0] || requestBytes[length-2] != H2Constant.RECORD_SEPERATOR [1]){
//			logger.error("[接收到的消息格式错误]:查询请求参数没有记录分隔符    数据内容："+new String(requestBytes,0,length));
//			return false;
//		}
		
		return true;
	}

}
