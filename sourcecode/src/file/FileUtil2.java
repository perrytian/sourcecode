package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;


public class FileUtil2 {

	private static final Logger log = Logger.getLogger(FileUtil2.class);

	/**
	 * 移动文件
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean moveFile(String fileName, String toPath) {
		File fromFile, toFile;
		fromFile = new File(fileName);
		toFile = new File(toPath + File.separatorChar + fromFile.getName());
		return moveFile(fromFile, toFile);
	}

	public static boolean moveFile(File fromFile, File toFile) {
		if (!fromFile.exists()) {
			log.error("from:" + fromFile.getAbsolutePath() + "不存在无法复制");
			return false;
		}
		File toPath = toFile.getParentFile();
		if (!toPath.exists() || !toPath.isDirectory()) {
			if (!toPath.mkdirs()) {
				log.error("创建目录" + toPath.getAbsolutePath() + "失败!");
			}
		}
		if (toFile.exists() && toFile.isFile()) {
			if (!toFile.delete()) {
				log.error("移动文件时，删除旧文件" + toFile.getAbsolutePath() + "失败");
				return false;
			}
		}
		boolean b = fromFile.renameTo(toFile);
		if (b) {
			return true;
		}
		if (!copyFile(fromFile, toFile)) {
			return false;
		}
		if (!fromFile.delete()) {
			log.error("移动:" + fromFile.getAbsolutePath() + "错误：无法删除");
			return false;
		}
		return true;
	}

	/**
	 * 复制文件
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean copyFile(String from, String to) {
		File fromFile, toFile;
		fromFile = new File(from);
		toFile = new File(to);
		return copyFile(fromFile, toFile);
	}

	public static boolean copyFile(File fromFile, File toFile) {
		File tmpFile = new File(toFile.getAbsolutePath() + ".temp");
		FileInputStream fis = null;
		FileOutputStream fos = null;

		if (!fromFile.exists()) {
			log.error("from:" + fromFile.getAbsolutePath() + "不存在无法复制");
			return false;
		}
		File toPath = toFile.getParentFile();
		if (!toPath.exists() || !toPath.isDirectory()) {
			if (!toPath.mkdirs()) {
				log.error("创建目录" + toPath.getAbsolutePath() + "失败!");
			}
		}
		try {
			tmpFile.createNewFile();
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(tmpFile);
			int bytesRead;
			byte[] buf = new byte[4 * 1024];// 4K buffer
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.flush();
		} catch (Exception e) {
			log.error("from:" + fromFile.getAbsolutePath() + " to:" + toFile.getAbsolutePath() + " 复制失败");
			return false;
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					log.error("close error:" + fromFile.getAbsolutePath(), e);
				}
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					log.error("close error:" + tmpFile.getAbsolutePath(), e);
				}
		}
		return tmpFile.renameTo(toFile);
	}

	/**
	 * 移动一个接口文件组到指定目录
	 * 
	 * @param interfaceName
	 * @param toPath
	 * @return
	 */
	public static boolean moveGroupFiles(File interFile, File toPath) {
		// 判断目录不存在就创建 yzfy 目录创建代码没有，补上。
		if (!toPath.exists() || !toPath.isDirectory()) {
			Boolean bool = toPath.mkdirs();
			if (bool)
				log.info("创建目录" + toPath.getAbsolutePath() + "成功!");
			else
				log.error("创建目录" + toPath.getAbsolutePath() + "失败!");
		}
		// 分析文件名
		FileNameAnalysis fa = new FileNameAnalysis(interFile.getName());
		// 扫描目录读取文件
		File fromPath = interFile.getParentFile();
		if (fromPath == null)
			return false;
		String[] fileNames = fromPath.list(new FileNameFilter(fa.chkFileName(), null));
		boolean flag = false;
		for (String f : fileNames) {
			// 修改需求：当文件移动不成功是10次数的继续移动，直到成功
			synchronized (f) {
				int n = 0;
				do {
					flag = moveFile(fromPath.getAbsolutePath() + File.separatorChar + f, toPath.getAbsolutePath());
					n++;
					if (n > 10) {
						log.error("文件移动失败" + fromPath.getAbsolutePath() + File.separatorChar + f + "toPath:" + toPath.getAbsolutePath());
						break;
					}
				} while (!flag && n <= 10);
			}
		}
		return flag;
	}

