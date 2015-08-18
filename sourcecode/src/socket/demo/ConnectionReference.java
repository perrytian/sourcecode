package socket.demo;

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
		
		try{
			
			logger.info("开始查询["+this.hashCode()+"]参数:"+request.toString());
			
			byte[] queryResult = "11193  09115070316154511394110201110110018601061671         1Beijinh2st1006000011000001114042324190991      开通    测试    214     2011-11-24 16:36:47.0   5       4       99999830        测试    8986011381101645298             131000  0^Z".getBytes();
			
			Response response = new Response(queryResult); 
			response.setServiceCode(request.getHeader().getA3ServiceCode());
			
			List<Packet> packets = response.getPackets();
			
			//返回查询结果
			
			for(Packet packet:packets){
				byte[] packetBytes = packet.toBytes();
				socketInstance.write(packetBytes,packetBytes.length);
				logger.info("返回结果："+new String(packetBytes));
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
