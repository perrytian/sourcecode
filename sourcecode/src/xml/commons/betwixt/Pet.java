package xml.commons.betwixt;

/** 
* 宠物 
*/ 
public class Pet { 
        private String nikename;        //昵称 
        private String color;             //颜色 

        /** 
         * 这个默认的构造方法不可少 
         */ 
        public Pet() { 
        } 

        public Pet(String nikename, String color) { 
                this.nikename = nikename; 
                this.color = color; 
        } 

        public String getNikename() { 
                return nikename; 
        } 

        public void setNikename(String nikename) { 
                this.nikename = nikename; 
        } 

        public String getColor() { 
                return color; 
        } 

        public void setColor(String color) { 
                this.color = color; 
        } 

        public String toString() { 
                return "Pet{" + 
                                "nikename='" + nikename + '\'' + 
                                ", color='" + color + '\'' + 
                                '}'; 
        } 
}