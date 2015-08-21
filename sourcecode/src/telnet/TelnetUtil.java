package telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.log4j.Logger;



public class TelnetUtil implements Telnet{
	
	private static Logger logger = Logger.getLogger(TelnetUtil.class);
	
	private TelnetClient telnet;
	
	//提示符
	private char prompt = '%';
	//telnet端口
	private int port = 23;
	
	private InputStream in;//输入流，接入返回信息
	
	private PrintStream out;
	
	public TelnetUtil(){
		
	}
	
	public TelnetUtil(String ip,String username,String password) throws SocketException, IOException{
		connect(ip,username,password);
		
	}
	
	public void connect(String ip,String username,String password) throws SocketException, IOException {
		
			telnet = new TelnetClient();
			telnet.connect(ip, port);
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());
			
			readUntil("login:");
			write(username);
			readUntil("Password:");
			write(password);
			
			readUntil(prompt+"");

	}
	
	public void su(String password){
		write("su");
		readUntil("Password:");
		write(password);
		prompt = '#';
		readUntil(prompt+"");
	}
	
	public void disconnect(){
		try {
			telnet.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readUntil(String pattern){
		
		try {
			char lastChar = pattern.charAt(pattern.length()-1);
			StringBuffer sb = new StringBuffer();
//			boolean found = false;
			char ch = (char)in.read();
			while(true){
				//System.out.print(ch);
				sb.append(ch);
				if(ch == lastChar){
					if(sb.toString().endsWith(pattern)){
						//Logger.out(Logger.DEBUG, sb.toString());
						System.out.println(sb.toString());
						return sb.toString();
					}
				}
				ch = (char)in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void write(String value){
		out.println(value);
		out.flush();
		//Logger.out(Logger.DEBUG,value);
		logger.info(value);
	}
	
	public String sendCommand(String command){
		write(command);
		return readUntil(prompt+"");
	}
	
	public static void main(String[] args){
		
		TelnetUtil telnet = new TelnetUtil();
		try {
			//telnet.connect("219.158.66.246","root", "root!QAZ");
			telnet.connect("202.99.45.69","cnoc", "Cnoc201012XTJC");
		
			telnet.sendCommand("cd /proc");
			telnet.sendCommand("pwd");
//			telnet.su("root!QAZ");
			telnet.sendCommand("ls");
			telnet.disconnect();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
