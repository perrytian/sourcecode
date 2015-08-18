package ftp.client;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cusi.ods.dtp.common.DateUtils;
import com.cusi.ods.dtp.common.FileNameAnalysis;
import com.cusi.ods.dtp.common.FileNameFilter;
import com.cusi.ods.dtp.common.FileNameUtil;
import com.cusi.ods.dtp.common.FileUtil;
import com.cusi.ods.dtp.entity.InterfaceTransferRule;
import com.cusi.ods.dtp.ftpserver.FileVerify;

public class UploadFileScanThread implements Runnable {

	private static final Log log = LogFactory.getLog(UploadFileScanThread.class);

	private static final int SLEEP_TIME = 20 * 1000;

	private Thread thread;

	public void start() {
		thread = new Thread(this, "UploadFileScanThread");
		thread.start();
	}

	
	public void run() {
		while (true) {
			try {
				Thread.sleep(SLEEP_TIME);
				String path = SysConf.getString("atiToDtpDir");
				if (path != null && path.length() > 0) {
					if (UploaderManager.getMe().canBeAdd()) {
						// uploadLinkInPath(path);
						groupUploadPath(path);
					}
				} else
					Thread.sleep(10 * 60 * 1000);
			} catch (Throwable e) {
				log.error("轮询上传目录错误", e);
			}
		}
	}

	/**
	 * 根据CHK内容上传
	 * 
	 * @param filename
	 */
	private void groupUploadPath(String path) {
		// 扫描的文件路径
		File file = new File(path);
		String[] fileNames = file.list(new FileNameFilter(null, ".CHK"));
		if (fileNames != null && fileNames.length != 0) {// 如果文件存在
			// 调用文件抓取
			for (String obj : fileNames) { // 上传CHk文件对应的所有文件
				InterfaceTransferRule interfaceTransferRule = InterfaceTransferRuleServicer.getInstance().getInterfaceTransferRule(obj);
				if (interfaceTransferRule == null) {
					log.warn(path + "文件名为：" + obj + "接口单元中没有配置！");
					this.backupFile(path, obj);
					continue;
				}
				log.info("Found CHK, Begin查到数据CHK开始运行，PATH：" + path + " ,FileName:" + obj);
				this.uploadChkFile(path + File.separatorChar + obj, interfaceTransferRule);
			}
		} else {

		}
	}

