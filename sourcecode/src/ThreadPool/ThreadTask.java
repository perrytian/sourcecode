package ThreadPool;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;


/**
 * 
 * @author Jianghao
 *
 */
public class ThreadTask{
	private static Logger logger = Logger.getLogger(ThreadTask.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public ThreadTask(){
		
	}
	
	public void execute() throws SQLException{
		logger.info("执行任务........");
		
	}
	
}
