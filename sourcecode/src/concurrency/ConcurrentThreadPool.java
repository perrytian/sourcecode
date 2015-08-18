package concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConcurrentThreadPool extends ThreadPoolExecutor{
	
	public static final int CORE_POOL_SIZE = 10;
	public static final int MAX_POOL_SIZE = 20;
	public static final long KEEP_ALIVE_TIME = 20;
	
	
	public ConcurrentThreadPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	//执行任务之前
	@Override
	protected void beforeExecute(Thread t,Runnable r){
		super.beforeExecute(t, r);
		System.out.println("执行任务之前");
	}
	
	//执行任务之后
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		System.out.println("执行任务之后");
	}
	
	//执行任务完成，需要执行关闭操作才会调用这个方法
	@Override
	protected void terminated(){
		super.terminated();
		System.out.println("执行任务完成");
	}
	
	public static void main(String[] args){
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();//没有大小限制
		
		 /** 
	     * Param:  
	     * corePoolSize - 池中所保存的线程数，包括空闲线程。  
	     * maximumPoolSize - 池中允许的最大线程数(采用LinkedBlockingQueue时没有作用)。  
	     * keepAliveTime -当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间，线程池维护线程所允许的空闲时间。  
	     * unit - keepAliveTime参数的时间单位，线程池维护线程所允许的空闲时间的单位:秒 。  
	     * workQueue - 执行前用于保持任务的队列（缓冲队列）。此队列仅保持由execute 方法提交的 Runnable 任务。  
	     * RejectedExecutionHandler -线程池对拒绝任务的处理策略(重试添加当前的任务，自动重复调用execute()方法) 
	     */  
		ConcurrentThreadPool executor = new ConcurrentThreadPool(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, queue);
		
		/**
		ThreadPoolExecutor 提供 4 个现有的策略，分别是：
		ThreadPoolExecutor.AbortPolicy：表示拒绝任务并抛出异常
     	ThreadPoolExecutor.DiscardPolicy：表示拒绝任务但不做任何动作
		ThreadPoolExecutor.CallerRunsPolicy：表示拒绝任务，并在调用者的线程中直接执行该任务
		ThreadPoolExecutor.DiscardOldestPolicy：表示先丢弃任务队列中的第一个任务，然后把这个任务加进队列。
		*/
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		
		
		ThreadPoolMonitor monitor = new ThreadPoolMonitor(executor,3);
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();
		
		for(int i=0;i<20;i++){
			ConcurrentThreadTask task = new ConcurrentThreadTask();
			executor.execute(task);
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();//不会阻塞，调用此方法后，主线程马上结束，而线程池会继续运行直到所有任务执行完才会停止
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		monitor.shutdown();
	}

}
