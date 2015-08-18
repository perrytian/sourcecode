package database.commons.dbutils;

public class User {
	
	private String serial_number;
	private String net_type_code;
	private String user_id;
	private String eparchy_code;
	private String city_code;
	private String cust_id;
	

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getEparchy_code() {
		return eparchy_code;
	}

	public void setEparchy_code(String eparchy_code) {
		this.eparchy_code = eparchy_code;
	}

	public String getCity_code() {
		return city_code;
	}

	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	public String getNet_type_code() {
		return net_type_code;
	}

	public void setNet_type_code(String net_type_code) {
		this.net_type_code = net_type_code;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
}
