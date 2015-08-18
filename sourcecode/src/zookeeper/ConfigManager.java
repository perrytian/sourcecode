package zookeeper;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 配置管理
 * @author hh
 *
 */
public class ConfigManager{
	
	private static Logger logger = Logger.getLogger(ConfigManager.class);

	public static final String ZK_CONFIG_PATH = "/cdrmonitor/conf";
	private static Map<String,String> configMap = new HashMap<String,String>();
	
	public static String getConfigValue(String prov,String eparchy,String item){
		String itemPath = ZK_CONFIG_PATH+"/"+prov+"/"+eparchy+"/"+item;
		String value = getConfigValue(itemPath);
		if(value == null){
			//获取默认值
			String defaultPath = ZK_CONFIG_PATH+"/00/0000/"+item;
			value = getConfigValue(defaultPath);
			logger.debug("查询内存中配置数据,返回默认值:"+defaultPath+" 配置值:"+value);
			return value;
		}
		logger.debug("查询内存中配置数据:"+itemPath+" 配置值:"+value);
		return value;
	}
	
	public static void setConfig(String key,String value){
		configMap.put(key, value);
	}
	
	public static String getConfigValue(String key){
		return configMap.get(key);
	}
	public static void deleteConfigValue(String key){
		configMap.remove(key);
	}

}