	private void uploadChkFile(String chkFileNname, InterfaceTransferRule interfaceTransferRule) {
		File file = new File(chkFileNname);
		if ((!file.exists()) || (!file.isFile())) {
			log.warn("文件不存在,或路径不对:" + chkFileNname);
			return;
		}

		// String type = "" +
		// RuleManager.getInstance().getInterfaceDef(file.getName()).getIsAllData();//是否走全转增
		// 0否1是
		// yzfy edit 取消上边的UploadFtpConfig 代码 加入新的规则
		// InterfaceTransferRule interfaceTransferRule =
		// InterfaceTransferRuleServicer.getInstance().getInterfaceTransferRule(file.getName());
		List<String> filenames = new ArrayList<String>();
		try {
			filenames = getFileNameList(file, interfaceTransferRule.getInterfaceDef().getDataStartLine());
		} catch (IOException e) {
			log.error("CHK File read error move to backup:" + file.getAbsolutePath(), e);
			backupFile(file);
			return;
		}

		// 完整性校验 是否完整性校验
		if (interfaceTransferRule != null && interfaceTransferRule.getIsCheck() == 1) {
			log.debug("完整性校验" + chkFileNname);
			// 完整性校验
			boolean isAll = checkFileComplete(file.getParentFile().getAbsolutePath(), filenames);
			if ((!isAll) || (filenames.size() == 0)) {// 写回执备份
				log.error("完整性校验失败，FileName:" + file.getAbsolutePath());
				FileVerify fileVerify = new FileVerify();
				// fileVerify.setChkFile(file);
				fileVerify.chkVerifyLog(file.getParentFile().getAbsolutePath(), file.getName(), filenames);
				backupFile(file);
				return;
			}
		}

		// 移动到备份目录中
		String path = backupFile(file);

		if (path != null) {
			filenames.add(file.getName());
			for (String filec : filenames) {

				// 原有file变更为规范filename
				// if(SysConf.serverType() == (SysConfig.SD_SERVER)){
				// filec = simpleDtpService.sdChangeFileName(filec, path);
				// }
				// log.debug("准备上传文件:" + filec);

				// UploadFtpConfig uploadFtpConfig = UploadFtpConfigServicer
				// .getInstance().getUploadFtpConfig(filec);

				// yzfy edit 取消上边的UploadFtpConfig 代码 加入新的规则
				// InterfaceTransferRule interfaceTransferRule =
				// InterfaceTransferRuleServicer.getInstance().getInterfaceTransferRule(filec);

				// 不是全转增并且在扫描CHK文件才校验MD5
				// if(FileNameUtil.isChkFile(filec)&&SysConf.todoAfterDownload()!=SysConfig.AFD_ATOI){//CHK时进行MD5校验并写数据回执
				// if(allb){
				// //组合回执
				// FileVerify fileVerify = new FileVerify();
				// fileVerify.setRootPath(file.getParentFile().getParentFile().getAbsolutePath());
				// fileVerify.chkVerify(new
				// File(path+File.separatorChar+filec));
				// }
				// }
				/***************************************************************
				 * @author LiangKai 需要增加判断条件，如果接口单元的上传开关为0的话则不需要向上传队列增加任务
				 * @Date 2013/02/23
				 */
				// if (uploadFtpConfig == null ||
				// uploadFtpConfig.getInterfaceDef().getUploadFlag()==0) {
				// log.warn("当前接口文件没有对应的二次上传信息,上传被取消,文件名=" + filec);
				// continue;
				// }
				// yzfy 新的业务规则修改
				/***************************************************************
				 * @author LiangKai 需要增加判断条件，如果接口单元的上传开关为0的话则不需要向上传队列增加任务
				 * @Date 2013/02/23 yzfy 修改
				 */
				if (interfaceTransferRule == null) {
					log.warn("interfaceTransferRule没有配置当前接口文件没有对应的二次上传信息,上传被取消,文件名=" + filec);
					continue;
				} else {
					String uploadFlag = interfaceTransferRule.getInterfaceDef().getUploadFlag();
					if ("0".equals(uploadFlag)) {
						log.warn("uploadFlag=" + uploadFlag + "当前接口文件二次上传信息已经关闭,文件名=" + filec);
						continue;
					}
				}

				// if(SysConf.getBoolean("Audit")&&SysConf.todoAfterDownload()!=SysConfig.AFD_ATOI){
				// simpleDtpService.fileStreamAction(path + File.separatorChar +
				// filec);
				// }
				// 上传
				// UploadTask task = new UploadTask();
				// task.setFieName(path + File.separatorChar + filec);
				// task.setFtpServer(uploadFtpConfig.getFtpUser().getFtpResource()
				// .getIp());
				// task.setPort(Integer.parseInt(uploadFtpConfig.getFtpUser()
				// .getFtpResource().getPort()));
				// task.setUserName(uploadFtpConfig.getFtpUser().getUserName());
				// task.setPasswd(uploadFtpConfig.getFtpUser().getPassword());
				// task.setRemotePath("");
				// if(FileNameUtil.isDataFile(filec))
				// task.setNeedZipUpload(true);
				// UploaderManager.getMe().addTask(task);

				// 上传 yzfy 修改 新的业务规则 修改
				this.uploadFile(interfaceTransferRule, path, filec);
				// UploadTask task = new UploadTask();
				// task.setFieName(path + File.separatorChar + filec);
				// task.setFtpServer(interfaceTransferRule.getFtpUser().getFtpResource()
				// .getIp());
				// task.setPort(Integer.parseInt(interfaceTransferRule.getFtpUser()
				// .getFtpResource().getPort()));
				// task.setUserName(interfaceTransferRule.getFtpUser().getUserName());
				// task.setPasswd(interfaceTransferRule.getFtpUser().getPassword());
				// task.setRemotePath("");
				// if(FileNameUtil.isDataFile(filec))
				// task.setNeedZipUpload(true);
				// UploaderManager.getMe().addTask(task);
			}
		}
	}

	/**
	 * @since yzfy 上传文件信息 加入队列中
	 * @param interfaceTransferRule
	 *            规则
	 * @param path
	 *            路径
	 * @param filec
	 *            文件名
	 */
	private void uploadFile(InterfaceTransferRule interfaceTransferRule, String path, String filec) {

		if (interfaceTransferRule != null) {
			File file = new File(path + filec);
			if (!file.isFile()) {
				log.warn("文件不存在》" + path + filec);
				return;
			}
			UploadTask task = new UploadTask();
			task.setFieName(path + filec);
			task.setFtpServer(interfaceTransferRule.getFtpUser().getFtpResource().getIp());
			task.setPort(Integer.parseInt(interfaceTransferRule.getFtpUser().getFtpResource().getPort()));
			task.setUserName(interfaceTransferRule.getFtpUser().getUserName());
			task.setPasswd(interfaceTransferRule.getFtpUser().getPassword());
			task.setRemotePath(interfaceTransferRule.getRemotePath());
			// if(FileNameUtil.isDataFile(filec))
			if (FileNameUtil.isNeedGZip(filec))
				task.setNeedZipUpload(true);

			// 设置上传任务优先级
			task.setPriority(interfaceTransferRule.getInterfaceDef().getPriority());
			// 放入上传队列
			UploaderManager.getMe().addTask(task);
		} else {
			log.debug("当前接口文件没有对应的配置规则信息,上传被取消:" + filec);
		}
	}

