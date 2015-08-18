package md5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;


public class MD5File {
	
	private static final Logger log = Logger.getLogger(MD5File.class);
	
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
//	private ReadBufferBean readBufferBean;
	
	protected MessageDigest messagedigest = null;
	
	public MD5File(){
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error(MD5File.class.getName()
					+ "初始化失败，MessageDigest不支持MD5Util。", e);
			messagedigest = null;
		}
	}
	
	public void putContent(byte[] b){
		putContent(b, 0, b.length);
	}
	
	public void putContent(byte[] b, int start, int length){
		if(messagedigest!=null)
			messagedigest.update(b, start, length);
	}
	
	public String bufferToHex(){
		String md5_value = bufferToHex(messagedigest.digest());
		return md5_value;
	}
	
	/**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String getFileMD5String(File file) throws IOException {		
		InputStream fis = null;
		try{
		    fis = new FileInputStream(file);
		    byte[] buffer = new byte[1024];
		    int numRead = 0;
		    while ((numRead = fis.read(buffer)) > 0) {
		    	messagedigest.update(buffer, 0, numRead);
		    }
		}finally{
			if(fis!=null)
				try{
					fis.close();
				}catch(IOException e){
					log.error("close error file:"+file.getAbsolutePath(),e);
				}
		}
		return bufferToHex(messagedigest.digest());
	}
	
	private String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}
	
	private void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同 
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换 
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
	
//	public static void main(String[] args) throws IOException {
//		long begin = System.currentTimeMillis();
//		MD5File md5 = new MD5File();
//		File file = new File("/Users/rahat/Develop/BA031D010062013012100000001001.AVL");
//		String md5str = md5.getFileMD5String(file);
//		System.out.println(md5str);
//		
////		String md5 = getFileMD5String(file);
//		
////		String md5 = getMD5String("a");
//		
//		long end = System.currentTimeMillis();
//		System.out.println("md5:" + md5str + " time:" + ((end - begin) / 1000)	+ "s");
//	}
}
