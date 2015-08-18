package ftp.client;

public class UploadTask implements java.io.Serializable, Comparable<UploadTask> {

	private static final long serialVersionUID = 1L;
	private String fieName;// 暂为绝对路径
	private String ftpServer = "127.0.0.1";
	private int port = 21;
	private int priority = 0; //优先级

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	private String userName = "";
	private String passwd;
	private String remotePath = "";
	private boolean uploadStatus = false;// 标示是否上传成功

	private boolean needZipUpload = false;// 需要压缩上传

	private int uploadCount = 0;
	private int delayTime = 0;

	
	public int compareTo(UploadTask o) {
		// 复写此方法进行任务执行优先级排序
		// return priority < o.priority ? 1 :
		// (priority > o.priority ? -1 : 0);
		if (priority < o.priority) {
			return -1;
		} else {
			if (priority > o.priority) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public int getUploadCount() {
		return this.uploadCount;
	}

	public void setUploadCount(int uploadCount) {
		this.uploadCount = uploadCount;
	}

	public boolean getUploadStatus() {
		return this.uploadStatus;
	}

	public void setUploadStatus(boolean uploadStatus) {
		this.uploadStatus = uploadStatus;
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

	public boolean isNeedZipUpload() {
		return needZipUpload;
	}

	public void setNeedZipUpload(boolean needZipUpload) {
		this.needZipUpload = needZipUpload;
	}

	public boolean equals(Object e) {
		if (e instanceof UploadTask) {
			UploadTask t = (UploadTask) e;
			return this.fieName.equals(t.getFieName())
					&& this.ftpServer.equals(t.getFtpServer())
					&& this.port == t.getPort()
					&& this.userName.equals(t.getUserName())
					&& this.remotePath.equals(t.getRemotePath());
		}
		return false;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
}