	/**
	 * 添加CHK格式的数据
	 * 
	 * @param fileName
	 * @param dataFileName
	 * @param content
	 */
	public static void appendContent(String fileName, String dataFileName, String content, int count, String filesuccess) {
		String contents = dataFileName + "," + content + "," + count + "," + filesuccess;
		File file = new File(fileName);
		File toPath = file.getParentFile();
		if (!toPath.exists() || !toPath.isDirectory()) {
			if (!toPath.mkdirs()) {
				log.error("创建目录" + toPath.getAbsolutePath() + "失败");
			}
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName, true);
			contents = contents + "\n";
			writer.write(contents);
		} catch (IOException e) {
			log.error("写文件错误 file:" + fileName, e);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					log.error("关闭文件错误 file:" + fileName, e);
				}
		}
	}

	/**
	 * 追加回执内容
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendContent(String fileName, List<String> content) {
		File file = new File(fileName);
		File toPath = file.getParentFile();
		if (!toPath.exists() || !toPath.isDirectory()) {
			if (!toPath.mkdirs()) {
				log.error("创建目录" + toPath.getAbsolutePath() + "失败");
			}
		}
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName, true);
			for (String c : content) {
				char b = 0x0A;
				c = c + b;
				writer.write(c);
			}
		} catch (IOException e) {
			log.error("写文件错误:" + fileName, e);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					log.error("关闭文件错误:" + fileName, e);
				}
		}
	}

	public static boolean isUploadLinkFile(String file) {
		return file != null && file.endsWith(".link");
	}

	/**
	 * 在上传目录生成再上传文件的连接文件
	 * 
	 * @param file
	 *            希望上传的文件
	 * @param uploadScanPath
	 *            上传扫描目录
	 * @return
	 */
	public static boolean createUploadLinkFile(File file, File uploadScanPath) {
		if (!uploadScanPath.exists() || !uploadScanPath.isDirectory()) {
			if (!uploadScanPath.mkdirs()) {
				log.error("创建目录" + uploadScanPath.getAbsolutePath() + "失败");
				return false;
			}
		}
		File link = new File(uploadScanPath.getAbsolutePath() + File.separatorChar + file.getName() + ".link");
		FileWriter writer = null;
		try {
			writer = new FileWriter(link, false);
			writer.write(file.getAbsolutePath());
		} catch (IOException e) {
			log.error("写文件错误:" + link.getAbsolutePath(), e);
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					log.error("关闭文件错误:" + link.getAbsolutePath(), e);
				}
		}
		return true;
	}

	/**
	 * 从link文件读取内容
	 * 
	 * @param file
	 * @return
	 */
	public static String getUploadLinkFileContent(String file) {
		FileReader reader = null;
		try {
			char[] buf = new char[2048];
			reader = new FileReader(file);
			int count = reader.read(buf, 0, 2048);
			return new String(buf, 0, count);
		} catch (IOException e) {
			log.error("读文件错误:" + file, e);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					log.error("关闭文件错误:" + file, e);
				}
		}
		return null;
	}

	/**
	 * 移动一个接口文件组到指定目录
	 * 
	 * 
	 * @param interfaceName
	 * @param toPath
	 * @return
	 */
	public static boolean moveGroupFiles(File chkFile, String toPath) {
		if (!chkFile.isFile()) {
			log.warn("文件不存在：" + chkFile.getAbsolutePath());
			return false;
		}
		File toPathFile = new File(toPath);
		// 判断目录不存在就创建 yzfy 目录创建代码没有，补上。
		if (!toPathFile.exists() || !toPathFile.isDirectory()) {
			Boolean bool = toPathFile.mkdirs();
			if (bool)
				log.info("创建目录" + toPathFile.getAbsolutePath() + "成功!");
			else
				log.error("创建目录" + toPathFile.getAbsolutePath() + "失败!");
		}

		// 扫描目录读取文件
		File fromPath = chkFile.getParentFile();
		if (fromPath == null)
			return false;
		String[] fileNames = fromPath.list(new FileNameFilter(chkFile.getName().substring(0, chkFile.getName().indexOf(".")), null));
		boolean flag = false;
		for (String f : fileNames) {
			// 修改需求：当文件移动不成功是10次数的继续移动，直到成功
			synchronized (f) {
				int n = 0;
				do {
					flag = moveFile(fromPath.getAbsolutePath() + File.separatorChar + f, toPathFile.getAbsolutePath());
					n++;
					if (n > 10) {
						log.error("文件移动失败" + fromPath.getAbsolutePath() + File.separatorChar + f + "toPath:" + toPathFile.getAbsolutePath());
						break;
					}
				} while (!flag && n <= 10);
			}
		}
		return flag;
	}
}
