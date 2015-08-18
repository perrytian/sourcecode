package hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.cusi.hods.clear.model.Table;
import com.cusi.hods.clear.model.TableMeta;

/**
 * HBase操作类
 */
public class HBaseOper {
	
	private static Logger logger = Logger.getLogger(HBaseOper.class);
	
	private static Configuration cfg = null;
	
	public List<Put> putList = new ArrayList<Put>();
	
	//测试代码
	static{
		cfg = HBaseConfiguration.create();
//		cfg.set("hbase.master", "node4.hadoop:60000");
//		cfg.set("hbase.master", "10.0.210.152:60000");
//		cfg.set("hbase.zookeeper.quorum", "10.0.210.146,10.0.210.152,10.0.210.142,10.0.210.143,10.0.210.145");
//		cfg.set("hbase.zookeeper.quorum", "node1.hadoop,node4.hadoop,node10.hadoop");
//		cfg.set("hbase.zookeeper.property.clientPort", "2181");
		
	}
	
	//columnFamily 列族 cdrmn
	// 创建数据库表
	public boolean createTable(String tablename,String columnFamily) throws Exception{
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if(admin.tableExists(tablename)){
			logger.info("创建表失败:表"+tablename+" 已经存在!");
			return false;
		}else{
			HTableDescriptor tableDesc = new HTableDescriptor(tablename);
			HColumnDescriptor columnDesc = new HColumnDescriptor(columnFamily.getBytes());
			columnDesc.setCompressionType(Algorithm.SNAPPY);//采用SNAPPY压缩算法
			tableDesc.addFamily(columnDesc);
			admin.createTable(tableDesc);
			logger.info("创建表"+tablename+"成功：列族["+columnFamily+"],压缩算法[SNAPPY]");
			return true;
		}
	}
	
	// 创建数据库表
	public void createTable(String tableName, String[] columnFamilys)
				throws Exception {
			// 新建一个数据库管理员
			HBaseAdmin hAdmin = new HBaseAdmin(cfg);

			if (hAdmin.tableExists(tableName)) {
				logger.info("表已经存在");
			} else {
				// 新建一个 scores 表的描述
				HTableDescriptor tableDesc = new HTableDescriptor(tableName);
				// 在描述里添加列族
				for (String columnFamily : columnFamilys) {
					HColumnDescriptor columnDesc = new HColumnDescriptor(columnFamily.getBytes());
					columnDesc.setCompressionType(Algorithm.SNAPPY);//采用SNAPPY压缩算法
					tableDesc.addFamily(columnDesc);
				}
				// 根据配置好的描述建表
				hAdmin.createTable(tableDesc);
				logger.info("创建表成功"); 
			}
		}

	//往数据库表中添加数据
	public void put(String tablename,String rowkey,String columnFamily,String column,String data) throws Exception{
		HConnection connection = HConnectionManager.createConnection(cfg);
		HTableInterface table = connection.getTable(tablename);
		//table.setAutoFlush(false);
		Put put = new Put(Bytes.toBytes(rowkey));
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
		putList.add(put);
		
		if(putList.size() >=10000){
			//入库条数大于10000才集中入库，这个数据值在调优时需要调整
			StringBuffer output = new StringBuffer();
			output.append("执行批量入库：数据量"+putList.size());
			long start = System.currentTimeMillis();
			table.put(putList);
			putList.clear();
			output.append("   用时:"+(System.currentTimeMillis()-start)+"ms");
			logger.info(output.toString());
		}
		table.close();
		connection.close();
	}
	
	//从数据库表中获取数据
	public void get(String tablename,String rowkey) throws IOException{
		long start = System.currentTimeMillis();
		HTable table = new HTable(cfg,tablename);
		Get g = new Get(Bytes.toBytes(rowkey));
		Result result = table.get(g);
		logger.info("查询"+tablename+"表，ROWKEY="+rowkey+",用时["+(System.currentTimeMillis()-start)+"]ms  Get:"+result);
	}
	
	/**
		[
		 {rowkey:"rowkey",row: {USER_ID:value,HOME_AREA_CODE:value,START_TIME:value,OTHER_PARTY:value,UNI_CALL_TYPE:value}}
		 {rowkey:"rowkey",row: {name:value,name:value,name:value}}
		 {rowkey:"rowkey",row: {name:value,name:value,name:value}}
		 {rowkey:"rowkey",row: {name:value,name:value,name:value}}
		]
	 * @param tablename
	 * @param startRow
	 * @param stopRow
	 * @return
	 * @throws Exception
	 */
	public static List<DataRecord> scan(String tablename,byte[] startRow,byte[] stopRow) throws Exception{
		long start = System.currentTimeMillis();
		HTable table = new HTable(cfg,tablename);
		Scan scan = new Scan();
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		ResultScanner rs = table.getScanner(scan);
		int count = 0;
		
		StringBuffer output = new StringBuffer();
		
		List<DataRecord> ret = new ArrayList<DataRecord>();
		for(Result r:rs){
			DataRecord dataRecord = new DataRecord();
			count++;
		    byte[] rowkey = null;
		    Map<String,String> keyvalueMap = new ConcurrentHashMap<String,String>();
			for(KeyValue rowKV:r.raw()){
				rowkey = rowKV.getRow();
				//output.append("\n扫描结果[").append(tablename).append("]：      RowKey:").append(new String(rowKV.getRow())).append(" TimeStamp:").append(rowKV.getTimestamp());
				//output.append(" Column Family:").append(new String(rowKV.getFamily()));
				//output.append(" Column Name:").append(new String(rowKV.getQualifier())).append(" Value:").append(new String(rowKV.getValue()));
				
				keyvalueMap.put(new String(rowKV.getQualifier()), new String(rowKV.getValue()));
			}
			
			dataRecord.setRowkey(rowkey);
			dataRecord.setKeyvalue(keyvalueMap);
			
			ret.add(dataRecord);
		}
		rs.close();
		logger.info("\n ================扫描表"+tablename+",  返结果："+count+"条  用时"+(System.currentTimeMillis()-start)+"ms\n"+output.toString());
		return ret;
	}
	
