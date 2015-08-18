package socket.demo;

import java.util.ArrayList;
import java.util.List;

import com.cusi.ods.hdap.util.ByteArray;

public class Response {

	private int serviceCode;
	private List<Packet> packets = new ArrayList<Packet>();
	private byte[] resultData;//数据集
	
	public Response(byte[] resultData){
		this.resultData = resultData;
	}
	public int getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(int serviceCode) {
		this.serviceCode = serviceCode;
	}
	/**
	 * 分包逻辑，返回数据包大于20K时，需要进行分包
	 * @return
	 */
	public List<Packet> getPackets(){
		ByteArray dataBuffer = new ByteArray();
		if(resultData==null||resultData.length == 0){
			//没有符合条件的查询结果
			dataBuffer.append(H2Constant.PKG_END_BYTE);//增加包结束符

			Packet packet = new Packet();
			H2Header packHeader = packet.getHeader();
			packHeader.setA2Tag(false);
			packHeader.setA3ServiceCode(serviceCode);
			packHeader.setA4Seq(1);
			packHeader.setA5IsEnd(true);
			packHeader.setA6ErrorCode(H2Constant.ErrorCode_NODATA);
			
			packet.setBody(dataBuffer.toArray());
			packets.add(packet);
			
		}else{
			ByteArray data = new ByteArray(resultData);
			int seqNo = 1;//包编号
			
			int index = 0;
			int findStart = 0;
			int packetStart = 0;
			int maxLen = H2Constant.PACK_SIZE_THRESHOLD - H2Constant.HEADER_LENGTH;
			while(index>=0){
				dataBuffer.clear();
				do{
					if(index>0)
						findStart = index+H2Constant.RECORD_SEPERATOR.length;
					index = data.find(findStart, H2Constant.RECORD_SEPERATOR);
				}while((index-packetStart)<maxLen&&index>=0);
				
				if(index<0){
					dataBuffer.append(data.getBuffer(),packetStart,data.size()-packetStart - H2Constant.RECORD_SEPERATOR.length);
				}else{
					dataBuffer.append(data.getBuffer(), packetStart, findStart-packetStart - H2Constant.RECORD_SEPERATOR.length);
				}
				dataBuffer.append(H2Constant.PKG_END_BYTE);//增加包结束符
				packetStart = findStart;
				int packsize = H2Constant.HEADER_LENGTH+dataBuffer.size();
				Packet packet = new Packet();
				
				H2Header packHeader = packet.getHeader();
				
				packHeader.setA1PacketSize(packsize);
				packHeader.setA2Tag(true);
				packHeader.setA3ServiceCode(serviceCode);
				packHeader.setA4Seq(seqNo);
				packHeader.setA5IsEnd(index>0?false:true);
				packHeader.setA6ErrorCode(H2Constant.ErrorCode_NOERROR);
				
				packet.setBody(dataBuffer.toArray());
				packets.add(packet);
				
				seqNo++;
			}
		}
		//logger.debug("[responseID:"+this.hashCode()+"][响应请求]：查询数据共返回包数["+packets.size()+"],数据字节数["+(resultData==null?0:resultData.length)+"]B");
		return packets;
	}
	/**
	 * 获取下一个包的编号
	 * @param currentSeqlNo
	 * @return
	 */
	/*private byte[] getNextSeqlNo(int currentSeqlNo){
		int nextSeqlNo = currentSeqlNo+1;
		byte[] temp = String.valueOf(nextSeqlNo).getBytes();
		byte[] ret = new byte[5];
		for(int index = 0;index<5-temp.length;index++){
			ret[index] = 0x30;
		}
		for(int i = 5-temp.length;i<5;i++){
			ret[i] = temp[i-(5-temp.length)];
		}
		return ret;
	}*/
	
	/*
	public static void main(String[] args){
		Response response = new Response();
		byte[] bytes = response.getNextSeqlNo(122);
		System.out.println(new String(bytes));
	}*/
}