	/**
	 * 完整性校验
	 * 
	 * @param path
	 * @param list
	 * @return
	 */
	private boolean checkFileComplete(String path, List<String> list) {
		boolean re = true;
		File fpath = new File(path);
		if ((!fpath.exists()) || !fpath.isDirectory()) {
			return false;
		}

		for (String name : list) {
			File file = new File(path + File.separatorChar + name);
			if ((!file.exists()) || !file.isFile()) {
				re = false;
				break;
			}
		}

		return re;
	}

	/**
	 * 得到数据file列表 yzfy 修改通过配置数据文件读取行
	 * 
	 * @param name
	 * @return
	 */
	public List<String> getFileNameList(File chkFile, int dataLine) throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader re = null;
		try {
			re = new BufferedReader(new InputStreamReader(new FileInputStream(chkFile)));
			int a = 0;
			while (true) {
				a++;
				String content = re.readLine();
				if (content == null) {
					break;
				} else {
					if ("".equals(content) || a < dataLine) {
						continue;
					}
					String[] str = content.split(",");
					list.add(str[0]);

				}

			}
		} finally {
			if (re != null) {
				try {
					re.close();
				} catch (IOException e) {
					log.error("Close file error:" + chkFile.getAbsolutePath(), e);
				}
			}
		}
		return list;
	}

	/**
	 * 备份这组文件
	 * 
	 * @param chkFile
	 *            yzfy 修改备份数据目录通过配置获取
	 * @return 备份的目录
	 */
	private String backupFile(File chkFile) {
		FileNameAnalysis fna = new FileNameAnalysis(chkFile.getName());
		String toPath = chkFile.getParentFile().getParentFile().getAbsolutePath() + File.separatorChar + SysConf.getString("Ftp.interface.backup") + File.separatorChar + SysConf.getString("Ftp.interface.data") + File.separatorChar + fna.getAccountDateByConfig() + File.separatorChar;
		if (FileUtil2.moveGroupFiles(chkFile, new File(toPath))) {
			return toPath;
		} else
			return null;
	}

	/**
	 * 备份这组文件没有配置接口
	 * 
	 * @param chkFilePath
	 *            chkFileName 必须是chk文件 yzfy 修改备份数据目录通过配置获取
	 * @return 失败文件备份的目录
	 * 
	 */
	private String backupFile(String chkFilePath, String chkFileName) {
		File chkFile = new File(chkFilePath, chkFileName);
		if (!chkFile.exists() || !chkFile.isFile() || !FileNameUtil.isChkFile(chkFile.getName())) {
			log.warn("文件不存在,或路径不对,或不是chk文件:" + chkFilePath);
			return null;
		}
		String toPath = chkFilePath + File.separatorChar + SysConf.getString("Ftp.interface.backup") + File.separatorChar + DateUtils.dateYYYYMMDD(new Date()) + File.separatorChar;
		if (FileUtil2.moveGroupFiles(chkFile, toPath)) {
			return toPath;
		} else
			return null;
	}
	//	
	// /**
	// * 上传每一个文件
	// * @param path
	// */
	// private void uploadLinkInPath(String path) {
	// try{
	// File filePath = new File(path);
	// String[] fileNames = filePath.list(new FileNameFilter(null,".link"));
	// if (fileNames != null && fileNames.length != 0) {// 如果文件存在
	// // 调用文件抓取
	// for (String fn : fileNames) {
	// File linkFile = new File(path + File.separatorChar + fn);
	// //获取link的内容
	// String fileName =
	// FileUtil.getUploadLinkFileContent(linkFile.getAbsolutePath());
	// //删除link文件
	// linkFile.delete();
	// if(fileName==null){
	// continue;
	// }
	// File file = new File(fileName);
	// //获取上传信息
	// UploadFtpConfig uploadFtpConfig = UploadFtpConfigServicer
	// .getInstance().getUploadFtpConfig(file.getName());
	// if (uploadFtpConfig != null) {
	// UploadTask task = new UploadTask();
	// task.setFieName(file.getAbsolutePath());
	// task.setFtpServer(uploadFtpConfig.getFtpUser().getFtpResource()
	// .getIp());
	// task.setPort(Integer.parseInt(uploadFtpConfig.getFtpUser()
	// .getFtpResource().getPort()));
	// task.setUserName(uploadFtpConfig.getFtpUser().getUserName());
	// task.setPasswd(uploadFtpConfig.getFtpUser().getPassword());
	// task.setRemotePath("");
	// if(FileNameUtil.isDataFile(file.getName()) &&
	// SysConf.getBoolean("dataFTPUpload")) {
	// task.setNeedZipUpload(false);
	// }
	// //放入上传队列
	// UploaderManager.getMe().addTask(task);
	// }else {
	// log.debug("当前接口文件没有对应的二次上传信息,上传被取消:" + fn);
	// }
	// }
	// }
	// }catch(Exception e){
	// log.error("轮询上传目录错误",e);
	// }
	// }

}
