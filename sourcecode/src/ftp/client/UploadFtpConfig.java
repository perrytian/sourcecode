package ftp.client;

import java.io.Serializable;
import java.util.Date;

import com.cusi.ods.dtp.entity.FTPUser;
import com.cusi.ods.dtp.entity.InterfaceDef;
import com.google.gson.annotations.Expose;

public class UploadFtpConfig implements Serializable{
	
	private static final long serialVersionUID = 8207990631906336030L;
	
	@Expose
	private Long id;
	
	@Expose
    private Date createtime;
	
	@Expose
	private FTPUser ftpUser;
		
	@Expose
	private InterfaceDef interfaceDef;
	
	@Expose
	private String provId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public FTPUser getFtpUser(){
		return this.ftpUser;
	}
	
	public void setFtpUser(FTPUser ftpUser){
		this.ftpUser = ftpUser;
	}
	
	public InterfaceDef getInterfaceDef() {
		return interfaceDef;
	}

	public void setInterfaceDef(InterfaceDef interfaceDef) {
		this.interfaceDef = interfaceDef;
	}

	public String getProvId() {
		return provId;
	}

	public void setProvId(String provId) {
		this.provId = provId;
	}
	
}
