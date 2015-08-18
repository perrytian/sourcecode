package ThreadPool;

import java.lang.Thread.State;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class ThreadPool {
	
	private static Logger logger = Logger.getLogger(ThreadPool.class);
	
	private WorkThread[] works;
	private BlockingQueue<ThreadTask> taskQueue;
	private int poolSize;
	
	public ThreadPool(){
		/*
		ConfigManager config = new ConfigManager();
		//如果设置了任务队列大小，设置队列为有限大小，如果配置文件中没有设置，不设置大小
		if(config.getQueueSize() > 0){
			taskQueue = new ArrayBlockingQueue<ThreadTask>(config.getQueueSize());
		}else{
			taskQueue = new LinkedBlockingQueue<ThreadTask>();
		}
		
		this.poolSize = config.getMaxPoolSize();
		works = new WorkThread[poolSize];
		for(int index = 0;index < poolSize;index++){
			works[index] = new WorkThread(taskQueue,"WorkThread_"+String.valueOf(index));
		}*/
	}
	
	public void start(){
		
		for(int index = 0;index < poolSize;index++){
			works[index].start();
		}
		logger.info("线程池启动：初始线程数["+poolSize+"]");
	}
	
	public WorkThread[] getThreads(){
		return works;
	}
	
	public void getThreadState(){
		int totalSize = getThreads().length;
		StringBuffer output = new StringBuffer();
		output.append("\n##########线程池信息##############\n");
		output.append("线程数："+totalSize+"\n");
		for(WorkThread thread:getThreads()){
			output.append("线程：");
			output.append(thread.getThread().getName()+" ");
			State state = thread.getThread().getState();
			if(state.equals(State.NEW)){
				output.append("尚未启动\n");
			}else if(state.equals(State.BLOCKED)){
				output.append("受阻塞\n");
			}else if(state.equals(State.RUNNABLE)){
				output.append("正在执行\n");
			}else if(state.equals(State.TERMINATED)){
				output.append("已退出\n");
			}else if(state.equals(State.TIMED_WAITING)){
				output.append("等待另一个线程来执行,有限等待\n");
			}else if(state.equals(State.WAITING)){
				output.append("等待另一个线程执行,无限等待\n");
			}
		}
		output.append("##################################\n");
		logger.info(output.toString());
	}
	
	public void addTask(ThreadTask task){
		try{
			taskQueue.add(task);
			logger.debug( "############添加任务到队列：任务队列大小["+taskQueue.size()+"]#######");
			
			if(taskQueue.size() > 500){
				getThreadState();
			}
		}catch(java.lang.IllegalStateException ex){
			if(ex.getLocalizedMessage().trim().equals("Queue full")){
				logger.error("向线程池添加处理任务:队列已满size["+taskQueue.size()+"]");
			}else{
				ex.printStackTrace();
			}
		}catch(Exception ex){
			logger.error("向线程池添加处理任务出错size["+taskQueue.size()+"]:"+ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}
	
	public synchronized void stop(){
		for(int index = 0;index < poolSize;index++){
			works[index].stop();
			works[index] = null;
		}
		taskQueue.clear();
	}
	
	public static void main(String[] args){
		ThreadPool pool = new ThreadPool();
		pool.start();
		for(int index = 0;index<10;index++){
			ThreadTask task = new ThreadTask();
			pool.addTask(task);
		}
	}

}
