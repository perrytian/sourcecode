package socket;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.cusi.ods.hdap.config.ConfigManager;

/**
 * 线程池
 * @author jianghao
 *
 */
public class ProcessorEngine {
	
	private static Logger logger = Logger.getLogger(ProcessorEngine.class);
	
	private WorkThread[] works;
	private BlockingQueue<ConnectionReference> taskQueue;//请求消息队列
	private int poolSize;
	private int queueSize;
	
	
	public ProcessorEngine(){
		queueSize = ConfigManager.getInstance().getQueuesize();
		//如果设置了任务队列大小，设置队列为有限大小，如果配置文件中没有设置，不设置大小
		if(queueSize > 0){
			taskQueue = new ArrayBlockingQueue<ConnectionReference>(queueSize);
		}else{
			taskQueue = new LinkedBlockingQueue<ConnectionReference>();
		}
		
		this.poolSize = ConfigManager.getInstance().getThreadpoolsize();
		works = new WorkThread[poolSize];
		for(int index = 0;index < poolSize;index++){
			works[index] = new WorkThread(taskQueue,"WorkThread_"+String.valueOf(index));
		}
	}
	
	public void start(){
		
		for(int index = 0;index < poolSize;index++){
			works[index].start();
		}
		logger.info("HDAP消息处理引擎启动：初始线程数["+poolSize+"],队列大小设置为["+queueSize+"]");
	}
	
	public void addTask(ConnectionReference task){
		try{
			taskQueue.add(task);
			if(logger.isDebugEnabled()){
				logger.debug("查询请求["+task.hashCode()+"]加入队列：当前排队的查询请求数["+taskQueue.size()+"]");
			}
		}catch(java.lang.IllegalStateException ex){
			if(ex.getLocalizedMessage().trim().equals("Queue full")){
				logger.error("向线程池添加处理任务:队列已满size["+taskQueue.size()+"]");
			}else{
				ex.printStackTrace();
			}
		}catch(Exception ex){
			logger.error("向线程池添加处理任务出错size["+taskQueue.size()+"]:"+ex.getLocalizedMessage());
		}
	}
	
	public  void stop(){
		for(int index = 0;index < poolSize;index++){
			works[index].stop();
			works[index] = null;
		}
		taskQueue.clear();
	}
	

}
