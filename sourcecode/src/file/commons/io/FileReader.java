package file.commons.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

public class FileReader {

	public static void main(String[] args) throws IOException {
		String content = FileUtils.readFileToString(new File("file/file.txt"));
		System.out.println(content);
		InputStream in = new URL("http://www.sohu.com").openStream();
		try{
			System.out.println(IOUtils.toString(in));
			//获取目录空间
			long freeSpace = FileSystemUtils.freeSpaceKb("C:/");
			System.out.println(freeSpace+"KB");
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			IOUtils.closeQuietly(in);
		}
		
		File file = new File("file/file.txt");
		List lines = FileUtils.readLines(file,"UTF-8");
		
		//打印文件的所有行
		LineIterator it = FileUtils.lineIterator(file,"UTF-8");
		try{
			while(it.hasNext()){
				String line = it.nextLine();
			}
		}finally{
			LineIterator.closeQuietly(it);
		}
	}

}
