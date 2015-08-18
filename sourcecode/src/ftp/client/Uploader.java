package ftp.client;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.cusi.ods.dtp.common.FileNameUtil;

/**
 * 上传数据线程
 * @author xinyaoqiang
 *
 */
public class Uploader implements Runnable{
	
	private static final Log log = LogFactory.getLog(Uploader.class);
	
	UploaderManager manager;
	
	public Uploader(UploaderManager manager){
		this.manager = manager;
	}
	
	public Uploader(){
		
	}
	
	
	public void run() {
		UploadTask task;
		try {
			while((task=manager.getTask())!=null){
				//根据任务上传文件
				try{
				  uploadFile(task);
				}catch(Throwable e){
					log.error("上传文件时发生异常：FileName="+task.getFieName()+",Server="+task.getFtpServer(),e);
					
				}
			}
		} catch (Exception e) {
			log.error("从队列中取上传任务时出错", e);
		}
	}
	
	/**
	 * 上传数据改为压缩流
	 * @param remotePath
	 * @param filename
	 * @param input
	 * @param ftp
	 * @return
	 * @throws IOException
	 */
	private boolean toZipStreamStoreFileStream(String filename, InputStream input, FTPClient ftp) throws IOException{
		OutputStream ftpos =ftp.storeFileStream(filename + ".gz");
		BufferedInputStream isb = new BufferedInputStream(input);
		GZIPOutputStream gs = null;
		try{
			gs = new GZIPOutputStream(ftpos);
			byte[] b = new byte[4096];
			int lc;
			while((lc = isb.read(b))>0){
				gs.write(b, 0, lc);
			}
			gs.finish();
			gs.flush();
		}catch(IOException e){
			throw e;
		}finally{
			if(gs!=null)
				try{
					gs.close();
				}catch(IOException e){
					log.error("close ftp.storeFileStream",e);
				}
		}
		return true;
	}
	
	public boolean uploadFile(UploadTask task){
		log.debug("FileName:" + task.getFieName() + " 开始上传, Host:" + task.getFtpServer() + " User:" + task.getUserName());
		String msg = "FTP文件上传";
		long beginTime = System.currentTimeMillis();
		int reply;
		String fileName = task.getFieName();
		String host = task.getFtpServer();
		String userName = task.getUserName();
		String passwd = task.getPasswd();
		int port = task.getPort();
		task.setUploadStatus(false);
		String remotePath = task.getRemotePath();
		String beginHost = SysConf.getString("sys.alias");

		File file = new File(fileName);
		
		FTPClient ftp = new FTPClient();
		try {
			ftp.setConnectTimeout(10000);
			ftp.connect(host, port);
			ftp.setSoTimeout(20000);
			ftp.login(userName, passwd);
			
			ftp.setControlKeepAliveTimeout(60);

			reply = ftp.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)){
				ftp.disconnect();
				return false;
			}
			
			ftp.changeWorkingDirectory(remotePath);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.setSendBufferSize(1024*2);
			ftp.setDataTimeout(30000);	
			ftp.setDefaultTimeout(30000);
			ftp.enterLocalPassiveMode();
//			ftp.changeWorkingDirectory("/data");
			
			InputStream input = null;	
			boolean flag = false;
			try{
				input = new FileInputStream(file);
				if(task.isNeedZipUpload()){
					flag = toZipStreamStoreFileStream(file.getName(), input, ftp);
				}else{
					flag = ftp.storeFile(file.getName(),input);
				}
				
				if(flag){
					task.setUploadStatus(true);
					msg = "发送文件完成 Host:" + host + " UserName:" + userName + " FileName:" + fileName;
					log.info("发送文件完成 Host:" + host + " UserName:" + userName + " FileName:" + fileName);
				}else{
					msg = "文件发送失败!!FileName:" + file.getAbsolutePath();
					log.warn("文件发送失败!!FileName:" + file.getAbsolutePath());
				}
			}finally{
				if(input!=null)
					try{
						input.close();
					}catch(IOException e){
						log.error("close file error:"+file.getAbsolutePath(),e);
					}
			}
			ftp.logout();
		
			return true;
		} catch (FileNotFoundException e){
			task.setUploadCount(9999);
			msg = "发送文件时file not found Host:" + host + " UserName:" + userName + " FileName:" + fileName+e.getMessage();
			log.error("发送文件时file not found Host:" + host + " UserName:" + userName + " FileName:" + fileName, e);
			task.setUploadStatus(false);
		} catch (Exception e) {
			msg = "发送文件时传输出错 Host:" + host + " UserName:" + userName + " FileName:" + fileName+e.getMessage();
			log.error("发送文件时传输出错 Host:" + host + " UserName:" + userName + " FileName:" + fileName, e);
			task.setUploadStatus(false);
		} finally{
			if(ftp.isConnected()){
				try {
					ftp.disconnect();
				} catch (IOException e) {
					log.error("发送文件后关闭连接出错 Host:" + host + " UserName:" + userName, e);
				}
			}
			if(!task.getUploadStatus()){
				manager.returnTask(task,msg);
			} else {
				// DTP上传日志
				long endTime = System.currentTimeMillis();
				// 计算check文件内的文件数
				int fileCount = -1;
				if (FileNameUtil.isChkFile(fileName)) {
				//	CheckFile chkTemp = new CheckFile(new File(fileName));
					//if (chkTemp.isValid()) {
						//fileCount = chkTemp.getFileCount();
					//}
				}
				
				/***************************************************************
				 * @author LiangKai 日志中添加参数：文件大小 2013/03/04 yzfy Log
				
				
				TranceforLogger tLog = TranceforLogManager.createLog();
				// tLog.addLog(fileName, '9', beginTime, endTime, beginHost,
				// host, null, task.getUploadStatus() ? 1 : 0, 0, 0, null,
				// fileCount, file.length());
				com.cusi.ods.dtp.log.Log dmcQInterfacedata = new com.cusi.ods.dtp.log.Log();
				dmcQInterfacedata.setBeginHost(beginHost);
				dmcQInterfacedata.setEndHost(host);
				dmcQInterfacedata.setErrorDesc(msg);
				dmcQInterfacedata.setAvlCount(fileCount);
				tLog.addLog(fileName, "9", beginTime, endTime, "1", 0, file.length(), dmcQInterfacedata);
				tLog.send();
				 */
			}
		}
		return false;
	}
}
