package xml.commons.betwixt;

import org.apache.commons.betwixt.io.BeanReader; 
import org.apache.commons.betwixt.io.BeanWriter; 
import org.xml.sax.SAXException; 

import java.beans.IntrospectionException; 
import java.io.IOException; 
import java.io.StringReader; 
import java.io.StringWriter; 

/** 
* 超变态的Betwixt测试 
*/ 
public class TestBetwixt { 
        public static void main(String[] args) throws IOException, SAXException, IntrospectionException { 
                String xml = java2XML(); 
                System.out.println(xml); 

                //Person person = xml2Java(xml); 
                //System.out.println(person); 
        } 

        public static String java2XML() throws IOException, SAXException, IntrospectionException { 
                String reslutXml; 

                //创建一个输出流，将用来输出Java转换的XML文件 
                StringWriter outputWriter = new StringWriter(); 

                //输出XML的文件头 
                outputWriter.write("<?xml version='1.0' encoding='UTF-8'?>\n"); 

                //创建一个BeanWriter实例，并将BeanWriter的输出重定向到指定的输出流 
                BeanWriter beanWriter = new BeanWriter(outputWriter); 

                //配置BeanWriter对象 
                beanWriter.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false); 
                beanWriter.getBindingConfiguration().setMapIDs(false); //不自动生成ID 
                beanWriter.setWriteEmptyElements(true);         //输出空元素 
                beanWriter.enablePrettyPrint();         //格式化输出 

                //构建要转换的对象 
                Person person = new Person("唐伯虎", 24); 
                Pet pet1 = new Pet("旺财", "黄色"); 
                Pet pet2 = new Pet("小强", "灰色"); 
                person.getPetList().add(pet1); 
                person.getPetList().add(pet2); 

                //将对象转换为XML 
                beanWriter.write(person); 
                //获取转换后的结果 
                reslutXml = outputWriter.toString(); 

                //关闭输出流 
                outputWriter.close(); 

                return reslutXml; 
        } 

        /*
        public static Person xml2Java(String xml) throws IntrospectionException, IOException, SAXException { 
                //创建一个读取xml文件的流 
                StringReader xmlReader = new StringReader(xml); 
                //创建一个BeanReader实例，相当于转换器 
                BeanReader beanReader = new BeanReader(); 

                //配置BeanReader实例 
                beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false); 
                beanReader.getBindingConfiguration().setMapIDs(false); //不自动生成ID 
                //注册要转换对象的类，并指定根节点名称 
                beanReader.registerBeanClass("Person", Person.class); 

                //将XML解析Java Object 
                Person person = (Person) beanReader.parse(xmlReader); 

                return person; 
        } */
}