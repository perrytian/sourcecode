package com.cusi.nms.aladdin.comm.http.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.cusi.nms.aladdin.comm.http.Http;
import com.cusi.nms.aladdin.comm.http.impl.HttpImpl;
import com.cusi.nms.aladdin.comm.http.object.HttpServerResult;
import com.cusi.nms.aladdin.comm.http.service.HttpOper;

public class HttpOperImpl implements HttpOper{

	private static final int BUFFER_SIZE = 8096;//缓冲区大小

	/**
	 * 从指定WEB服务器上下载文件，存储为saveFilePath指定的文件
	 */
	@Override
	public HttpServerResult downloadFileFromHttpServer(String httpServerURL,String saveFilePath) throws Exception{
		
		HttpServerResult result = null;
		
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		URL httpServer = null;
		HttpURLConnection httpUrlConn = null;
		byte[] buf = new byte[BUFFER_SIZE];
		
		long downloadDelay = 0;
		
		try {
			
			result = new HttpServerResult();
			result.setHttpServerURL(httpServerURL);
			result.setSaveToFile(saveFilePath);
			
			//建立连接
			httpServer = new URL(httpServerURL);
			httpUrlConn = (HttpURLConnection) httpServer.openConnection();

			long startTime = System.currentTimeMillis();
			//连接指定的资源
			httpUrlConn.connect();
			//获取网络输入流
			bis = new BufferedInputStream(httpUrlConn.getInputStream());
			
			//建立文件
			fos = new FileOutputStream(saveFilePath);
			System.out.println("正在获取服务器链接["+httpServerURL+"]的内容：\n将其保存为文件["+saveFilePath+"]");
			//保存文件
			int size = 0;
			while((size = bis.read(buf))!=-1){
				fos.write(buf,0,size);
			}
			long stopTime = System.currentTimeMillis();
			
			downloadDelay = stopTime - startTime;
			
			result.setFileSize(size);
			result.setDownloadDelay(downloadDelay);
			
			System.out.println("读取服务器链接["+httpServerURL+"]的内容：\n将其保存为文件["+saveFilePath+"] 的时延为：["+downloadDelay+"ms]");
			
			return result;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally{
			try {
				fos.close();
				bis.close();
				httpUrlConn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public HttpServerResult downloadFileFromHttpServerMeta(String httpServerURL,String saveFilePath) throws Exception{
		
		Http http = new HttpImpl();
		http.GET(httpServerURL);
		
		if(http.getInputStream() != null){
			
		}else{
			
		}
		
		return null;
	}
	
	@Override
	public String[] openURL(String httpServerURL) {
		
		List<String> openUrlHeader = new ArrayList<String>();
		
		Http http = new HttpImpl();
		http.GET(httpServerURL);
		
		ByteArrayInputStream response = (ByteArrayInputStream) http.getInputStream();
		
		if(response != null){
			openUrlHeader.add(String.valueOf(http.getResponseCode()));
			openUrlHeader.add(http.getResponseMessage());
			openUrlHeader.add(http.getServerVersion());
		}
		
		return openUrlHeader.toArray(new String[openUrlHeader.size()]);
	}

//	@Override
//	public void uploadFileToHttpServer(String httpServerURL,String filePath) throws Exception{
//		
//	}
	
	public static void main(String[] args){
		HttpOper fileOper = new HttpOperImpl();
		
		String sohu_url = "http://www.sohu.com";
		String sina_url = "http://www.sina.com.cn";
		String baidu_url = "http://www.baidu.com";
		String google_url = "http://www.google.cn";
		String[] httpServerURLs = {sohu_url,sina_url,baidu_url,google_url};
		for(String httpServerURL:httpServerURLs){
			String destFileName = httpServerURL.substring(httpServerURL.indexOf("http://www.")+("http://www.").length());
			String saveFilePath = System.getProperty("user.dir")+"\\"+destFileName+".html";
			try {
				fileOper.downloadFileFromHttpServer(httpServerURL, saveFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

}
