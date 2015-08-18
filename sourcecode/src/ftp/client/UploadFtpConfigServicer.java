package ftp.client;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cusi.ods.dtp.common.WebConfigInfUtil;
import com.cusi.ods.dtp.conf.SysConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UploadFtpConfigServicer {

	private static UploadFtpConfigServicer uploadFtpConfigServicer;
	private Map<String, UploadFtpConfig> mapContent;

	private UploadFtpConfigServicer() {

	}

	public static synchronized UploadFtpConfigServicer getInstance() {
		if (uploadFtpConfigServicer == null) {
			uploadFtpConfigServicer = new UploadFtpConfigServicer();
			// init();//此方法大量访问数据库，时间较长，易导致ftp断线。改为在此类加载时即执行。
		}
		return uploadFtpConfigServicer;
	}

	/**
	 * 初始化加载本省所有的接口单元的二次上传信息
	 * @throws Exception 
	 */
	public void refresh()throws IOException {
		Map<String, UploadFtpConfig> map = new HashMap<String, UploadFtpConfig>();
		String provId =  SysConf.getString("provinceCode");
		String parameters = "uploadFtpConfigs?provCode="+provId;
		String jsonString = WebConfigInfUtil.getConfigInf(SysConfig.FTPSERVER_ADDR,parameters);
		Type type= new TypeToken<List<UploadFtpConfig>>(){}.getType();
		Gson gson= new Gson();
		List<UploadFtpConfig> list = gson.fromJson(jsonString,type);
		for(UploadFtpConfig a : list){
			map.put(a.getInterfaceDef().getFileNamePattern(), a);
		}
		synchronized(this){
			mapContent = map;
		}
	}

	/**
	 * 得到指定接口单元的二次上传信息对象
	 * 
	 * @param keyValue 此一般为一个ＣＨＫ文件名
	 * @return
	 */
	public synchronized UploadFtpConfig getUploadFtpConfig(String keyValue) {
		UploadFtpConfig uploadFtpConfig = null;

		byte[] new_keyValue = keyValue.getBytes();
		String new_keyValue_str = "";

		Iterator<String> it = mapContent.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			if (keyValue.startsWith(key)) {
				uploadFtpConfig = mapContent.get(key);
				break;
			}
		}
		
		if (uploadFtpConfig == null) { //如果第一次查找没有找到，则改名再查
			it = mapContent.keySet().iterator();
			// 针对全转增生成的增量的接口文件,可能并没有配置接口单元，如果查不到可以在对应的日全量接口中查
			if (new_keyValue[5] == 'A') {// 如果是日增量
				new_keyValue[5] = 'D';// 将增量文件名改成对应的全量接口文件名
				new_keyValue_str = new_keyValue.toString();
				while (it.hasNext()) {
					String key = it.next();
					if (new_keyValue_str.startsWith(key)) {
						uploadFtpConfig = mapContent.get(key);
						break;
					}
				}
			}
			// 针对增量的接口文件，如果还查不到可以在对应的月全量接口中查一下
			if (new_keyValue[5] == 'M') {// 如果是月增量
				new_keyValue[5] = 'N';// 将增量文件名改成对应的全量接口文件名
				new_keyValue_str = new_keyValue.toString();
				while (it.hasNext()) {
					String key = it.next();
					if (new_keyValue_str.startsWith(key)) {
						uploadFtpConfig = mapContent.get(key);
						break;
					}
				}

			}
		}
		return uploadFtpConfig;
	}
}
