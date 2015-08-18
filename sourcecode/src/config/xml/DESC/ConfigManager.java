package config.xml.DESC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.apache.log4j.Logger;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 配置管理类
 * 
 * @author 姜昊
 * 
 */
public class ConfigManager {

	private static final Logger logger = Logger.getLogger(ConfigManager.class);

	private String serverAddress = "127.0.0.1";// 提供服务的默认地址
	private int serverPort = -1;// 监听的默认端口
	private int threadpoolsize = 10;// 消息处理线程池大小
	private int queuesize = 65535;// 消息队列大小
	private String encode = null;// 返回数据的编码
	private String localname = "";// 本地进程名称
	private String reciever = "";// 心跳服务端地址和端口
	private int period = 10;// 心跳周期:默认10秒
	private long idleTime = 30000;
	private String url = "";
	private int interval = 3600;// 性能监控周期:默认3600秒
	private List<String> ips = new ArrayList<String>();

	private static ConfigManager configManager;

	private String configFile = "conf/config.xml";

	private ConfigManager() {
		parserXml(configFile);
	}

	public static synchronized ConfigManager getInstance() {
		if (configManager == null) {
			configManager = new ConfigManager();
		}
		return configManager;
	}

	private void parserXml(String fileName) {

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(fileName);
			Element element = document.getDocumentElement();
			localname = element.getAttribute("name");
			NodeList nodes = element.getChildNodes();
			StringBuffer output = new StringBuffer();
			output.append("\n解析配置文件:" + configFile);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("serveraddress")) {
					serverAddress = node.getTextContent();
					output.append("\n监听服务IP地址:" + serverAddress);
				} else if (node.getNodeName().equals("serverport")) {
					serverPort = Integer.parseInt(node.getTextContent());
					output.append("\n监听服务端口:" + serverPort);
				} else if (node.getNodeName().equals("threadpoolsize")) {
					threadpoolsize = Integer.parseInt(node.getTextContent());
					output.append("\n消息处理线程池大小:" + threadpoolsize);
				} else if (node.getNodeName().equals("queuesize")) {
					queuesize = Integer.parseInt(node.getTextContent());
					output.append("\n消息队列大小:" + queuesize);
				} else if (node.getNodeName().equals("encode")) {
					encode = node.getTextContent();
					output.append("\n编码:" + encode);
					if (encode.toLowerCase().equals("utf-8")) {
						encode = null;
					}
				} else if (node.getNodeName().equals("idle")) {
					idleTime = Long.parseLong(node.getTextContent());
					output.append("\n空闲时间:" + idleTime);
				} else if (node.getNodeName().equals("monitor")) {
					NodeList mns = node.getChildNodes();
					for (int index = 0; index < mns.getLength(); index++) {
						Node n = mns.item(index);
						if (n.getNodeName().equals("reciever")) {
							reciever = n.getTextContent();
							output.append("\n状态消息汇报地址:" + reciever);
						} else if (n.getNodeName().equals("period")) {
							period = Integer.parseInt(n.getTextContent());
							output.append("\n状态心跳周期:" + period + "秒");
						}
					}
				} else if (node.getNodeName().equals("rest")) {
					NodeList mns = node.getChildNodes();
					for (int index = 0; index < mns.getLength(); index++) {
						Node n = mns.item(index);
						if (n.getNodeName().equals("url")) {
							url = n.getTextContent();
							output.append("\nRest:" + url);
						} else if (n.getNodeName().equals("interval")) {
							interval = Integer.parseInt(n.getTextContent());
							output.append("\n性能监控周期:" + interval + "秒");
						}
					}
				} else if (node.getNodeName().equals("ipallowed")) {
					NodeList mns = node.getChildNodes();
					output.append("\n允许访问IP列表:");
					for (int index = 0; index < mns.getLength(); index++) {
						Node n = mns.item(index);
						if (n.getNodeName().equals("ip")) {
							ips.add(n.getTextContent());
							output.append(n.getTextContent() + " ");
						}
					}
				}
			}
			logger.info(output.toString());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getThreadpoolsize() {
		return threadpoolsize;
	}

	public void setThreadpoolsize(int threadpoolsize) {
		this.threadpoolsize = threadpoolsize;
	}

	public int getQueuesize() {
		return queuesize;
	}

	public void setQueuesize(int queuesize) {
		this.queuesize = queuesize;
	}

	public String getLocalname() {
		return localname;
	}

	public void setLocalname(String localname) {
		this.localname = localname;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getEncdoe() {
		return encode;
	}

	public long getIdleTime() {
		return idleTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<String> getIps() {
		return ips;
	}

	public void setIps(List<String> ips) {
		this.ips = ips;
	}
}
