package file.commons.io;

import java.io.File;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileAndDirMonitor {

	public static void main(String[] args) {
		File parentDir = FileUtils.getFile(FilenameUtils.getFullPath(new File("file/file.txt").getAbsolutePath()));
		System.out.println(parentDir.getAbsolutePath());
		FileAlterationObserver observer = new FileAlterationObserver(parentDir);
		observer.addListener(new FileAlterationListenerAdaptor(){
			public void onFileCreate(File file){
				System.out.println("File created:"+file.getName());
			}
			
			public void onFileDelete(File file){
				System.out.println("File deleted:"+file.getName());
			}
			
			public void onDirectoryCreate(File dir){
				System.out.println("Directory create:"+dir.getName());
			}
			
			public void onDirectoryDelete(File dir){
				System.out.println("Directory delete:"+dir.getName());
			}
		});
		
		//add a monitor that will check for events every x ms;
		//每500ms监听一次
		FileAlterationMonitor monitor = new FileAlterationMonitor(500,observer);
		try {
			monitor.start();
			//添加文件，测试
			File newDir = new File("file/newDir");
			File newFile = new File("file/newfile.txt");
			
			newDir.mkdirs();
			newFile.createNewFile();
			Thread.sleep(1000);
			
			FileDeleteStrategy.NORMAL.delete(newDir);
			FileDeleteStrategy.NORMAL.delete(newFile);
			
			Thread.sleep(1000);
			monitor.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
