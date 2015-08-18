package ftp.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class Downloader implements Runnable{
	
	private BlockingQueue<DownloadTask> queue;
	
	public Downloader(BlockingQueue<DownloadTask> queue){
		this.queue = queue;
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		DownloadTask task;
		try {
			while((task=queue.take())!=null){
				//根据任务上传文件
				downloadFile(task);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean downloadFile(DownloadTask task){
		
		int reply;
		String fileName = task.getFieName();
		String host = task.getFtpServer();
		String userName = task.getUserName();
		String passwd = task.getPasswd();
		int port = task.getPort();
		String localPath = task.getLocalPath();
		
		String remotePath = task.getRemotePath();
		
		FTPClient ftp = new FTPClient();
		OutputStream output = null;
		try {
			ftp.connect(host, port);
			ftp.login(userName, passwd);
			reply = ftp.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)){
				ftp.disconnect();
				return false;
			}
			
			ftp.changeWorkingDirectory(remotePath);
			FTPFile[] ftpFiles = ftp.listFiles();
			for(FTPFile ftpFile : ftpFiles){
				if(ftpFile.isFile()){
					
				}
			}
			
			File localFile = new File(localPath+fileName);
			output = new FileOutputStream(localFile);  
			ftp.retrieveFile(fileName, output);
			
			output.close();
			
			ftp.logout();
			
			return true;
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(output!=null)
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(ftp.isConnected())
				try {
					ftp.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return false;
		
	}

	

}
