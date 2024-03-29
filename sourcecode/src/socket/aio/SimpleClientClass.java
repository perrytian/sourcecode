package socket.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SimpleClientClass {
	
	private AsynchronousSocketChannel client;
	
	public SimpleClientClass(String host, int port) throws IOException, 
	                                    InterruptedException, ExecutionException { 
	 this.client = AsynchronousSocketChannel.open(); 
	 Future<?> future = client.connect(new InetSocketAddress(host, port)); 
	 future.get(); 
	} 
	
	public void write(byte b) { 
	 ByteBuffer byteBuffer = ByteBuffer.allocate(32);
	 System.out.println("byteBuffer="+byteBuffer);
	 byteBuffer.put(b);//向 buffer 写入读取到的字符 
	 byteBuffer.flip();
	 System.out.println("byteBuffer="+byteBuffer);
	 client.write(byteBuffer); 
	} 

}