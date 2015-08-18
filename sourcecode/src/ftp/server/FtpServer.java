/***********************************************************************
 * Module:  FtpServer.java
 * Author:  songzongquan
 * Purpose: Defines the Class FtpServer
 ***********************************************************************/

package ftp.server;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;

import com.cusi.ods.dtp.conf.SysConfFactory;
import com.cusi.ods.dtp.ftpclient.InterfaceTransferRuleServicer;
import com.cusi.ods.dtp.hdfs.HdfsFileSystemFactory;
import com.cusi.ods.dtp.hdfs.HdfsOverFtpSystem;
import com.cusi.ods.dtp.heartbeat.StatusReporter;

public class FtpServer {

	private StatusReporter statusReporter = null;
	private static final Log log = LogFactory.getLog(FtpServer.class);

	private org.apache.ftpserver.FtpServer ftpServer;
	private UserManager um;
	private FtpUserService ftpUserService;
	private static final long REFRESH_TIME = 10 * 60 * 1000;

	private void initSysConf() throws Exception {
		SysConfFactory.init();
		log.info("初始化配置信息(" + SysConf.getString("sys.alias") + ")...");
		SysConf.refresh();

		log.info("加载配置信息(" + SysConf.getString("rest.config.urlAddr") + "...");
		InterfaceTransferRuleServicer.getInstance().refresh();
		log.info("加载传输规则信息...");
	}

	private void initFtp() throws Exception, FtpException {
		StringBuffer output = new StringBuffer();
		//output.append("\n初始化FTP配置信息(监听端口:" + SysConf.getInt("ftp_port") + ")...");
		output.append("\n初始化FTP配置信息(Rest log URI:" + SysConf.getString("rest.log.uri") + ")...");
		output.append("\n初始化FTP配置信息(hdfs.uri:" + SysConf.getString("hdfs.uri") + ")...");
		log.info(output.toString());
		HdfsOverFtpSystem.setHDFS_URI (SysConf.getString("hdfs.uri"));
		
		//HdfsOverFtpSystem.getDfs();
		
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(SysConf.getInt("ftp_port", 21));
		// 对passive模式指定一个端口范围，需要firewall规则支持
		DataConnectionConfigurationFactory f = new DataConnectionConfigurationFactory();
		f.setPassivePorts("20000-22000");
		factory.setDataConnectionConfiguration(f.createDataConnectionConfiguration());
		serverFactory.addListener("default", factory.createListener());
		

		this.ftpUserService = new FtpUserService();
		um = ftpUserService.getPropertiesUserManager();
		for (User user : ftpUserService.getFtpUser()) {
			um.save(user);
		}
		
		serverFactory.setUserManager(um);
		
		serverFactory.setFileSystem(new HdfsFileSystemFactory());
		
		ftpServer = serverFactory.createServer();
	}

	private void init() throws Exception {
		initSysConf();
		/*
		comment by jianghao 20140109 
		new UploadFileScanThread().start();
		*/
		initFtp();
		
		statusReporter = new StatusReporter();
		/*
		 * comment by jianghao 20140109
		QuartZService quartZService = new QuartZService();
		try {
			quartZService.startScheduler(um);
			log.info("定时任务启动完成");
		} catch (SchedulerException e) {
			log.error("定时任务启动出错", e);
		}*/
	}

	private void start() throws FtpException {
		ftpServer.start();
		statusReporter.start();
	}

	private void refreshUsers() throws Exception {
		if (null == this.ftpUserService) {
			this.ftpUserService = new FtpUserService();
		}
		List<User> lista = this.ftpUserService.getFtpUser();
		String[] oldNames = this.um.getAllUserNames();
		// 删除配置里没有的用户
		for (String user : oldNames) {
			boolean removeFlag = true;
			for (User a : lista) {
				if (a.getName().equals(user)) {
					removeFlag = false;
					break;
				}
			}
			if (removeFlag) {
				this.um.delete(user);
			}
		}
		// 添加有的用户
		for (User a : lista) {
			try {
				this.um.save(a);
			} catch (FtpException e) {
				log.error("加载FTP用户出错：" + a.getName(), e);
			}
		}
	}

	private void refresh() {
		log.debug("刷新配置...");
		try {
			SysConf.refresh();
		} catch (Exception e) {
			log.error("读取配置错误", e);
		}
		try {
			InterfaceTransferRuleServicer.getInstance().refresh();
		} catch (Exception e) {
			log.error("读取传输规则配置错误", e);
		}

		try {
			this.refreshUsers();
		} catch (Exception e) {
			log.error("加载Ftp用户出错：", e);
		}
	}

	public static void main(String args[]) {
		FtpServer server = new FtpServer();
		try {
			server.init();
			server.start();
			log.info("HDFS_OVER_DTP(" + SysConf.getString("sys.alias") + ")启动完成......");
		} catch (Exception e) {
			log.error("HDFS_OVER_DTP(" + SysConf.getString("sys.alias") + ")启动错误......", e);
			System.exit(1);
		}
		// 刷新配置循环
		while (true) {
			try {
				server.refresh();
				Thread.sleep(SysConf.getLong("refresh_time", REFRESH_TIME));
			} catch (Throwable e) {
				log.error("刷新配置错误", e);
			}
		}
	}
}
