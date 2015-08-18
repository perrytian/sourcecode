package hbase.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.log4j.Logger;

import com.cbss.hsip.common.objects.table.TableMeta;
import com.cbss.hsip.common.objects.table.TableNameMap;

public class HBaseOperV2 implements HBaseInter{
	
	private static Logger logger = Logger.getLogger(HBaseOperV2.class);
	private static HConnection connection;
	private static Configuration cfg;
	
	private ConcurrentHashMap<String, HTableInterface> tableConnPool = new ConcurrentHashMap<String, HTableInterface>();
	
	static{
		cfg = HBaseConfiguration.create();
	/*	configuration.set("hbase.zookeeper.property.clientPort", "2181"); 
	    configuration.set("hbase.zookeeper.quorum", "10.0.210.151"); 
	    configuration.set("hbase.master", "10.0.210.146:60000"); */
		try {
			connection = HConnectionManager.createConnection(cfg);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("获取HBase连接错误",e);
		}
	}

	@Override
	public void put(String tableName,byte[] rowkey, byte[] family, Map<byte[], byte[]> fields) {
		
		if (!fields.isEmpty()) {
			HTableInterface htable = null;
			try {
				htable = getHTable(tableName);
				
				Put put = new Put(rowkey);
				for (Map.Entry<byte[], byte[]> entry : fields.entrySet()) {
					put.add(family, entry.getKey(), entry.getValue());
				}
				htable.put(put);
			} catch(TableNotFoundException e){
				logger.error("增加HBase表数据出错:表不存在,TableName["+tableName+"],Rowkey["+new String(rowkey)+"]",e);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			logger.error("更新的数据为空");
		}
	}
	
	private HTableInterface getHTable(String tableName){
		
		HTableInterface htable = tableConnPool.get(tableName);
		if(htable == null){
			try {
				try {
					createTableIfNotExist(tableName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				htable = connection.getTable(tableName);
				tableConnPool.put(tableName, htable);
				logger.info("创建HTableInterface实例：TableName["+tableName+"]");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			logger.info("从内存中获取HTableInterface实例：TableName["+tableName+"]");
		}
		return htable;
	}
	
	@Override
	public Result get(String tableName, byte[] rowKey) {
		HTableInterface htable = null;
		try {
			long start = System.currentTimeMillis();
			
			htable = getHTable(tableName);
			
			logger.info("###打开HBase表:"+tableName+" 耗时："+(System.currentTimeMillis()-start)+"ms");
			Get get = new Get(rowKey);
			get.setMaxVersions();
			start = System.currentTimeMillis();
			Result res = htable.get(get);
			logger.info("###查询HBase表:"+tableName+" 耗时："+(System.currentTimeMillis()-start)+"ms");
			return res;
		} catch (IOException e) {
			logger.error("获取HBase表数据出错:TableName["+tableName+"],Rowkey["+new String(rowKey)+"]",e);
			return null;
		} catch (Exception e) {
			logger.error("获取HBase表数据出错:TableName["+tableName+"],Rowkey["+new String(rowKey)+"]",e);
			return null;
		}
	}
	
	private void createTableIfNotExist(String tableName) throws Exception{
		long start = System.currentTimeMillis();
		HBaseAdmin admin = new HBaseAdmin(cfg);
		try{
			if (admin.tableExists(tableName)) {
				
				logger.info("表" + tableName + " 在数据库中存在:判断用时："+(System.currentTimeMillis()-start));
				return;
			} else {
				logger.warn("表" + tableName + " 在数据库中不存在,开始自动创建表");
				
				TableName name = TableName.valueOf(tableName);
				
				HTableDescriptor tableDesc = new HTableDescriptor(name);
				StringBuffer output = new StringBuffer();
				
				String tableMetaClassName = TableNameMap.getMetaDataClassName(name.getNameAsString());
				if(tableMetaClassName != null){
					
					try {
						TableMeta meta = (TableMeta) Class.forName(tableMetaClassName).newInstance();
						byte[][] familys = meta.getFamilys();
						for(byte[] family:familys){
							HColumnDescriptor columnDesc = new HColumnDescriptor(
									new String(family));
							columnDesc.setCompressionType(Algorithm.SNAPPY);// 采用SNAPPY压缩算法
							columnDesc.setMaxVersions(2);
							tableDesc.addFamily(columnDesc);
							output.append(new String(family)).append(",");
							//System.out.println(new String(family));
						}
						
					} catch (InstantiationException | IllegalAccessException
							| ClassNotFoundException e) {
						e.printStackTrace();
					}

				} else {
					// 没有配置源数据的表，使用默认的列族
					HColumnDescriptor columnDesc = new HColumnDescriptor(
							new String(TableMeta.FAMILY_BASE));
					columnDesc.setCompressionType(Algorithm.SNAPPY);// 采用SNAPPY压缩算法
					columnDesc.setMaxVersions(2);
					tableDesc.addFamily(columnDesc);
					output.append(new String(TableMeta.FAMILY_BASE));
				}

				admin.createTable(tableDesc);
				if (admin.tableExists(tableName)) {
					logger.info("创建表" + tableName + "成功：列族[" + output.toString()
							+ "],压缩算法[SNAPPY]");
				} else {
					logger.error("创建表 " + tableName + " 失败,请检查数据库");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			admin.close();
		}
	}

	
/* 	public String getValue(byte[] family,byte[] columnName,Result rs){
		long start = System.currentTimeMillis();
		byte[] value = rs.getValue(family, columnName);
		logger.info(new String(family)+":"+new String(columnName)+" cost:"+(System.currentTimeMillis()-start));
		if(value == null){
			return null;
		}else{
			return new String(value);
		}
	} */


	
	

}
