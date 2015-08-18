package ftp.client;

public class DownloadTask implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String fieName;//暂为绝对路径
	private String ftpServer;
	private int port;
	
	private String userName;
	private String passwd;
	private String remotePath;
	private String localPath;
	
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getFieName() {
		return fieName;
	}
	public void setFieName(String fieName) {
		this.fieName = fieName;
	}
	public String getFtpServer() {
		return ftpServer;
	}
	public void setFtpServer(String ftpServer) {
		this.ftpServer = ftpServer;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getRemotePath() {
		return remotePath;
	}
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}
}
