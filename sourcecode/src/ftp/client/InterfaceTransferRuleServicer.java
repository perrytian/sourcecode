package ftp.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cusi.ods.dtp.audit.MyScriptEngine;
import com.cusi.ods.dtp.common.WebConfigInfUtil;
import com.cusi.ods.dtp.entity.AuditRule;
import com.cusi.ods.dtp.entity.InterfaceDef;
import com.cusi.ods.dtp.entity.InterfaceTransferRule;

/**
 * 优化安全线程 mapContent
 * 
 * @author yzfy
 * 
 */
public class InterfaceTransferRuleServicer {

	private static final Log log = LogFactory.getLog(InterfaceTransferRuleServicer.class);
	private static InterfaceTransferRuleServicer interfaceTransferRuleServicer = new InterfaceTransferRuleServicer();
	private static Map<String, InterfaceTransferRule> mapContent = new HashMap<String, InterfaceTransferRule>();

	private InterfaceTransferRuleServicer() {

	}

	public static synchronized InterfaceTransferRuleServicer getInstance() {
		if (interfaceTransferRuleServicer == null) {
			interfaceTransferRuleServicer = new InterfaceTransferRuleServicer();
		}
		return interfaceTransferRuleServicer;
	}

	/**
	 * 初始化加载本机所有的接口单元的传输配置信息及稽核配置
	 * 
	 * @throws Exception
	 */
	public void refresh() throws Exception {
		Map<String, InterfaceTransferRule> mapContentConfig = new HashMap<String, InterfaceTransferRule>();
		//查询DMP获取接口传输规则列表
		List<InterfaceTransferRule> list = WebConfigInfUtil.getInstance().getInterfacetransByRest();
//		List<InterfaceTransferRule> list = this.initInterfaceTransferRule();
		if (null != list)
			for (InterfaceTransferRule interfaceTransferRule : list) {
				InterfaceDef interfaceDef = interfaceTransferRule.getInterfaceDef();
				if (null == interfaceDef) {
					log.warn("传输规则没有配置接口单元 id=" + interfaceTransferRule.getId());
					continue;
				}
				String fileNamePattern = interfaceDef.getFileNamePattern();
				if (null != fileNamePattern) {
					//log.debug("create interface map: "+fileNamePattern);
					mapContentConfig.put(fileNamePattern.trim(), interfaceTransferRule);//以接口文件名前缀作为Key
					List<AuditRule> rules = interfaceDef.getAuditRule();//获取稽核规则列表
					if (null != rules)
						for (int j = 0; j < rules.size(); j++) {
							AuditRule rule = rules.get(j);
							try {
								if (null != rule && null != rule.getRuleExp()) {
									rule.setExpression(MyScriptEngine.compile(rule.getRuleExp()));
								}
							} catch (Exception e) { // 出现编译异常，删除这个规则
								log.error("规则编译出错：" + rule.getRuleExp() + " InterfaceDefName:" + interfaceDef.getName() + ":" + e.getMessage());
							}
						}
				}
			}
		if (mapContentConfig.isEmpty()) {
			log.warn("加载传输规则为空！");
		} else {
			InterfaceTransferRuleServicer.mapContent = mapContentConfig;
		}
	}

