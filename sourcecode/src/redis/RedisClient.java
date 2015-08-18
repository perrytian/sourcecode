package redis;

import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisSentinelPool;
import redis.clients.jedis.Tuple;

/**
 * Redis客户端
 * @author hh
 *
 */
public class RedisClient {
	private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
	
	private ShardedJedisSentinelPool pool = null;

	private ShardedJedisSentinelPool getPool(){
		try {
			pool = PoolManager.getInstance().getConnectionPool();
			return pool;
		} catch (Exception e) {
			logger.error("获取连接池失败",e);
			return null;
		}
	}
	
	/**
	 * 字符串类型——获取数据
	 * @param key
	 * @return 返回key的value。如果key不存在，返回特殊值nil。如果key的value不是string，就返回错误，因为GET只处理string类型的values。
	 */
	public String get(String key){
		String value = null;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			logger.error("jedis获取字符串失败", e);
			e.printStackTrace();
		}finally{
			//返还到连接池
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return value;
	}
	
	/**
	 * 判断Key是否存在
	 * @param key
	 * @return
	 */
	public boolean exists(String key){
		boolean ret = false;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 判断Key中field是否存在
	 * @param key field
	 * @return
	 */
	public boolean hexists(String key,String field){
		boolean ret = false;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 字符串类型——存放数据
	 * 将key和value对应。如果key已经存在了，它会被覆盖，而不管它是什么类型。
	 * @param key
	 * @param value
	 * @return 总是"OK"，因为SET不会失败。 
	 */
	public  String set(String key,String value){
		String ret = "";
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.set(key,value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 字符串类型——存放数据并获取原先值
	 * 自动将key对应到value并且返回原来key对应的value。如果key存在但是对应的value不是字符串，就返回错误
	 * @param key
	 * @param value
	 * @return
	 */
	public String getSet(String key,String value){
		String ret = "";
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.getSet(key,value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 哈希类型——读取哈希域的值
	 * @param key
	 * @param field
	 * @return 返回 key 指定的哈希集中该字段所关联的值
	 */
	public  String hget(String key,String field){
		String ret = "";
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 哈希类型——设置hash里面一个字段的值
	 * 设置 key 指定的哈希集中指定字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。如果字段在哈希集中存在，它将被重写。
	 * @param key
	 * @param field
	 * @param value
	 * @return 1:如果field是一个新的字段;0:如果field原来在map里面已经存在
	 */
	public  Long hset(String key,String field,String value){
		Long ret = 0L;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hset(key, field,value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 哈希类型——获取hash的所有字段
	 * @param key
	 * @return 返回 key 指定的哈希集中所有字段的名字。
	 */
	public  Set<String> hkeys(String key){
		Set<String> ret = null;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 哈希类型——获得hash的所有值
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key){
		List<String> ret = null;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	
	/**
	 * 哈希类型——迭代hash里面的元素
	 * @param key
	 * @param cursor
	 * @return
	 */
	public  ScanResult<Entry<String,String>> hscan(String key,String cursor){
		ScanResult<Entry<String,String>> ret = null;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hscan(key, cursor);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 哈希类型——获取hash里所有元素的数量
	 * @param key
	 * @return 哈希集中字段的数量，当 key 指定的哈希集不存在时返回 0
	 */
	public  Long hlen(String key){
		Long ret = null;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 哈希类型——从hash读取全部的域和值
	 * @param key
	 * @return 哈希集中字段和值的列表。当 key 指定的哈希集不存在时返回空列表。
	 */
	public Map<String, String> hgetAll(String key){
		Map<String, String> ret = null;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hgetAll(key);
		} catch (Exception e) {
			pool.returnResource(jedis);
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**	
	 * hdel(key, field)：删除hash中名称为key键域为field的值
	 * @param key
	 * @return If the field was present in the hash it is deleted and 1 is returned
	 */
	public Long hdel(String key,String field){
		Long ret = 0L;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hdel(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * 哈希类型——hincrBy//	hincrby(key, field, value)将名称为key的hash中field的value增加value
	 * @param key
	 * @return Integer reply The new value at field after the increment operation
	 */
	public Long hincrBy(String key,String field,long value){
		Long ret = 0L;
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ret = jedis.hincrBy(key, field,value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 设置带有过期时间的key,
	 * @param key,outTime
	 * @return 1 如果设置了过期时间;0 如果没有设置过期时间，或者不能设置过期时间
	 */
	
	public long expireAt(String key,long outTime){
		ShardedJedis jedis = null;
		long ret = 0L;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			for(int i=1; i <= 3 ;i++){
				ret = jedis.expireAt(key, outTime);
				if(ret==1){
					break;
				}
				Thread.sleep(2000*i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * 当 key 不存在时，返回 -2 。当 key 存在但没有设置剩余生存时间时，返回 -1 。否则，以秒为单位，返回 key 的剩余生存时间。
	 * @param key
	 * @return
	 */
	public long ttl(String key){
		ShardedJedis jedis = null;
		long ret = -3L;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			ret = jedis.ttl(key);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		
		return ret;
	}
	
	public long incrby(String key,long integer){
		ShardedJedis jedis = null;
		long ret = 0L;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			ret = jedis.incrBy(key,integer);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * zadd  
	 * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
	 * @param key score member
	 * @return 返回添加个数
	 */
	public long zadd(String key,double score,String member){
		ShardedJedis jedis = null;
		long ret = 0;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			ret = jedis.zadd(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * zscore
	 * 返回有序集 key 中，成员 member 的 score 值。
	 * @param key member
	 * @return score 
	 */
	public double zscore(String key, String member){
		ShardedJedis jedis = null;
		double value = 0;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			if(jedis.exists(key)){
				value = jedis.zscore(key, member);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return value;
	}
	
	/**
	 * zrem
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * @param key member
	 * @return 成员数量
	 */
	public long zrem(String key, String member){
		ShardedJedis jedis = null;
		long ret = 0;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			ret = jedis.zrem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * zincrby
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 * @param key score member
	 * @return score
	 */
	public double zincrby(String key,double score,String member){
		ShardedJedis jedis = null;
		double ret = 0;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			ret = jedis.zincrby(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return ret;
	}
	
	/**
	 * zrevrange
	 * 返回有序集 key 中，指定区间内的成员。
	 * 其中成员的位置按 score 值递减(从大到小)来排列。
	 * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列。
	 * @param args
	 */
	public Set<String> zrevrange(String key, long start, long end){
		ShardedJedis jedis = null;
		Set<String> set = null;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			set = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return set;
	}
	
	/**
	 * zrevrangeByScoreWithScores
	 * 返回有序集合中在一定范围内按从大到小顺序的member和score
	 * @param args
	 */
	public Set<Tuple> zrevrangeByScoreWithScores(String key, long start, long end) {
		ShardedJedis jedis = null;
		Set<Tuple> set = null;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			set = jedis.zrevrangeByScoreWithScores(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return set;
	}
	
	/**
	 * 
	 * @param args
	 */
	public void setBit(String key, long offset, boolean value){
		
		ShardedJedis jedis = null;
		
		try {
			pool = getPool();
			jedis = pool.getResource();	

			jedis.setbit(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		
	}
	
	/**
	 * 
	 * @param key
	 * @param offset
	 * @return 
	 */
	public boolean getBit(String key, long offset){
		ShardedJedis jedis = null;
		boolean result = false;
		try {
			pool = getPool();
			jedis = pool.getResource();	

			result = jedis.getbit(key, offset);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		
		return result;
	}
	
	/**
	 * pfAdd 
	 * 将任意数量的元素添加到指定的 HyperLogLog 里面。
	 * @param key
	 * @param elements
	 */
	public void  pfAdd(String key,String[] elements) {
		ShardedJedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();	

		    jedis.pfadd(key, elements);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}	
	}
	
	public long pfCount(String key){
		ShardedJedis jedis = null;
		long value = 0;
		try {
			pool = getPool();
			jedis = pool.getResource();	
			value = jedis.pfcount(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null){
				pool.returnResource(jedis);
			}
		}
		return value;
	}
	
	public static void main(String[] args){
//		RedisClient client = new RedisClient();
//		client.hset("prov:34:areacode:0519:threshold", "item:121", "30");
//		System.out.println(client.hget("prov:34:areacode:0519:threshold",  "item:121"));
//		//client.hget("user", "time");
//		//client.hget("user", "time");
		
		RedisClient client = new RedisClient();
		client.zadd("test", 20, "tian1");
		client.zadd("test", 30, "tian2");
		client.zadd("test", 10, "tian3");
		client.zadd("test", 100, "tian4");
		
		Set<String> set = client.zrevrange("test", 0, -1);
		for(String stringSet : set){
			System.out.println("Result:"+stringSet);
		}
	
	}
	
}
