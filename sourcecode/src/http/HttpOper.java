package http;

public interface HttpOper {
	
	public HttpServerResult downloadFileFromHttpServer(String httpServerURL,String saveFilePath) throws Exception;
	
	public String[] openURL(String httpServerURL) throws Exception;
	
//	public void uploadFileToHttpServer(String httpServerURL,String filePath) throws Exception;
	
}
