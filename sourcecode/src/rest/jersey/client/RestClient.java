package rest.jersey.client;

import java.util.Date;
import javax.ws.rs.core.UriBuilder;
import org.apache.log4j.Logger;

/**
 * 测试rest服务类
 * @author xs
 */
public class RestClient {
	
	private static final Logger log = Logger.getLogger(RestClient.class);
	
	private ClientConfig config;
	private Client client ;
	private WebResource service ;
	
	public RestClient(){
		config = new DefaultClientConfig();
		client = Client.create(config);
		//操作日志
//		service = client.resource(UriBuilder.fromUri("http://localhost:8080/cbss_rest/rest/service").build());//要请求的url路径
		//性能监控
		service = client.resource(UriBuilder.fromUri("http://localhost:8080/cbss_rest/rest/performance").build());//要请求的url路径
	}
	
	public String restClient(CbssHdapPerformance entity){
		String json = JSONUtils.getJsonString4JavaPOJO(entity);//将对象转换成json格式字符串
		log.info("---------json-----------:" + json);
		ClientResponse response = service.put(ClientResponse.class,json);
		log.info("-------response status-------" + response.getStatus() );
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		String result = response.getEntity(String.class);
		return result;
	}
	
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadTest test = new ThreadTest();
		Thread th1 = new Thread(test);
		th1.setName("1");
		Thread th2 = new Thread(test);
		th2.setName("2");
		Thread th3 = new Thread(test);
		th3.setName("3");
		th1.start();
		th2.start();
		th3.start();
	}
}

class ThreadTest extends Thread {

	public ThreadTest() {
	}

	@Override
	public void run() {
		RestClient clientDtp = new RestClient();
		long beforeTime = System.currentTimeMillis();
		//=============================================================== 保存操作测试数据
//		CbssExecuteLog entity = new CbssExecuteLog();
//		entity.setProvinceID("000");
//		entity.setFileName("BB000N091512013012700000001001.AVL");
//		entity.setAccount("20131120");
//		entity.setTableName("test");
//		entity.setStartTime(DateUtil.DatetoString(new Date()));
//		entity.setEndTime(DateUtil.DatetoString(new Date()));
//		entity.setOperType(1);
//		entity.setPreNum(2548);
//		entity.setPostNum(2548);
//		entity.setDestIP("10.0.210.151");
//		entity.setSourceIP("10.0.210.154");
//		entity.setErrorCode("0000");
//		entity.setErrorCont("");
//		entity.setInsertTime(DateUtil.DatetoString(new Date()));
//		entity.setRemark("");
		
		CbssHdapPerformance entity=new CbssHdapPerformance();
		entity.setStartTime(DateUtil.DatetoString(new Date()));
		entity.setEndTime(DateUtil.DatetoString(new Date()));
		entity.setQueryCount(26);
		entity.setErrorCount(4);
		entity.setAvgDuration(98);
		entity.setMaxDuration(1025);
		entity.setMaxDurationCount(0);
		entity.setMinDuration(8);
		entity.setMinDurationCount(1);
		entity.setHostIP("10.0.210.153");
		entity.setTotalCount(51);
		
		for(int i=0; i < 1; i++){
			System.out.println("------线程名称----：" + Thread.currentThread().getName() + "----:第"+ i +"次");
			String result = clientDtp.restClient(entity);
			System.out.println(result);
		}
		long afterTime = System.currentTimeMillis();
		System.out.println("本次批量处理花费的时间是：" + ((afterTime - beforeTime) / 1000)+ "m");
	}
}
