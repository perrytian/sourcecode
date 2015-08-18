package ftp.client;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cusi.ods.dtp.check.CheckFile;
import com.cusi.ods.dtp.common.FileNameUtil;
import com.cusi.ods.dtp.log.TranceforLogManager;
import com.cusi.ods.dtp.log.TranceforLogger;

import java.util.concurrent.PriorityBlockingQueue;

public class UploaderManager implements Runnable {

	private static final int WORK_THREAD_SIZE = 10;

	private static final Log log = LogFactory.getLog(UploaderManager.class);

	private int queueSize;
	private Thread[] threads;
	private BlockingQueue<UploadTask> queue;

	private Thread delayThread;
	private List<UploadTask> sleepTask = new ArrayList<UploadTask>();

	private static UploaderManager m_this = null;

	public static synchronized UploaderManager getMe() {
		if (m_this == null) {
			m_this = new UploaderManager();
		}
		return m_this;
	}

	private UploaderManager() {
		queueSize = SysConf.getInt("UploadFileQueueSize");
		queue = new PriorityBlockingQueue<UploadTask>(queueSize);
		log.debug("create uploadfile queue size:" + SysConf.getInt("UploadFileQueueSize"));

		delayThread = new Thread(this, "UploadErrorDelay");
		delayThread.start();

		threads = new Thread[WORK_THREAD_SIZE];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Uploader(this), "UploaderThread-" + i);
			threads[i].start();
		}
	}

	public boolean canBeAdd() {
		return queue.size() < queueSize;
	}

	public boolean addTask(UploadTask task) {
		// queue.put(task);
		boolean flag = false;
		if (queue.contains(task)) { // 同样的任务已经存在
			log.error("文件已经在上传队列中 文件名:" + task.getFieName());
		} else {
			flag = queue.add(task);// 此处改成add，可以减少队列满时的不良影响，比如前台再也传不上去文件 。。
			if (!flag) {
				log.error("上传队列满 无法添加文件:" + task.getFieName());
			} else {
				log.debug("添加到上传队列 文件:" + task.getFieName());
			}
		}
		return flag;
	}

	protected UploadTask getTask() throws InterruptedException {
		return queue.take();
	}

	protected void returnTask(UploadTask task, String msg) {
		if (task.getUploadCount() <= 20) {
			task.setUploadCount(task.getUploadCount() + 1);
			task.setDelayTime(task.getUploadCount() * 10);
			synchronized (sleepTask) {
				sleepTask.add(task);
			}
			log.error("发送文件出错，等待" + task.getDelayTime() + "秒进入重传线程队列时等待重传:" + task.getFieName());
		} else {
			log.error("重传次数超过25次，取消重传 FileName:" + task.getFieName());
			/*******************************************************************
			 * @author LiangKai 日志中添加参数：文件大小 2013/03/04 yzfy Log
			 */
			// 计算check文件内的文件数
			int fileCount = -1;
			if (FileNameUtil.isChkFile(task.getFieName())) {
				/*
				CheckFile chkTemp = new CheckFile(new File(task.getFieName()));
				if (chkTemp.isValid()) {
					fileCount = chkTemp.getFileCount();
				}*/
			}
			/**
			 * 
			TranceforLogger tLog = TranceforLogManager.createLog();
			// tLog.addLog(fileName, '9', beginTime, endTime, beginHost, host,
			// null, task.getUploadStatus() ? 1 : 0, 0, 0, null, fileCount,
			// file.length());
			com.cusi.ods.dtp.log.Log dmcQInterfacedata = new com.cusi.ods.dtp.log.Log();
			dmcQInterfacedata.setBeginHost(SysConf.getString("sys.alias"));
			dmcQInterfacedata.setEndHost(task.getFtpServer());
			dmcQInterfacedata.setErrorDesc(msg);
			dmcQInterfacedata.setAvlCount(fileCount);
			tLog.addLog(new File(task.getFieName()).getName(), "9", System.currentTimeMillis(), System.currentTimeMillis(), "0", 0, new File(task.getFieName()).length(), dmcQInterfacedata);
			tLog.send();
			 */

		}
	}

	
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				synchronized (sleepTask) {
					Iterator<UploadTask> i = sleepTask.iterator();
					while (i.hasNext()) {
						UploadTask task = i.next();
						int delayTime = task.getDelayTime();
						if (delayTime <= 0) {
							i.remove();
							addTask(task);
						} else {
							task.setDelayTime(delayTime - 1);
						}
					}
				}

			} catch (InterruptedException e) {
				log.error("sleep error", e);
			}
		}
	}
	
	public static void main(String[] args){
		UploaderManager uploader = new UploaderManager();
		Thread thread = new Thread(uploader);
		thread.start();
		UploadTask task = new UploadTask();
		task.setFieName("jianghaotest.txt");
		task.setFtpServer("127.0.0.1");
		task.setPort(2121);
		
		uploader.addTask(task);
	}
}
