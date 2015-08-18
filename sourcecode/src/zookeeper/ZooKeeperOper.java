package zookeeper;

import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.Map;

public interface ZooKeeperOper {
	public void connect();
	public void createConfigItem(String prov,String eparchy,String itemID,String itemValue) throws KeeperException, InterruptedException ;
	public void updateConfigItem(String prov,String eparchy,String itemID,String itemValue)  throws KeeperException, InterruptedException ;
	public void deleteConfigItem(String prov,String eparchy,String itemID) throws InterruptedException, KeeperException;
	
	public String getConfigItemValue(String prov,String eparchy,String itemID)  throws Exception ;
	
	public  Map<String,String> getAllConfigItem()  throws KeeperException, InterruptedException ;

}
