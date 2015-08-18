package rest.jersey2.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.*;

import org.apache.log4j.Logger;

@Path("/user")
public class UserResource {
	
	private static final Logger logger=Logger.getLogger(UserResource.class);
	
	private static int numberOfSuccessResponses = 0;
	private static int numberOfFailures = 0;
	private static Throwable lastException = null;
	
	@GET
	@Path("/{param}")
	@Produces("application/json")
	public void asyncGet(@Suspended final AsyncResponse asyncResponse){
		logger.info("queryInfo()");
		asyncResponse.setTimeoutHandler(new TimeoutHandler(){
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
						.entity("Operation time out").build());
			}
		});
		
		asyncResponse.setTimeout(20, TimeUnit.SECONDS);
		
		asyncResponse.register(new CompletionCallback(){

			@Override
			public void onComplete(Throwable throwable) {
				if(throwable == null){
					numberOfSuccessResponses++;
				}else{
					numberOfFailures++;
					lastException = throwable;
				}
			}
			
		});
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				
				String result = queryUserInfo("18601105586");
				asyncResponse.resume(result);
			}
		}).start();
	}
	
	private String queryUserInfo(String serial_number) {
		logger.info("queryUserInfo()  param:"+serial_number);
		return "{'serial_number':'"+serial_number+"'}";
		/*
		String msisdn = "18650345575";
		Connection conn = null;
		Statement stmt = null;
		ResultSet res = null;
		String result = null;
		try {
			conn = JdbcUtils.getConnection();
			String query = " select "+"to_char(PARTITION_ID) ,to_char(USER_ID)  ,DUMMY_TAG  ,NET_TYPE_CODE  ,SERIAL_NUMBER , EPARCHY_CODE,"
         	   		+ "CITY_CODE,to_char(CUST_ID),to_char(USECUST_ID),BRAND_CODE,to_char(PRODUCT_ID),USER_TYPE_CODE,PREPAY_TAG  ,SERVICE_STATE_CODE,OPEN_MODE  ,"
         	   		+ "ACCT_TAG,REMOVE_TAG	,to_char(CREDIT_CLASS),to_char(BASE_CREDIT_VALUE),to_char(CREDIT_VALUE),to_char(CREDIT_CONTROL_ID)	,to_char(SCORE_VALUE),"
         	   		+ "UPDATE_DEPART_ID	,UPDATE_STAFF_ID	,USER_PASSWD,OPEN_DEPART_ID	,PROVINCE_CODE "
         	   		+ "from ucr_act1.tf_f_user where net_type_code = '50'  and serial_number ='"+msisdn+"'";

			logger.info("querySQL:"+query);
			
			stmt = conn.createStatement();
			res = stmt.executeQuery(query);
			List<User> userList = new ArrayList<User>();
			while(res != null && res.next()){
				User user = new User();
				user.setPartitionID(res.getString("to_char(PARTITION_ID)"));
				user.setUserID(res.getString("to_char(USER_ID)"));
				user.setDummyTag(res.getString("DUMMY_TAG"));
				user.setNetTypeCode(res.getString("NET_TYPE_CODE"));
				user.setSerialNumber(res.getString("SERIAL_NUMBER"));
				user.setEparchyCode(res.getString("EPARCHY_CODE"));
				user.setCityCode(res.getString("CITY_CODE"));
				user.setCustID(res.getString("to_char(CUST_ID)"));
				user.setUsecust_ID(res.getString("to_char(USECUST_ID)"));
				user.setBrandCode(res.getString("BRAND_CODE"));
				user.setProductID(res.getString("to_char(PRODUCT_ID)"));
				user.setUserTypeCode(res.getString("USER_TYPE_CODE"));
				user.setPrepayTag(res.getString("PREPAY_TAG"));
				user.setServiceStateCode(res.getString("SERVICE_STATE_CODE"));
				user.setOpenMode(res.getString("OPEN_MODE"));
				userList.add(user);
			}
			
			result = new Gson().toJson(userList);
			
			logger.info("RESULT:"+result);
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			JdbcUtils.release(conn, stmt, res);
		}*/
	}
	
	/*
	@GET
	@Path("userinfo")
	public String queryUserInfo(String msisdn) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String result = null;
		try {
			conn = JdbcUtils.getConnection();
			  
			String query = " select "+"to_char(PARTITION_ID) ,to_char(USER_ID)  ,DUMMY_TAG  ,NET_TYPE_CODE  ,SERIAL_NUMBER , EPARCHY_CODE,"
         	   		+ "CITY_CODE,to_char(CUST_ID),to_char(USECUST_ID),BRAND_CODE,to_char(PRODUCT_ID),USER_TYPE_CODE,PREPAY_TAG  ,SERVICE_STATE_CODE,OPEN_MODE  ,"
         	   		+ "ACCT_TAG,REMOVE_TAG	,to_char(CREDIT_CLASS),to_char(BASE_CREDIT_VALUE),to_char(CREDIT_VALUE),to_char(CREDIT_CONTROL_ID)	,to_char(SCORE_VALUE),"
         	   		+ "UPDATE_DEPART_ID	,UPDATE_STAFF_ID	,USER_PASSWD,OPEN_DEPART_ID	,PROVINCE_CODE "
         	   		+ "from ucr_act1.tf_f_user where net_type_code = '50'  and serial_number ='"+msisdn+"'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs != null && rs.next()){
				String partitionID = rs.getString("PARTITION_ID");
				String userID = rs.getString("USER_ID");
				String dummyTag = rs.getString("DUMMY_TAG");
				String netTypeCode = rs.getString("NET_TYPE_CODE");
				String serialNumber = rs.getString("SERIAL_NUMBER");
				String eparchyCode = rs.getString("EPARCHY_CODE");
				String cityCode = rs.getString("CITY_CODE");
				String custID = rs.getString("CUST_ID");
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			JdbcUtils.release(conn, stmt, rs);
		}
		
		
	}*/
}
