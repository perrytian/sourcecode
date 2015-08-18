package socket.aio;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AIODemoTest {
	
	public static void main(String[] args){
		
		
		try {
			SimpleServer server = new SimpleServer(8888);
			
			SimpleClientClass client = new SimpleClientClass("127.0.0.1",8888);
			String test = "hello world";
			for(byte b:test.getBytes()){
				client.write(b);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}