package ftp.server;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import org.apache.ftpserver.ConnectionConfig;

public class DtpConnectionConfig implements ConnectionConfig {
	
	private int maxLogins = 200;

    private boolean anonymousLoginEnabled = false;

    private int maxAnonymousLogins = 10;

    private int maxLoginFailures = 3;

    private int loginFailureDelay = 500;
    
    private int maxThreads = 400;
    
    public DtpConnectionConfig(){
    	this.maxLogins = SysConf.getInt("ftp_maxLogins",200);
    	this.maxAnonymousLogins = SysConf.getInt("ftp_maxAnonymousLogins",0);
    	this.maxLoginFailures = SysConf.getInt("ftp_maxLoginFailures",3);
    	this.loginFailureDelay = SysConf.getInt("ftp_loginFailureDelay",200);
    	this.maxThreads = SysConf.getInt("ftp_maxThreads",400);
    }

    public int getLoginFailureDelay() {
        return loginFailureDelay;
    }

    public int getMaxAnonymousLogins() {
        return maxAnonymousLogins;
    }

    public int getMaxLoginFailures() {
        return maxLoginFailures;
    }

    public int getMaxLogins() {
        return maxLogins;
    }

    public boolean isAnonymousLoginEnabled() {
        return anonymousLoginEnabled;
    }
    
    public int getMaxThreads() {
        return maxThreads;
    }

}
