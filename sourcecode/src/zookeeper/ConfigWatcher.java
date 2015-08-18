package zookeeper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;


public class ConfigWatcher implements Watcher{
	private static Logger logger = Logger.getLogger(ConfigWatcher.class);
	private ZooKeeper zk;
	
	public ConfigWatcher(ZooKeeper zk){
		this.zk = zk;
	}

	@Override
	public void process(WatchedEvent event) {
		//监控所有被触发的事件
		//logger.info("[事件监听]监听ZooKeeper事件:事件路径"+event.getPath()+" 事件类型："+event.getType());
		if(event.getType() == EventType.NodeDataChanged ){
			byte[] data;
			try {
				data = zk.getData(event.getPath(), true, null);
				if(data == null){
					logger.info("[事件监听]ZooKeeper配置项数据值发生变化：配置信息："+event.getPath()+" 当前值:空");
				}else{
					logger.info("[事件监听]ZooKeeper配置项数据值发生变化，更新配置项：配置信息："+event.getPath()+" 当前值:"+new String(data));
					ConfigManager.setConfig(event.getPath(), new String(data));
				}
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if(event.getType() == EventType.NodeCreated){
			/*
			byte[] data;
			try {
				data = zk.getData(event.getPath(), true, null);
				if(data == null){
					logger.info("创建配置项数据：配置信息："+event.getPath()+" 当前值:空");
				}else{
					logger.info("ZooKeeper创建配置项，配置信息："+event.getPath()+" 当前值:"+new String(data));
					ConfigManager.setConfig(event.getPath(), new String(data));
				}
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}else if(event.getType() == EventType.NodeDeleted){			
			ConfigManager.deleteConfigValue(event.getPath());
			logger.info("[事件监听]ZooKeeper删除node节点数据，将该node数据从内存中删除，path="+event.getPath());
		}
		
		
		/*
				|| event.getType() == EventType.NodeDeleted
				|| event.getType() == EventType.NodeCreated){
			*/
	}

}
