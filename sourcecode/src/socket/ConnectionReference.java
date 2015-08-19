package socket;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * 连接引用
 * @author Jianghao
 *
 */
public class ConnectionReference{
	
	private static Logger logger = Logger.getLogger(ConnectionReference.class);
	
	private SocketInstance socketInstance;//连接实例
	
	private Request request;//查询请求
	
	private Response response;//查询响应
	
	private long startTime=System.currentTimeMillis();
	
	public SocketInstance getSocketInstance() {
		return socketInstance;
	}

	public void setSocketInstance(SocketInstance socketInstance) {
		this.socketInstance = socketInstance;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Response getResponse() {
		return response;
	}
	
	public void setResponse(Response response) {
		this.response = response;
	}

	public void execute() throws Exception{
		
		long start = System.currentTimeMillis();
		int serviceCode = request.getHeader().getA3ServiceCode();
		
		QueryProcessor processor = ProcessorFactory.createProcessor(request);
		
		if(processor!=null){
			try{
				
				if(logger.isInfoEnabled()){
					logger.info("开始查询["+this.hashCode()+"]参数:"+request.toString());
				}
				
				byte[] queryResult = processor.executeQuery(request);
				
				Response response = new Response(queryResult); 
				response.setServiceCode(request.getHeader().getA3ServiceCode());
				
				List<Packet> packets = response.getPackets();
				
				//返回查询结果
				
				for(Packet packet:packets){
					byte[] packetBytes = packet.toBytes();
					socketInstance.write(packetBytes,packetBytes.length);
				}
				
				if(logger.isInfoEnabled()){
					StringBuilder buffer = new StringBuilder();
					buffer.append("返回[").append(hashCode());
					buffer.append("][").append(processor.toString());
					buffer.append("][结果包: ").append(packets.size());
					buffer.append(" 个][用时: ").append(System.currentTimeMillis()-start);
					buffer.append(" ms], 处理类:").append(processor.getClass().getName());
					logger.info(buffer.toString());
				}
				
			}catch(QueryException e){
				
				Packet packet = new Packet();
				
				H2Header packHeader = packet.getHeader();
				packHeader.setA2Tag(false);
				packHeader.setA3ServiceCode(request.getHeader().getA3ServiceCode());
				packHeader.setA4Seq(1);
				packHeader.setA5IsEnd(true);
				packHeader.setA6ErrorCode(e.getType());
				
				packet.setBody(new byte[]{H2Constant.PKG_END_BYTE});
				
				byte[] packetBytes = packet.toBytes();
				socketInstance.write(packetBytes,packetBytes.length);
				
				throw e;
			}
		}else{
			Packet packet = new Packet();
			
			H2Header packHeader = packet.getHeader();
			packHeader.setA2Tag(false);
			packHeader.setA3ServiceCode(request.getHeader().getA3ServiceCode());
			packHeader.setA4Seq(1);
			packHeader.setA5IsEnd(true);
			packHeader.setA6ErrorCode(H2Constant.ErrorCode_PARERROR);
			
			packet.setBody(new byte[]{H2Constant.PKG_END_BYTE});
			
			byte[] packetBytes = packet.toBytes();
			logger.info("&&&&&&&&"+new String(packetBytes,0,packetBytes.length));
			socketInstance.write(packetBytes,packetBytes.length);
			logger.error("请求["+this.hashCode()+"]错误，不存在的服务编码:"+serviceCode);
		}
	}
}
