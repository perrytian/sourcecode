package telnet;

import java.io.IOException;
import java.net.SocketException;


public interface Telnet {
	
	public void connect(String ip,String username,String password) throws SocketException, IOException;
	
	public void su(String password);
	
	public void disconnect();
	
	/**
	 * 向TELNET服务端发送命令并回显
	 * @param command
	 * @return 回显字符串
	 */
	public String sendCommand(String command);

}
