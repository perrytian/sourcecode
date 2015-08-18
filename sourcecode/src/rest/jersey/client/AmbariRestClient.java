package rest.jersey.client;

import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.cusi.hods.monitor.ambari.model.ClustInfo;
import com.cusi.hods.monitor.ambari.model.ComponentInfo;
import com.cusi.hods.monitor.ambari.model.Components;
import com.cusi.hods.monitor.ambari.model.HostComponentDetail;
import com.cusi.hods.monitor.ambari.model.HostComponents;
import com.cusi.hods.monitor.ambari.model.HostEntity;
import com.cusi.hods.monitor.ambari.model.Hosts;
import com.cusi.hods.monitor.ambari.model.HostsInfo;
import com.cusi.hods.monitor.ambari.model.ServiceComponentInfo;
import com.cusi.hods.monitor.ambari.model.ServiceComponents;
import com.cusi.hods.monitor.ambari.model.ServiceEntity;
import com.cusi.hods.monitor.ambari.model.ServiceInfo;
import com.cusi.hods.monitor.util.ConfigUtil;
import com.cusi.hods.monitor.util.JSONUtils;

public class AmbariRestClient {
	
	private static final Logger log = Logger.getLogger(AmbariRestClient.class);
	private HttpClient client = null;
	private Properties properties = ConfigUtil.getProperties("ambari.properties");
	
	public AmbariRestClient(){
		auth();
	}
	
	/**
	 * 身份认证
	 */
	public void auth(){
		client = new HttpClient();
		String hostIP = properties.getProperty("hostIP");
		int hostPort = Integer.parseInt(properties.getProperty("hostPort"));
		String authName = properties.getProperty("authName");
		String authPwd = properties.getProperty("authPwd");
		//身份认证
		client.getState().setCredentials(
				new AuthScope(hostIP, hostPort,AuthScope.ANY_REALM),
				new UsernamePasswordCredentials(authName, authPwd));
		client.getParams().setAuthenticationPreemptive(true);
	}
	
