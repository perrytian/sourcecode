package redis;

import java.util.List;

import redis.clients.jedis.Jedis;

public class RedisConnectThread implements Runnable {

	private Jedis sentinel = null;
	private Jedis client = null;
	private boolean isRunning = false;
	private Thread thread = null;

	public RedisConnectThread() {
		sentinel = new Jedis("192.168.1.10", 26379);
		sentinel.connect();
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			if (sentinel != null)
				sentinel.disconnect();
		}

	}

	public void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();

	}

	private void init() throws Exception {

		if (sentinel == null || (!sentinel.isConnected()))
			throw new Exception("初始化Sentinel失败!");

		List<String> master = sentinel.sentinelGetMasterAddrByName("G3");
		
		

		if (master == null || master.size() != 2)
			throw new Exception("获取master失败!");
		// master主机IP
		String host = master.get(0);
		// master监听端口
		int port = Integer.parseInt(master.get(1));
		client = new Jedis(host, port);
	}

	private void refresh() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		int key = 0;
		while (isRunning) {
			try {
				client.set(String.valueOf(key), "1");
				key++;
				if (key % 1000 == 0)
					System.out.println(key);
				Thread.sleep(1);
			} catch (Exception e) {
				System.out.println("挂掉时的key:" + key);
				try {
					Thread.sleep(1000);
					refresh();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		}

	}

}
