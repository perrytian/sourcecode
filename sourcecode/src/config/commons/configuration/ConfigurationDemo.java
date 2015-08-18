package config.commons.configuration;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.betwixt.XMLUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

public class ConfigurationDemo {
	
	public static void readProperties(){
		PropertiesConfiguration config;
		try {
			config = new PropertiesConfiguration("conf/commons.configuration.demo.properties");
			System.out.println(config.getString("driver"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void readXML(){
		try {
			XMLConfiguration config = new XMLConfiguration("conf/commons.configuration.config.xml");
			//对于单独元素的话，可以直接通过标签名获取值  
            String str=config.getString("boy");  
            System.out.println(str);  
            //对于循环出现的嵌套元素，可以通过父元素.子元素来获取集合值  
            List<Object> names= config.getList("student.name");  
            System.out.println(Arrays.toString(names.toArray()));  
            //对于一个单独元素包含的值有多个的话如：a,b,c,d 可以通过获取集合  
            List<Object> titles=config.getList("title");  
            System.out.println(Arrays.toString(titles.toArray()));  
            //对于标签元素的属性，可以通过 标签名[@属性名] 这样的方式获取  
            String size=config.getString("ball[@size]");  
            System.out.println(size);  
            //对于嵌套的标签的话，想获得某一项的话可以通过 标签名(索引名) 这样方式获取  
            String id=config.getString("student(1)[@id]");  
            System.out.println(id);  
              
            String go=config.getString("student.name(0)[@go]");  
            System.out.println(go);  
            /** 
             * 依次输出结果为 
             * tom 
             * [lily, lucy] 
             * [abc, cbc, bbc, bbs] 
             * 20 
             * 2 
             * common1 
             *  
             */  
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		readProperties();
		readXML();
	}

}
