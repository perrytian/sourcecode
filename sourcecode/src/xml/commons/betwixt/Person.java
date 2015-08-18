package xml.commons.betwixt;

import java.util.ArrayList; 
import java.util.List; 

public class Person { 
        private String name;                //姓名 
        private int age;                        //年龄 
        private List<Pet> petList;    //拥有的宠物 

        //------------constructor----------- 

        /** 
         * 这个默认的构造方法不可少，否则转换出错 
         */ 
        public Person() { 
                petList = new ArrayList<Pet>(); 
        } 

        public Person(String name, int age) { 
                petList = new ArrayList<Pet>(); 
                this.name = name; 
                this.age = age; 
        } 

        //------------add集合成员的方法----------- 

        /** 
         * 添加集合属性元素的方法，add后的单词必须决定了xml中元素的名字 
         * 
         * @param pet 
         */ 
        public void addPet(Pet pet) { 
                petList.add(pet); 
        } 

        //------------getter/setter----------- 

        public String getName() { 
                return name; 
        } 

        public void setName(String name) { 
                this.name = name; 
        } 

        public int getAge() { 
                return age; 
        } 

        public void setAge(int age) { 
                this.age = age; 
        } 

        public List<Pet> getPetList() { 
                return petList; 
        } 

        public void setPetList(List<Pet> petList) { 
                this.petList = petList; 
        } 

        public String toString() { 
                StringBuffer sb = new StringBuffer(); 
                sb.append("Person{" + 
                                "name='" + name + '\'' + 
                                ", age=" + age + 
                                ", petList=\n"); 
                for (Pet pet : petList) { 
                        sb.append("\t\t" + pet.toString()).append(";\n"); 
                } 
                sb.append('}'); 
                return sb.toString(); 
        } 
}
