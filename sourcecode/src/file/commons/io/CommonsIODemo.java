package file.commons.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class CommonsIODemo {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Full path of exampleTxt: "
				+ FilenameUtils.getFullPath(new File("file/file.txt").getAbsolutePath())); 
		System.out.println("Base name of exampleTxt: "
				+ FilenameUtils.getBaseName(new File("file/file.txt").getAbsolutePath()));
	}

}