	/**
	 * 得到指定接口单元的传输规则信息对象 yzfy 修改文件的接口单元获取方式
	 * 
	 * @param keyValue
	 *            此一般为一个ＣＨＫ文件名
	 * @return
	 */
	public InterfaceTransferRule getInterfaceTransferRule(String fileName) {
		// log.debug("得到指定接口单元的传输规则信息对象"+keyValue);
		InterfaceTransferRule interfaceTransferRule = null;
		fileName = fileName.replaceAll("\\\\", "/");
		int fileN = fileName.lastIndexOf("/");
		if (fileN != -1) {
			fileName = fileName.substring(fileN + 1);
		}
		byte[] new_keyValue = fileName.getBytes();
		String new_keyValue_str = "";
		Iterator<String> it = mapContent.keySet().iterator();
		while (it.hasNext()) {
			String fileNamePattern = it.next();
			if (fileName.contains(fileNamePattern)) {
				interfaceTransferRule = this.getInterfaceTransferRuleByMap(fileName, fileNamePattern);
				if (null != interfaceTransferRule)
					break;
			} else {
				if (new_keyValue.length <= 5)
					continue;
				// 针对全转增生成的增量的接口文件,可能并没有配置接口单元，如果查不到可以在对应的日全量接口中查
				if (new_keyValue[5] == 'A') {// 如果是日增量
					new_keyValue[5] = 'D';// 将增量文件名改成对应的全量接口文件名
					new_keyValue_str = new_keyValue.toString();
					if (new_keyValue_str.contains(fileNamePattern)) {
						interfaceTransferRule = this.getInterfaceTransferRuleByMap(fileName, fileNamePattern);
						if (null != interfaceTransferRule)
							break;
					}

					// 针对增量的接口文件，如果还查不到可以在对应的月全量接口中查一下
				} else if (new_keyValue[5] == 'M') {// 如果是月增量
					new_keyValue[5] = 'N';// 将增量文件名改成对应的全量接口文件名
					new_keyValue_str = new_keyValue.toString();
					if (new_keyValue_str.contains(fileNamePattern)) {
						interfaceTransferRule = this.getInterfaceTransferRuleByMap(fileName, fileNamePattern);
						if (null != interfaceTransferRule)
							break;
					}
				}
			}
		}
		if (null == interfaceTransferRule)
			log.warn("文件[" + fileName + "]没有配置接口单元");
		return interfaceTransferRule;

	}

	/**
	 * 
	 * @param fileName
	 * @param fileNamePatternByDB
	 * @return
	 */
	private InterfaceTransferRule getInterfaceTransferRuleByMap(String fileName, String fileNamePatternByDB) {

		InterfaceTransferRule interfaceTransferRule = mapContent.get(fileNamePatternByDB);
		InterfaceDef interfaceDef = interfaceTransferRule.getInterfaceDef();
		int start = interfaceDef.getInterfaceTagStart();
		int end = interfaceDef.getInterfaceTagEnd();
		if (start > 0 && end >= start && fileName.length() >= start) {
			if (fileNamePatternByDB.equalsIgnoreCase(fileName.substring(start - 1, end))) {

				return interfaceTransferRule;
			} else {
				log.warn(fileName + "接口单元配置接口位置不对" + fileNamePatternByDB + "开始位置：" + start + "结束位置" + end);
			}
		} else {
			log.warn(fileName + "接口单元配置接口位置不对" + fileNamePatternByDB + "开始位置：" + start + "结束位置" + end);
		}
		return null;
	}
	
	/*
	 private List<InterfaceTransferRule> initInterfaceTransferRule(){
		 List<InterfaceTransferRule> list = new ArrayList<InterfaceTransferRule>();
		 InterfaceTransferRule interfaceTransferRule = new InterfaceTransferRule();
		 InterfaceDef interfaceDef = new InterfaceDef();
		 interfaceTransferRule.setInterfaceDef(interfaceDef);
		 interfaceTransferRule.setIsAudit(0);
		 interfaceTransferRule.setIsCheck(1);
		 interfaceTransferRule.setIsCompress(0);
		 interfaceTransferRule.setNextTask(0);
		 interfaceTransferRule.setRemotePath("/data");
		 interfaceDef.setCheckFileLength(25);
		 interfaceDef.setDataStartLine(3);
		 interfaceDef.setDateFileLength(30);
		 interfaceDef.setFileNamePattern("B");
		 interfaceDef.setFileSuffix("*");
		 interfaceDef.setInterfaceTagStart(1);
		 interfaceDef.setInterfaceTagEnd(1);
		 interfaceDef.setTimeStringResource(2);
		 list.add(interfaceTransferRule);
	  return list;
	 }*/
}
