package commons.net.ftp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;


public class FTPClientDemo {

	public static void main(String[] args) {
		 FTPClient ftp = new FTPClient();
		 ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
		 try {
			ftp.connect("202.99.45.69");
			int reply = ftp.getReplyCode();

	        if (!FTPReply.isPositiveCompletion(reply))
	        {
	        	ftp.disconnect();
	            System.err.println("FTP server refused connection.");
	            System.exit(1);
	        }
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
