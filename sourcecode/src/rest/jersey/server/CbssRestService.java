package rest.jersey.server;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.apache.log4j.Logger;
import com.google.gson.Gson;

@Path("/")
public class CbssRestService {
	
	private static final Logger logger=Logger.getLogger(CbssRestService.class);
	private static Gson gson = null;
	static{
		gson = new Gson();
	}
	
	//操作日志
	@PUT
	@Path("service")
	public String doService(String json) {
		CbssExecuteLog executeLog=new CbssExecuteLog();
		CbssResponseEntity response=new CbssResponseEntity();
		CbssRestDao cbssRestDao=new CbssRestDaoImpl();
		logger.info("接收到的Json字符串:" + json);
		if(null==json || "".equals(json.trim())){
			response.setRet("1");
			response.setResultMsg("json字符串为空");
			logger.info("json字符串为空");
			return gson.toJson(response).toString();
		}
		
		String result = "";
		try{
			executeLog = (CbssExecuteLog) JSONUtils.jsonStrToObject(json,CbssExecuteLog.class);
			int res=cbssRestDao.saveExecuteLog(executeLog);
			if(res==1){//表示插入成功
				response.setRet("0");
				response.setResultMsg("操作日志新增成功");
				result = gson.toJson(response).toString();
				logger.info("操作日志新增成功");
			}else{
				response.setRet("1");
				response.setResultMsg("操作日志新增失败");
				result = gson.toJson(response).toString();
				logger.info("操作日志新增失败");
			}
		}catch(Exception e){
			response.setRet("1");
			response.setResultMsg("请求失败"+e.getMessage());
			logger.error("cbss接口请求失败",e);
			return gson.toJson(response).toString();
		}
		return result;
	}

	//性能监控
	@PUT
	@Path("performance")
	public String doPerformance(String json) {
		CbssHdapPerformance performance=new CbssHdapPerformance();
		CbssResponseEntity response=new CbssResponseEntity();
		CbssRestDao cbssRestDao=new CbssRestDaoImpl();
		logger.info("接收到的Json字符串:" + json);
		if(null==json || "".equals(json.trim())){
			response.setRet("1");
			response.setResultMsg("json字符串为空");
			logger.info("json字符串为空");
			return gson.toJson(response).toString();
		}
		String result = "";
		try{
			performance = (CbssHdapPerformance) JSONUtils.jsonStrToObject(json,CbssHdapPerformance.class);
			int res=cbssRestDao.savePerformance(performance);
			if(res==1){//表示插入成功
				response.setRet("0");
				response.setResultMsg("性能监控记录新增成功");
				result = gson.toJson(response).toString();
				logger.info("性能监控记录新增成功");
			}else{
				response.setRet("1");
				response.setResultMsg("性能监控记录新增失败");
				result = gson.toJson(response).toString();
				logger.info("性能监控记录新增失败");
			}
		}catch(Exception e){
			response.setRet("1");
			response.setResultMsg("请求失败"+e.getMessage());
			logger.error("cbss接口请求失败",e);
			return gson.toJson(response).toString();
		}
		return result;
	}
}
