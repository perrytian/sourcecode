package redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.ShardedJedisSentinelPool;

/**
 * 连接池工厂类
 * 
 * @author is
 * 
 */
public class PoolManager {

	private static PoolManager manager = null;
	private ShardedJedisSentinelPool pool = null;
	private List<String> masters = new ArrayList<String>();
	private Set<String> sentinels = new HashSet<String>();

	private PoolManager() {
		for (int i = 1; i < 3; i++) {
			masters.add("G" + i);
		}
		sentinels.add("192.168.1.12:26379");
		sentinels.add("192.168.1.13:26379");
	}
	
	public static PoolManager getInstance() {
		if (manager == null) {
			synchronized (PoolManager.class) {
				if (manager == null) {
					manager = new PoolManager();
				}
			}
		}
		return manager;
	}
	
	//得到连接池
	public ShardedJedisSentinelPool getConnectionPool() throws Exception {
		if (pool == null) {
			try {
				initialize();
			} catch (Exception e) {
				throw e;
			}
		}
		return pool;
	}

	// 初始化ShardedJedisSentinelPool
	private void initialize() throws Exception {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		try {
			pool = new ShardedJedisSentinelPool(masters, sentinels, config,
					60000);
		} catch (Exception e) {
			throw e;
		}
	}

	// 关闭连接池
	public void close() {
		if (pool != null)
			pool.destroy();
	}
}
