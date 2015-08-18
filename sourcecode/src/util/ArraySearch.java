package util;

public class ArraySearch {
	
	
	/**
	 * 折半查找
	 */
	public static int splitHalf(int[] arrayData,int searchData,int start,int end){  
        int index = (start + end)/2;  
        int data = arrayData[index];
        if(start > end ){  
            return -1;  
        }  
        if(data == searchData){  
            return index;  
        }else{  
            if(data < searchData){  
                return splitHalf(arrayData,searchData,index+1,end);  
            }else{  
                return splitHalf(arrayData,searchData,start,index-1);  
            }  
        }  
    }  
}
