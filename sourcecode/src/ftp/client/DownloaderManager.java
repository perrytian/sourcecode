package ftp.client;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DownloaderManager {
	
	private Thread[] threads;
	private BlockingQueue<DownloadTask> queue;
	
	private static DownloaderManager m_this = null;
	
	public static synchronized DownloaderManager getMe(){
		if(m_this==null){
			m_this = new DownloaderManager();
		}
		return m_this;
	}
	
	private DownloaderManager(){
		queue = new LinkedBlockingQueue<DownloadTask>(100);
		threads = new Thread[20];
		for(int i=0;i<20;i++){
			threads[i] = new Thread(new Downloader(queue),"DownloaderThread-"+i);
			threads[i].start();
		}
	}
	
	public void addTask(DownloadTask task){
		try {
			queue.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}


