package socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import util.ByteArray;

public class SocketInstance{
	
	private static Logger logger = Logger.getLogger(SocketInstance.class);

	private final Lock writeLock = new ReentrantLock();
	private ByteArray writeBuffer = new ByteArray();
	private static final int BUFFERSIZE = 65536;
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFERSIZE);
	private long lastValid = System.currentTimeMillis();
	
	private InetSocketAddress socketAddress;
	private SelectionKey key = null;
	private H2ProtoParser parser = new H2ProtoParser();
	private ProcessorEngine processorEngine;//H2协议分析处理器
	private long idleTime;
	
	public SocketInstance(SelectionKey key,ProcessorEngine processorEngine,long idleTime){
		this.processorEngine = processorEngine;
		this.key = key;
		this.idleTime = idleTime;
		SocketChannel sc = (SocketChannel)key.channel();
		setSocketAddress(new InetSocketAddress(
				sc.socket().getInetAddress().getHostAddress(),
				sc.socket().getPort()));
	}

	
	protected void setSelectionKey(SelectionKey key){
		this.key = key;
	}

	
	public void dispatcherWrite(Dispatcher dispatcher)throws IOException{
		writeLock.lock();
		byte[] bytes = writeBuffer.getBuffer();
		ByteBuffer buf = ByteBuffer.wrap(bytes,0,writeBuffer.size());

		SocketChannel sc = (SocketChannel)key.channel();
		int writeCount = sc.write(buf);
		
		if(writeCount>0){
			writeBuffer.remove(0, writeCount);
		}
		if(writeBuffer.size()<=0){
			key = key.interestOps(SelectionKey.OP_READ);
			key.attach(this);
		}

		lastValid = System.currentTimeMillis();
		
		writeLock.unlock();
	}
	
	public void write(ByteArray bytes){
		write(bytes.getBuffer(),bytes.size());
	}
	
	public void write(byte[] data,int size){
		writeLock.lock();
		writeBuffer.append(data,size);
		writeLock.unlock();
		if(key!=null)
			key.selector().wakeup();
	}

	public void dispatcherRead() throws IOException{
		byteBuffer.clear();
		SocketChannel sc = (SocketChannel)key.channel();
		
		
		int readCount = sc.read(byteBuffer);
		if(readCount>0){
			byteBuffer.flip();//用position与limit定位读入数据的位置
			byte[] data = byteBuffer.array();
			int len = byteBuffer.limit();
			
			//解析协议，生成查询请求
			List<Request> requestList = parser.parser(data,len);
			
			for(Request request:requestList){
				if(logger.isDebugEnabled()){
					logger.debug("查询请求：对端IP地址["
							+ sc.socket().getInetAddress()
									.getHostAddress() + "]");
				}
				ConnectionReference packetRef = new ConnectionReference();
				packetRef.setRequest(request);
				packetRef.setSocketInstance(this);
				//将请求发送到处理队列
				processorEngine.addTask(packetRef);
			}
			
			byteBuffer.clear();//清空缓冲区
			lastValid = System.currentTimeMillis();
		}else{
			disconnect();
		}
	}

	public void disconnect(){
		try {
			SocketChannel sc = (SocketChannel)key.channel();
			if(sc.isOpen()){
				sc.close();
			}
			key.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setSocketAddress(InetSocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}
	
	public String getHost(){
		return socketAddress.getHostName();
	}
	
	public int getPort(){
		return socketAddress.getPort();
	}
	
	public void refresh(){
		try{
			writeLock.lock();
			if(key!=null){
				//如果写缓冲区有数据置可写事件标志
				SocketChannel sc = (SocketChannel)key.channel();
				
				if(sc.isConnected()){
					if(writeBuffer.size()>0){
						key = key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						key.attach(this);
					}
				}
			}
			writeLock.unlock();
		}catch(CancelledKeyException e){
			writeLock.unlock();
			disconnect();
		}
		
		//超时断开链接
		if((System.currentTimeMillis() - lastValid)>idleTime){
			disconnect();
		}
	}
	
}
