package jmx.objects;

import java.io.Serializable;

public class TomcatUsersInfo implements Serializable{
	
	private String[] roleNames;
	
	private String[] userNames;
	
	private String userDatabasePath;

	public String[] getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}

	public String[] getUserNames() {
		return userNames;
	}

	public void setUserNames(String[] userNames) {
		this.userNames = userNames;
	}

	public String getUserDatabasePath() {
		return userDatabasePath;
	}

	public void setUserDatabasePath(String userDatabasePath) {
		this.userDatabasePath = userDatabasePath;
	}
	
}
