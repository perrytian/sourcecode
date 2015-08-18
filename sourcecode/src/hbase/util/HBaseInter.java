package hbase.util;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;

public interface HBaseInter {
	
	public void put(String tableName,byte[] rowkey, byte[] family, Map<byte[], byte[]> fields);
	
	public Result get(String tableName,byte[] rowKey);
	
	

}