	//扫描表
	public Table scan(TableMeta meta,byte[] startRow,byte[] stopRow) throws IOException{
		long start = System.currentTimeMillis();
		HTable hTable = new HTable(cfg,meta.getTableName());
		Scan scan = new Scan();
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		ResultScanner rs = hTable.getScanner(scan);
		
		Table table = new Table(meta);
		int count = table.Load(rs);
		
		StringBuffer output = new StringBuffer();
		
		table.Load(rs);
		
		rs.close();
		logger.info("\n ================扫描表"+meta.getTableName()+",  返结果："+count+"条  用时"+(System.currentTimeMillis()-start)+"ms\n"+output.toString());
		return table;
	}
	
	//根据条件扫描表
	public static List<DataRecord> scanByFilter(String tablename,byte[] startRow,byte[] stopRow,List<String> filterCondition) throws IOException{
		long start = System.currentTimeMillis();
		HTable table = new HTable(cfg,tablename);
		FilterList filterList = new FilterList();
		Scan scan = new Scan();
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		StringBuffer output = new StringBuffer();
		for(String attribute:filterCondition){
			output.append(attribute).append(" | ");
			String[] s = attribute.split(",");
			filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(s[0]),Bytes.toBytes(s[1]),CompareOp.EQUAL,Bytes.toBytes(s[2])));
		}
		
		scan.setFilter(filterList);
		ResultScanner resultScanner = table.getScanner(scan);
		int count = 0;
		List<DataRecord> ret = new ArrayList<DataRecord>();
		
		for(Result result:resultScanner){
			DataRecord dataRecord = new DataRecord();
			count++;
		    byte[] rowkey = null;
		    Map<String,String> keyvalueMap = new ConcurrentHashMap<String,String>();
			for(KeyValue rowKV:result.raw()){
				rowkey = rowKV.getRow();
				
				//output.append("\n扫描结果：      RowKey:").append(new String(rowKV.getRow())).append(" TimeStamp:").append(rowKV.getTimestamp());
				//output.append(" Column Family:").append(new String(rowKV.getFamily()));
				//output.append(" Column Name:").append(new String(rowKV.getQualifier())).append(" Value:").append(new String(rowKV.getValue()));
				keyvalueMap.put(new String(rowKV.getQualifier()),new String(rowKV.getValue()));
			}
			
			dataRecord.setRowkey(rowkey);
			dataRecord.setKeyvalue(keyvalueMap);
			
			ret.add(dataRecord);
		}
		resultScanner.close();
		logger.info("\n ================扫描表"+tablename+",过滤条件("+output+"),  返结果："+count+"条  用时"+(System.currentTimeMillis()-start)+"ms\n"+output.toString());
		return ret;
	} 
	
	//根据条件扫描表
	public Table scanByFilter(TableMeta meta,byte[] startRow,byte[] stopRow,List<String> filterCondition) throws IOException{
		long start = System.currentTimeMillis();
		HTable hTable = new HTable(cfg,meta.getTableName());
		FilterList filterList = new FilterList();
		Scan scan = new Scan();
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		StringBuffer output = new StringBuffer();
		for(String attribute:filterCondition){
			output.append(attribute).append(" | ");
			String[] s = attribute.split(",");
			filterList.addFilter(new SingleColumnValueFilter(Bytes.toBytes(s[0]),Bytes.toBytes(s[1]),CompareOp.EQUAL,Bytes.toBytes(s[2])));
		}
		
		scan.setFilter(filterList);
		ResultScanner resultScanner = hTable.getScanner(scan);
		
		Table table = new Table(meta);
		int count = table.Load(resultScanner);
		
		resultScanner.close();
		logger.info("\n ================扫描表"+meta.getTableName()+",过滤条件("+output+"),  返结果："+count+"条  用时"+(System.currentTimeMillis()-start)+"ms\n"+output.toString());
		return table;
	}
	
	//删除表
	public boolean deleteTable(String tablename) throws IOException{
		HBaseAdmin admin = new HBaseAdmin(cfg);
		if(admin.tableExists(tablename)){
			try{
				admin.disableTable(tablename);
				admin.deleteTable(tablename);
				logger.info("删除表"+tablename+"成功");
			}catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
		}else{
			logger.info("删除表"+tablename+"不存在");
			return false;
		}
		return true;
	}
}