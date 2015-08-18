package ftp.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class FtpResource implements Serializable{
	
	private static final long serialVersionUID = 8667067452983109276L;

    @Expose
	private Long id;
	
	@Expose
	private String servername;
	
	@Expose
	private String ip;
	
	@Expose
	private String port;
	
	@Expose
	private String remark;
	
	@Expose
	private String provCode;
	
    @Expose
    private java.util.Date createtime;
		
	private List<FTPUser> fTPUsers = new ArrayList<FTPUser>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public java.util.Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
	
	public List<FTPUser> getFTPUsers(){
		return this.fTPUsers;
	}
	
	public void setFTPUsers(List<FTPUser> fTPUsers){
		this.fTPUsers = fTPUsers;
	}
	
	public String getProvCode(){
		return this.provCode;
	}
	public void setProvCode(String provCode){
		this.provCode = provCode;
	}
	
}
