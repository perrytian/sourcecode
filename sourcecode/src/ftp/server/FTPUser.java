package ftp.server;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class FTPUser implements Serializable{
	
	private static final long serialVersionUID = -3843089985183897607L;
	
    @Expose
	private Long id;
	
	@Expose
	private String userName;
	
	@Expose
	private String password;
	
	@Expose
	private String ftpPort;
	
	@Expose
	private String homeDirectory;
	
	@Expose
	private Integer idleTime;
	
    @Expose
    private java.util.Date createtime;
	
	@Expose
	private FtpResource ftpResource;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	
	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}
	
	public Integer getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(Integer idleTime) {
		this.idleTime = idleTime;
	}
	
	public FtpResource getFtpResource(){
		return ftpResource;
	}
	
	public void setFtpResource(FtpResource ftpResource){
		this.ftpResource = ftpResource;
	}
	
	public java.util.Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
}
