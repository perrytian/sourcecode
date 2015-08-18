package ftp.server;

import static com.cusi.ods.dtp.conf.SysConfFactory.SysConf;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import com.cusi.ods.dtp.common.WebConfigInfUtil;
import com.cusi.ods.dtp.entity.FTPUser;

public class FtpUserService {
	
	private static final Log log = LogFactory.getLog(FtpUserService.class);
	
	/**
	 * 使用PropertiesUserManager管理器
	 * @return
	 */
	public UserManager getPropertiesUserManager() {
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		try {
			if (SysConf.getBoolean("FTP.useUserProperties", false)) {
				userManagerFactory.setFile(new File(FtpServer.class
						.getResource("/").toURI().getPath()
						+ SysConf.getString("FTP.userproperties")));
			}
		} catch (URISyntaxException e2) {
			log.error("加载" + SysConf.getString("FTP.userproperties") + "出错", e2);
		}
		return userManagerFactory.createUserManager();
	}
	
	public List<User> getFtpUser() throws Exception{
		List<FTPUser> listUser= getFtpUserByRest();
//		List<HdfsUser> listUser=initFTPUser();
		List<User> list = new ArrayList<User>();
		for(FTPUser item : listUser){
			BaseUser user = new BaseUser();
			user.setName(item.getUserName());
			user.setPassword(item.getPassword());
			user.setHomeDirectory(item.getHomeDirectory());
			user.setMaxIdleTime(item.getIdleTime());
			List<Authority> authorities = new ArrayList<Authority>();
			WritePermission wp = new WritePermission();
			authorities.add(wp);
			user.setAuthorities(authorities);
			list.add(user);
		}
		return list;
	}
	
	private List<FTPUser> getFtpUserByRest() throws Exception{
		List<FTPUser> list = WebConfigInfUtil.getInstance().getFtpUserByRest();
		return list;
	}
//	
//	private List<HdfsUser> initFTPUser(){
//		List<HdfsUser> list = new ArrayList<HdfsUser>();
//		HdfsUser hdfsUser = new HdfsUser();
//		hdfsUser.setName("root");
//		ArrayList<String> usergroup = new ArrayList<String>();
//		usergroup.add("root");
//		hdfsUser.setGroups(usergroup);
//		hdfsUser.setPassword("123456");
//		hdfsUser.setMaxIdleTime(60000);
//		//hdfsUser.setHomeDirectory("/hdfs_ftp");
//		hdfsUser.setHomeDirectory("/");
//		list.add(hdfsUser);
//		return list;
//	}
}
