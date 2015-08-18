package ftp.client;

/**
 * @author wei xiaomei
 * 2013/03/07
 * */
import java.io.Serializable;
import java.util.Date;

import com.cusi.ods.dtp.audit.InterfaceDef;
import com.cusi.ods.dtp.ftpserver.FTPUser;
import com.google.gson.annotations.Expose;

public class InterfaceTransferRule implements Serializable {

	private static final long serialVersionUID = -4499659905431278003L;

	@Expose
	private Long id;
	@Expose
	private Date createtime;
	@Expose
	private FTPUser ftpUser;
	@Expose
	private InterfaceDef interfaceDef;
	@Expose
	private int nextTask;
	@Expose
	private int isAudit;
	@Expose
	private int isCompress;
	@Expose
	private int isCheck;// 完整性校验，0不校验 1 校验
	@Expose
	private String alias;
	@Expose
	private String remotePath;
	

	public String getRemotePath() {
		return remotePath;
	}

	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

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

	public FTPUser getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(FTPUser ftpUser) {
		this.ftpUser = ftpUser;
	}

	public InterfaceDef getInterfaceDef() {
		return interfaceDef;
	}

	public void setInterfaceDef(InterfaceDef interfaceDef) {
		this.interfaceDef = interfaceDef;
	}

	public int getNextTask() {
		return nextTask;
	}

	public void setNextTask(int nextTask) {
		this.nextTask = nextTask;
	}

	public int getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(int isAudit) {
		this.isAudit = isAudit;
	}

	public int getIsCompress() {
		return isCompress;
	}

	public void setIsCompress(int isCompress) {
		this.isCompress = isCompress;
	}

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
