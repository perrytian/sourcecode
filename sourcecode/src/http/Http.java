package http;

import java.io.InputStream;

public interface Http {
	
	public void GET(String url);
	
	public void HEAD(String url);
	
	public void POST(String url,String content);
	
	public int getResponseTime();
	
	public int getResponseCode();
	
	public String getResponseMessage();
	
	public String getServerVersion();
	
	public InputStream getInputStream();   

}