	/**
	 * 获取所有服务名称
	 * @param clusterName
	 */
	public List<ClustInfo> getAllClusterService(String clusterName) throws Exception{
		GetMethod method = new GetMethod(properties.getProperty("url")+ "api/v1/clusters/"+ clusterName +"/services");
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			String json = method.getResponseBodyAsString();
			// log.info("返回的json数据：" + json);
			if(status == 200){
				ServiceInfo service = (ServiceInfo)JSONUtils.jsonStrToObject(json,ServiceInfo.class);
				return service.getItems();
			}else{
				throw new RuntimeException("Failed : HTTP error code : "+ status);
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 查看指定服务名称的服务状态
	 * eg:INIT、INSTALLING、INSTALL_FAILED、INSTALLED,STARTING、STARTED、
	 * 	   STOPPING、UNINSTALLING、UNINSTALLED、WIPING_OUT、UPGRADING、MAINTENANCE、UNKNOWN
	 * @param clusterName
	 * @param serviceName
	 * @return
	 */
	public ServiceEntity getServiceInfoByName(String clusterName,String serviceName) throws Exception{
		GetMethod method = new GetMethod(properties.getProperty("url")+ "api/v1/clusters/"+ clusterName +"/services/" + serviceName);
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			String json = method.getResponseBodyAsString();
			//log.info("返回的json数据：" + json);
			if(status == 200){
				ClustInfo service = (ClustInfo)JSONUtils.jsonStrToObject(json,ClustInfo.class);
				return service.getServiceInfo();
			}else{
				throw new RuntimeException("Failed : HTTP error code : "+ status);
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	
	/**
	 * 查看指定主机的状态:"HEALTHY" or "UNHEALTHY"
	 * @param clustName 集群名称 (eg:hods)
	 * @param hostName 主机名称 (eg:node4.hods)
	 */
	public HostEntity getHostInfo(String clustName,String hostName) throws Exception{
		//http://132.35.224.107:8088/api/v1/clusters/hods/hosts/node4.hods?fields=Hosts/host_state
		
		//curl --User admin:admin http://10.0.210.146:8080/api/v1/clusters/cbsscluster/hosts?fields=Hosts/host_status
			
		GetMethod method = new GetMethod(properties.getProperty("url") + "api/v1/clusters/"+ clustName +"/hosts/"+hostName + "?fields=Hosts/host_state");
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			String json = method.getResponseBodyAsString();
			log.info("返回的json数据：" + json);
			if(status == 200){
				HostsInfo info = (HostsInfo)JSONUtils.jsonStrToObject(json,HostsInfo.class);
				System.out.println(info.getHref());
				return info.getHosts();
			}else{
				throw new RuntimeException("Failed : HTTP error code : "+ status);
			}
		}finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 获取集群下所有主机状态信息
	 * @param clustName
	 * @return
	 * @throws Exception
	 */
	public List<HostsInfo> getAllHostInfo(String clustName) throws Exception{
		GetMethod method = new GetMethod(properties.getProperty("url") + "api/v1/clusters/"+ clustName +"/hosts?fields=Hosts/host_status");
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			String json = method.getResponseBodyAsString();
//			log.info("返回的json数据：" + json);
			if(status == 200){
				Hosts info = (Hosts)JSONUtils.jsonStrToObject(json,Hosts.class);
				return info.getItems();
			}else{
				throw new RuntimeException("Failed : HTTP error code : "+ status);
			}
		}finally {
			method.releaseConnection();
		}
	}
	
	
	/**
	 * 获取指定主机所有组件状态
	 * eg:http://10.*.4.*:8080/api/v1/clusters/hods/hosts/node4.hods/host_components
	 * @param clustName
	 * @param hostName
	 * @throws Exception
	 */
	public List<Components> getHostComponentsInfo(String clustName,String hostName) throws Exception{
		GetMethod method = new GetMethod(properties.getProperty("url") + "api/v1/clusters/"+ clustName +"/hosts/"+ hostName+"/host_components?fields=ServiceComponentInfo/state");
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			String json = method.getResponseBodyAsString();
//			log.info("返回的json数据：" + json);
			if(status == 200){
				HostComponents comps = (HostComponents)JSONUtils.jsonStrToObject(json,HostComponents.class);
				return comps.getItems();
			}else{
				throw new RuntimeException("Failed : HTTP error code : "+ status);
			}
		}finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 获取指定主机下的指定组件的服务状态
	 * eg:http://*.*.*.*:8080/api/v1/clusters/hods/hosts/node4.hods/host_components/DATANODE?fields=HostRoles/state
	 * @param clustName 集群名称
	 * @param hostName 主机名称
	 * @param compName 组件名称
	 * @throws Exception
	 * 
	 */
	public ComponentInfo getComponentsEntity(String clustName,String hostName,String compName) throws Exception{
		GetMethod method = new GetMethod(properties.getProperty("url") + "api/v1/clusters/"+ clustName +"/hosts/"+ hostName+"/host_components/"+ compName +"?fields=HostRoles/state");
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			String json = method.getResponseBodyAsString();
//			log.info("返回的json数据：" + json);
			if(status == 200){
				HostComponentDetail comp = (HostComponentDetail)JSONUtils.jsonStrToObject(json,HostComponentDetail.class);
				return comp.getHostRoles();
			}else{
				throw new RuntimeException("Failed : HTTP error code : "+ status);
			}
		}finally {
			method.releaseConnection();
		}
	}
	
	
	/**
	 * 获取指定服务下的组件名称
	 * @param hostName
	 */
	public List<ServiceComponentInfo> getServiceComponents(String clustName,String serviceName) throws Exception{
		GetMethod method = new GetMethod(properties.getProperty("url") + "api/v1/clusters/"+ clustName +"/services/"+serviceName+"/components?fields=ServiceComponentInfo/state");
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			String json = method.getResponseBodyAsString();
//			log.info("返回的json数据：" + json);
			if(status == 200){
				ServiceComponents sercominfo = (ServiceComponents)JSONUtils.jsonStrToObject(json,ServiceComponents.class);
				return sercominfo.getItems();
			}else{
				throw new RuntimeException("Failed : HTTP error code : "+ status);
			}
		}finally {
			method.releaseConnection();
		}
	}
	//调试使用
	public void getHosts(String hostName){///clusters/c1/services/
//		GetMethod method = new GetMethod("http://132.35.224.107:8088/api/v1/clusters/hods/hosts/node4.hods?fields=Hosts/host_state");
//		GetMethod method = new GetMethod("http://10.161.4.111:8100/api/v1/clusters/hods/hosts/node4.hods/host_components/DATANODE?fields=HostRoles/state");
//		GetMethod method = new GetMethod("http://10.161.4.111:8100/api/v1/clusters/hods/hosts/node4.hods/host_components?fields=ServiceComponentInfo/state");
		GetMethod method = new GetMethod("http://10.161.4.111:8100/api/v1/clusters/hods/services/HDFS/components?fields=ServiceComponentInfo/state");
//		GetMethod method = new GetMethod("http://10.161.4.111:8100/api/v1/clusters/hods/hosts?fields=Hosts/host_status");
//		GetMethod method = new GetMethod("http://10.161.4.111:8100/api/v1/clusters/hods/services/HDFS/components");
//		GetMethod method = new GetMethod("http://132.35.224.107:8088/api/v1/clusters/hods/services/HDFS/components/DATANODE");
//		GetMethod method = new GetMethod("http://10.161.4.111:8100/api/v1/clusters/hods/services/HBASE");
//		GetMethod method = new GetMethod("http://10.0.210.146:8080/api/v1/clusters/cbsscluster/services/HBASE");
//		DeleteMethod method = new DeleteMethod("http://10.0.210.146:8080/api/v1/clusters/cbsscluster/hosts/node2.hadoop/host_components");///ZKFC
		method.setDoAuthentication(true);
		try {
			int status = client.executeMethod(method);
			System.out.println("---------------------:" + status + "--------/n"
					+ method.getResponseBodyAsString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}
	
	public static void main(String[] args) {
		AmbariRestClient client1 = new AmbariRestClient();
		client1.getHosts("hods");//node4.hods//inf1.hods
		
		try {
//			List<Components> list = client1.getHostComponentsInfo("hods", "node4.hods");
//			System.out.println(list.size());
//			for(int i=0;i<list.size();i++){
//				System.out.println(list.get(i).getHostRoles().getComponent_name());
//				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
