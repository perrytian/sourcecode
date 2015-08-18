package database.commons.dbutils;
 
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 


import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
 
 
public class JdbcUtils{
    
    private static DataSource ds = null;
    //在静态代码块中创建数据库连接池
    static{
        try{
             
            //通过读取C3P0配置文件创建数据源，C3P0的xml配置文件c3p0.properties必须放在src目录下
           ds = new ComboPooledDataSource("oracle");//使用C3P0的命名配置来创建数据源
             
        }catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    public static Connection getConnection() throws SQLException{
        //从数据源中获取数据库连接
        return ds.getConnection();
    }
    public static void release(Connection conn,Statement st,ResultSet rs){
        if(rs!=null){
            try{
                //关闭存储查询结果的ResultSet对象
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(st!=null){
            try{
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
         
        if(conn!=null){
            try{
                //将Connection连接对象还给数据库连接池
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
 
    public static DataSource getDataSource() {
        return ds;
    }
    
    public static void main(String[] args){
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
		try {
			conn = JdbcUtils.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from ucr_act1.tf_f_user");
			while(rs!=null && rs.next()){
				String serial_number = rs.getString("serial_number");
				System.out.println(serial_number);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			DbUtils.closeQuietly(conn);
		}
    	
    }
}