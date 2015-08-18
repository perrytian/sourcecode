package ThreadPool;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class WorkThread implements Runnable{
	private static Logger logger = Logger.getLogger(WorkThread.class);
	
	private boolean isRunning = false;
	private BlockingQueue<ThreadTask> taskQueue;
	private Thread thread;
	private String name;
	
	public WorkThread(BlockingQueue<ThreadTask> taskQueue,String name){
		this.taskQueue = taskQueue;
		this.name = name;
	}
	
	public void start(){
		isRunning = true;
		thread = new Thread(this);
		thread.setName(name);
		thread.start();
	}
	
	public Thread getThread(){
		return thread;
	}
	
	public void stop(){
		if(isRunning){
			isRunning = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void run(){
		while(isRunning){
			ThreadTask task = null;
			try {
				try{
					task = taskQueue.take();
					logger.debug("#########执行任务：任务队列大小：["+taskQueue.size()+"]###########");
					task.execute(); 	
				} catch (SQLException e) {
					logger.error("线程执行任务失败：SQLException",e);
					rollBackTask(task);
				}catch (InterruptedException e) {
					logger.error("线程处理数据出错：" ,e);
				}catch(RuntimeException ex){
					logger.error("线程处理数据出错",ex);
				}catch (Exception ex) {
					logger.error("线程处理数据出错",ex);
				}
			}catch(Throwable ex){
				logger.error("线程执行任务失败：",ex);
			}
		}
	}
	
	/**
	 * 当执行任务出错时，执行此方法
	 * @param conn
	 * @param task
	 */
	public void rollBackTask(ThreadTask task){
		logger.warn("执行任务出错,将任务重新加入队列");
		if(task != null){
			try {
				Thread.sleep(100);
				if(taskQueue.add(task)){
					logger.info("盟市线程执行任务失败，将任务重新放入任务队列中等待处理: 当前队列任务数["+taskQueue.size()+"]......");
				}else{
					logger.warn("线程池任务队列已满,无法添加任务到队列中:当前队列任务数["+taskQueue.size()+"]");
				}
				
			} catch (IllegalStateException e2) {
				logger.error("线程池任务队列已满,无法添加任务到队列中:当前队列任务数["+taskQueue.size()+"]",e2);
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
	}
	
	public boolean isRunning(){
		return isRunning;
	}
}
