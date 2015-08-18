package database.commons.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

public class Test {

	private static Logger logger = Logger.getLogger(Test.class);
	
	public static void main(String[] args) {
		test_query();
	}
	
	public static void test_query(){
		Connection conn = null;
		try {
			conn = JdbcUtils.getConnection();
			QueryRunner runner = new QueryRunner();
			String querySQL = "select * from ucr_act1.tf_f_user where serial_number='18601105586'";
			/* 
			 * ResultSetHandler 类型 
			 * ArrayHandler：把结果集中的第一行数据转成对象数组。 
			 * ArrayListHandler：把结果集中的每一行数据都转成一个对象数组，再存放到List中。 
			 * BeanHandler：将结果集中的第一行数据封装到一个对应的JavaBean实例中。 
			 * BeanListHandler：将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里。 
			 * ColumnListHandler：将结果集中某一列的数据存放到List中。 
			 * KeyedHandler：将结果集中的每一行数据都封装到一个Map里，然后再根据指定的key把每个Map再存放到一个Map里。 
			 * MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。 
			 * MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List。 
			 * ScalarHandler：将结果集中某一条记录的其中某一列的数据存成Object。 
			 */
			List<User> userList = (List<User>)runner.query(conn,querySQL,new BeanListHandler<User>(User.class));
			Gson gson = new Gson();
			String output = gson.toJson(userList);
			logger.info(output);
			/*
			for(User user:userList){
				logger.info(user.getSerial_number()+" "+user.getNet_type_code()+" "+user.getEparchy_code()+" "+user.getCust_id());
				
			}*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
		
	}

}
