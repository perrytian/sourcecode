package ftp.server;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cusi.ods.dtp.audit.AuditResultLog;
import com.cusi.ods.dtp.common.FileNameAnalysis;
import com.cusi.ods.dtp.common.FileNameUtil;
import com.cusi.ods.dtp.common.FileUtil;

public class FileVerify {

	private static final Log log = LogFactory.getLog(FileVerify.class);

	private String md5content;
	private String fileName;
	private String rootPath;
	private int count = 0;
	private String filesuccess;
	private Set<String> errorList = new HashSet<String>();
	private Set<String> reList = new HashSet<String>();

	/*
	 * yzfy 修改文件的根目录 通过配置获取
	 */
	public FileVerify() {
		this.rootPath = SysConf.getString("Ftp.interface");
	}

	public void setMd5content(String md5content) {
		this.md5content = md5content;
	}

	public void setFileName(String filename) {
		this.fileName = filename;
	}

	public void setRootPath(String path) {
		this.rootPath = path;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setFilesuccess(String filesuccess) {
		this.filesuccess = filesuccess;
	}

	/**
	 * 校验MD5是否正确
	 */
	public boolean chkVerify(File chkFile, boolean chk) {
		if (chk) {
			return true;
		} else {
			return true;
		}
	}

	/**
	 * 校验MD5是否正确
	 */
	public boolean chkVerify(File chkFile) {
		log.debug("MD5 Verify Begin");
		boolean re = true;
		List<String> list = new ArrayList<String>();
		FileNameAnalysis fa = new FileNameAnalysis(chkFile.getName());
		File f1 = new File(rootPath + File.separatorChar + SysConf.getString("Ftp.interface.receip") + File.separatorChar + fa.chkFileName() + ".tmp");
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> mapc = new HashMap<String, String>();
		fileContentToMap(chkFile, mapc);
		int[] count = fileContentToMapBak(f1, map);
		if (mapc.size() == 0) {

		}
		Iterator<String> it = mapc.keySet().iterator();
		while (it.hasNext()) {
			String keya = it.next();
			String valuea = mapc.get(keya);

			if (map.containsKey(keya)) {
				if (!valuea.equals(map.get(keya))) {
					log.debug("MD5_Value: " + map.get(keya) + "       " + valuea);
					list.add(reContent(keya, "10022"));
					re = false;
				}
			} else {
				list.add(reContent(keya, "10012"));
				re = false;
			}
		}

		if (count[1] == 0) {
			re = false;
			for (String efile : errorList) {
				list.add(reContent(efile, "出错记录过多"));
			}
		}

		for (String efile : reList) {
			list.add(reContent(efile, "10013"));
		}

		AuditResultLog auditResultLog = new AuditResultLog(rootPath);
		auditResultLog.setContentList(list);
		auditResultLog.setCount(count[0]);

		try {
			auditResultLog.createReceiptFile(chkFile.getName());
		} catch (Exception e) {
			log.error("生成回执出错 Chk FileName:" + chkFile.getName(), e);
		}

		boolean b = f1.delete();
		log.debug("TMP del file:" + b + " MD5 Verify end");
		return re;

	}

	/**
	 * CHK完整性校验失败后写日志
	 */
	public synchronized void chkVerifyLog(String path, String chkname, List<String> filenames) {
		log.debug("CHK完整性校验失败后写日志");
		List<String> list = new ArrayList<String>();

		for (String name : filenames) {
			File file = new File(path + File.separatorChar + name);
			if ((!file.exists()) || !file.isFile()) {
				list.add(reContent(name, "10012"));
			}
		}

		AuditResultLog auditResultLog = new AuditResultLog(rootPath);
		// AuditResultLog auditResultLog = new
		// AuditResultLog(SysConf.getString("Ftp.interface"));
		auditResultLog.setContentList(list);
		try {
			auditResultLog.createReceiptFileFW(path, chkname);
		} catch (Exception e) {
			log.error(rootPath + "生成回执出错 Chk FileName:" + path + File.separatorChar + chkname, e);
		}
	}

	/**
	 * CHK完整性校验失败后写日志山东
	 */
	public void chkVerifySdLog(String path, String chkname, List<String> filenames) {
		List<String> list = new ArrayList<String>();

		for (String name : filenames) {
			File file = new File(path + File.separatorChar + name);
			if ((!file.exists()) || !file.isFile()) {
				list.add(reContent(name, "10012"));
			}
		}

		AuditResultLog auditResultLog = new AuditResultLog(rootPath);
		auditResultLog.setContentList(list);
		try {
			auditResultLog.createReceiptFileSdFW(path + File.separatorChar + chkname);
		} catch (Exception e) {
			log.error("生成回执出错 Chk FileName:" + path + File.separatorChar + chkname, e);
		}
	}

	/**
	 * 校验完整性是否正确(山东没有MD5校验)
	 */
	public boolean chkVerifySd(File chkFile) {
		boolean re = true;
		List<String> list = new ArrayList<String>();
		FileNameAnalysis fa = new FileNameAnalysis(this.fileName);
		File f1 = new File(rootPath + File.separatorChar + SysConf.getString("Ftp.interface.receip") + File.separatorChar + fa.chkFileName() + ".tmp");
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> mapc = new HashMap<String, String>();
		fileContentToMap(chkFile, mapc);
		int[] count = fileContentToMapBak(f1, map);

		Iterator<String> it = mapc.keySet().iterator();
		while (it.hasNext()) {
			String keya = it.next();

			if (!map.containsKey(keya)) {
				list.add(reContent(keya, "10012"));
				re = false;
			}
		}

		if (count[1] == 0) {
			re = false;
			for (String efile : errorList) {
				list.add(reContent(efile, "出错记录过多"));
			}
		}

		for (String efile : reList) {
			list.add(reContent(efile, "10013"));
		}

		AuditResultLog auditResultLog = new AuditResultLog(rootPath);
		auditResultLog.setContentList(list);
		auditResultLog.setCount(count[0]);

		try {
			auditResultLog.createReceiptFile(chkFile.getName());
		} catch (Exception e) {
			log.error("生成回执出错 Chk FileName:" + chkFile.getName(), e);
		}

		boolean b = f1.delete();
		log.debug("TMP del file:" + b);
		log.debug("MD5 Verify end");
		return re;
	}

	/**
	 * 组装返回MD5校验结果
	 * 
	 * @param filekey
	 * @param status
	 * @return
	 */
	public String reContent(String errorfilename, String status) {
		return null;
		/*
		ReceiptContentFormat receiptContentFormat = new ReceiptContentFormat();
		return receiptContentFormat.feContent(errorfilename, status);
		*/
	}

	/**
	 * 保存MD5和稽核数据到临时文件，以便以后和CHK文件对比
	 */
	public synchronized void save() { // 必须有synchronized，以保证多线程时的同步
		FileNameAnalysis fa = new FileNameAnalysis(this.fileName);
		String tmpFileName = rootPath + File.separatorChar + SysConf.getString("Ftp.interface.receip") + File.separatorChar + fa.chkFileName() + ".tmp";
		File tmpFile = new File(tmpFileName);
		if (tmpFile.exists() && tmpFile.isFile()) {
			long lastTime = tmpFile.lastModified();
			if (System.currentTimeMillis() - lastTime > 5 * 3600 * 1000) {
				tmpFile.delete();
			}
		}
		FileUtil2.appendContent(tmpFileName, fa.getFileName(), md5content, this.count, this.filesuccess);
		log.debug("MD5 BAK File:" + tmpFileName + " " + md5content);
	}

	/**
	 * 将MD5值组装到MAP
	 * 
	 * @param filec
	 *            File
	 * @param map
	 */
	public int[] fileContentToMap(File filec, Map<String, String> map) {
		log.debug("将MD5值组装到MAP");
		int[] count = { 0, 1 };
		BufferedReader read = null;
		int a = 0;
		try {
			read = new BufferedReader(new InputStreamReader((new FileInputStream(filec))));
			while (true) {
				a++;
				String content = read.readLine();
				if (content == null) {
					break;
				} else {
					if (!"".equals(content)) {
						if ((a >= 2)) {
							if (content.toUpperCase().indexOf(".AVL") == -1 || content.toUpperCase().indexOf(".DEL") == -1) {
								continue;
							}
							// FIXME
							String[] contents = content.split(",");
							if (contents.length < 2) {
								log.error("CHK格式出错 " + filec.getAbsolutePath());
							}
							map.put(contents[0], contents[1]);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error("FileNotFound:" + filec.getAbsolutePath(), e);
		} catch (IOException e) {
			log.error("IOException:" + filec.getAbsolutePath(), e);
		} catch (Exception e) {
			log.error("CHK格式出错,MD5 error:" + filec.getAbsolutePath(), e);
		} finally {
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					log.error("close error:" + filec.getAbsolutePath(), e);
				}
		}
		return count;
	}

	/**
	 * 将MD5值组装到MAP
	 * 
	 * @param filec
	 *            File
	 * @param map
	 */
	private int[] fileContentToMapBak(File filec, Map<String, String> map) {
		log.debug("将MD5值组装到MAP");
		int[] count = { 0, 1 };
		BufferedReader read = null;
		try {
			read = new BufferedReader(new InputStreamReader((new FileInputStream(filec))));
			while (true) {
				String content = read.readLine();
				if (content == null) {
					break;
				} else {
					if (!"".equals(content)) {

						// if (content.toUpperCase().indexOf(".AVL") == -1 ||
						// content.toUpperCase().indexOf(".DEL") == -1) {
						// continue;
						// }
						String[] contents = content.split(",");
						if (contents.length > 0) {
							if (!FileNameUtil.isGetFileType(contents[0])) {
								continue;
							}
						}
						if (contents.length < 2) {
							log.error("CHK格式出错 " + filec.getAbsolutePath());
						}
						map.put(contents[0], contents[1]);
						if (contents.length == 4) {
							try {
								int c = Integer.valueOf(contents[2]).intValue();
								count[0] = count[0] + c;
								if ("0".equals(contents[3])) {
									count[1] = 0;
									errorList.add(contents[0]);
									log.warn("上传的数据存在问题，CHK不会上传接口机 FileName:" + contents[0]);
								}
								if (c > 5000000) {
									reList.add(contents[0]);
								}
							} catch (Exception e) {
								log.error("读备份数据时，数据类型转换出错,FileName=" + contents[0], e);
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error("FileNotFound:" + filec.getAbsolutePath(), e);
		} catch (IOException e) {
			log.error("IOException:" + filec.getAbsolutePath(), e);
		} catch (Exception e) {
			log.error("CHK格式出错,MD5 error:" + filec.getAbsolutePath(), e);
		} finally {
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					log.error("close error:" + filec.getAbsolutePath(), e);
				}
		}
		return count;
	}

}
