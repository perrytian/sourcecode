package zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ConnectedWatcher implements Watcher {
	private static Logger logger = Logger.getLogger(ConnectedWatcher.class);
	
    private CountDownLatch connectedLatch;

    public ConnectedWatcher(CountDownLatch connectedLatch) {
        this.connectedLatch = connectedLatch;
    }

    @Override
    public void process(WatchedEvent event) {
    	logger.debug("监听ZooKeeper事件：事件路径"+event.getPath()+" 事件类型："+event.getType());
        if (event.getState() == KeeperState.SyncConnected) {
        	logger.info("连接ZooKeeper成功......");
            connectedLatch.countDown();
        }
    }
}