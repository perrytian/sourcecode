package ftp.server;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ftpserver.ftplet.FtpSession;

import com.cusi.ods.dtp.common.FileNameAnalysis;
import com.cusi.ods.dtp.common.HdfsFileUtil;
import com.cusi.ods.dtp.entity.InterfaceTransferRule;
import com.cusi.ods.dtp.hdfs.HdfsFileObject;
import com.cusi.ods.dtp.log.RestLog;
import com.cusi.ods.dtp.log.TranceforLogManager;
import com.cusi.ods.dtp.log.TranceforLogger;

/**
 * 本地服务器提供数据的执行动作
 * 
 * @author jianghao
 * 
 */
public class DtpService {

	private static final Log log = LogFactory.getLog(DtpService.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static SimpleDateFormat categorySDF = new SimpleDateFormat("yyyyMMdd");
	private FtpSession session;
	private InterfaceTransferRule interfaceTransferRule = null;
	
	private HdfsFileObject hdfsFile;
	
	public DtpService(HdfsFileObject hdfsFile,FtpSession session,InterfaceTransferRule interfaceTransferRule){
		this.hdfsFile = hdfsFile;
		this.session = session;
		this.interfaceTransferRule = interfaceTransferRule;
	}
	
	public void ioDataConnectionEndAction(long startTime,long size) throws Exception{
		log.info("DTP接收文件完成 : 文件名称[" + hdfsFile.getAbsolutePath() + "] 用户[" + session.getUser().getName() + "]  文件大小[" + size+"B]");

		//今天的日期
		String today = categorySDF.format(new Date());
		
		// 备份文件
		StringBuffer toPath = new StringBuffer();
		toPath.append(session.getUser().getHomeDirectory()).append(File.separatorChar).append(SysConf.getString("Ftp.interface.backup"))
		.append(File.separatorChar).append(SysConf.getString("Ftp.interface.data"))
		.append(File.separatorChar)
		.append(today);

		HdfsFileObject destPath = new HdfsFileObject(toPath.toString());
		
		if(HdfsFileUtil.moveGroupFile(hdfsFile, destPath)){
			String dataFileName = hdfsFile.getName();
			if(!dataFileName.endsWith(".CHK")){
				String srcFile = destPath.getAbsolutePath()+File.separatorChar+dataFileName;
				String linkFile = SysConf.getString("links.dir")+"/"+today+"."+dataFileName;
				createLinkFile(srcFile,linkFile);
				log.info("create link ok src"+srcFile+" link："+linkFile);
			}
		}else{
			log.error("backup files error: " + hdfsFile.getAbsolutePath());
		}
		
		
		/***********************************************************************
		 * @author jianghao 
		 */
		// 写日志
		TranceforLogger tLog = TranceforLogManager.createLog();
		FileNameAnalysis nameAnalysis = new FileNameAnalysis(hdfsFile.getName());
		
		RestLog restlog = new RestLog();
		restlog.setProvinceId(nameAnalysis.getProvCode());
		restlog.setInterfaceId(hdfsFile.getName());
		restlog.setCycleDate(nameAnalysis.getAccountDate());
		restlog.setBeginTime(sdf.format(new Date(startTime)));
		restlog.setEndTime(sdf.format(new Date()));
		restlog.setOperateType("8");//8->10:接口机接收文件
		restlog.setFileSize(size);
		restlog.setBeginHost(session.getClientAddress().getHostName());
		restlog.setEndHost(SysConf.getString("sys.alias"));
		restlog.setErrorCode("0000");//0000：正常  1XXX:传输类异常  2XXX:文件预处理异常 5001 DTP文件空记录 5002 DTP无文件 5003 DTP空文件 5004 DTP失败
		restlog.setErrorDesc("HDFS文件接收完成--TEST");
		restlog.setInterfaceUnit(nameAnalysis.getInterfaceCode());
		
		tLog.addLog(restlog);
		tLog.send();
	}

	public HdfsFileObject getFile() {
		return hdfsFile;
	}

	public String getUserName() {
		return session.getUser().getName();
	}

	public InterfaceTransferRule getInterfaceTransferRule() {
		return interfaceTransferRule;
	}
	
	private void createLinkFile(String srcName, String decName) throws IOException{
		HdfsFileObject linkFile = new HdfsFileObject(decName);
		OutputStream os = null;
		try{
			os = linkFile.createOutputStream(true);
			os.write(srcName.getBytes());
			os.flush();
		}finally{
			os.close();
		}
	}
}
