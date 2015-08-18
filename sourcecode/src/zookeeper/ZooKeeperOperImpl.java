package zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper.States;

public class ZooKeeperOperImpl implements ZooKeeperOper{
	
	private static Logger logger = Logger.getLogger(ZooKeeperOperImpl.class);
	private ZooKeeper zk;
	private final String zkHosts = "192.168.1.1:2181,192.168.1.2:2181";//19集群
	//private final String zkHosts = "10.0.210.154:2181,10.0.210.151:2181,10.0.210.152:2181";//146集群 
	
	public ZooKeeperOperImpl(){
		try {
			zk = new ZooKeeper(zkHosts,20000,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void connect(){
		waitUntilConnected(zk);
		zk.register(new ConfigWatcher(zk));
	}

	@Override
	public void createConfigItem(String prov, String eparchy, String itemID,String itemValue) throws KeeperException, InterruptedException {
		
		String path = ConfigManager.ZK_CONFIG_PATH+"/"+prov+"/"+eparchy+"/"+itemID;
		createZKPath(path);
		zk.setData(path, itemValue.getBytes(), -1);
		zk.exists(path, true);
	}

	@Override
	public void updateConfigItem(String prov, String eparchy, String itemID,String itemValue) throws KeeperException, InterruptedException {
		String path = ConfigManager.ZK_CONFIG_PATH+"/"+prov+"/"+eparchy+"/"+itemID;
		createZKPath(path);
		zk.setData(path, itemValue.getBytes(), -1);
		logger.info("更新ZooKeeper配置节点数据:节点["+path+"],当前值["+itemValue+"]");
		zk.exists(path, true);
	}

	@Override
	public void deleteConfigItem(String prov, String eparchy, String itemID) throws  KeeperException, InterruptedException {
		String path = ConfigManager.ZK_CONFIG_PATH+"/"+prov+"/"+eparchy+"/"+itemID;
		if(zk.exists(path, false) != null){
			logger.info("ZooKeeperOperImpl信息:node存在，将该节点删除，path="+path+",value="+new String(zk.getData(path, true, null)));
			zk.delete(path, -1);	
		}
		else{
			logger.info("ZooKeeperOperImpl信息:node不存在，path="+path);
		}
	}
	
	@Override
	public String getConfigItemValue(String prov, String eparchy, String itemID) throws Exception {
		String itemPath = ConfigManager.ZK_CONFIG_PATH+"/"+prov+"/"+eparchy+"/"+itemID;
		try {
			byte[] itemValue = zk.getData(itemPath, true, null);
			if(itemValue == null){
				throw new Exception("配置项节点数据为空:"+itemPath);
			}else{
				String configValue = new String(itemValue);
				logger.info("获取ZooKeeper配置节点数据:节点["+itemPath+"],当前值["+configValue+"]");
				return configValue;
			}
		} catch (KeeperException e) {
			logger.error("配置项节点不存在:"+itemPath,e);
			throw e;
		} 
	}

	
	
	@Override
	/**
	 * 初始化
	 */
	public Map<String,String> getAllConfigItem() throws KeeperException, InterruptedException {
		
			try {
				Map<String,String> allconfigitem = new HashMap<String,String>();
				List<String> allitemlist = getAllChildrenPath(ConfigManager.ZK_CONFIG_PATH);
				String key;
				String value;
				for(String iter:allitemlist){
					key = iter;
					String[] temp = key.substring(1).split("/");
					value = getConfigItemValue(temp[2], temp[3], temp[4]);
					allconfigitem.put(key, value);
				}
				return allconfigitem;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
	}
	
	private List<String> getChildrenPath(String path) throws KeeperException, InterruptedException{
		List<String> pathList = zk.getChildren(path, false);
		for(String iter:pathList){
			System.out.println(path);
			String nextPath = path +"/"+iter;
			List<String> subPath = getChildrenPath(nextPath);
			return subPath;
		}
		return null;
	}
	/**
	 * 求所有的子节点
	 * @param path
	 * @return
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private List<String> getAllChildrenPath(String path) throws KeeperException, InterruptedException{
		List<String> resultList = new ArrayList<String>();
		
		List<String> proList = zk.getChildren(path, false);//省份list

		for(String proviter:proList){//遍历省
			
			String provPath = path +"/"+proviter;	
			
			List<String> eparchyList = zk.getChildren(provPath, false);//当前省的所有市
			
			for(String eparchyiter:eparchyList){
				String eparchyPath = provPath +"/"+eparchyiter;	
				
				List<String> itemyList = zk.getChildren(eparchyPath, false);//当前市的所有item
				for(String itemiter:itemyList){
					String itenPath = eparchyPath + "/" +itemiter;
					resultList.add(itenPath);
				}	
			}
		}

		return resultList;
	}
	
	
	private void waitUntilConnected(ZooKeeper zooKeeper) {
        CountDownLatch connectedLatch = new CountDownLatch(1);
        Watcher watcher = new ConnectedWatcher(connectedLatch);
        zooKeeper.register(watcher);
        if (States.CONNECTING == zooKeeper.getState()) {
            try {
            	logger.info("正在尝试连接ZooKeeper:"+zkHosts);
                connectedLatch.await();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
	
	/**
	 * 创建ZooKeeper配置路径
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private void createZKPath(String path) throws KeeperException, InterruptedException{
		String[] nodeArray = path.substring(1).split("/");
		
		String pathToCreate = "";
		for(int index = 0;index<nodeArray.length;index++){
			pathToCreate+= "/"+nodeArray[index];
			
			if(zk.exists(pathToCreate, true) == null){
				zk.create(pathToCreate, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				logger.info("创建节点成功："+pathToCreate);
				zk.exists(pathToCreate, true);
			}else{
				logger.warn("节点已经存在："+pathToCreate);
			}
		}
	}
	

}
