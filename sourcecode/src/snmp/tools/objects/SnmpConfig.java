package snmp.tools.objects;

import java.io.Serializable;

public class SnmpConfig implements Serializable{
	
	protected long id;//MOID
	protected String moName;//设备名称
	protected String neModel;//设备类型:采集时默认为sysobjID
	protected String[] sysConfig;
	protected PortConfig[] portConfig;
	
	public String getMoName() {
		return moName;
	}
	public void setMoName(String moName) {
		this.moName = moName;
	}
	public String getNeModel() {
		return neModel;
	}
	public void setNeModel(String neModel) {
		this.neModel = neModel;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String[] getSysConfig() {
		return sysConfig;
	}
	
	public void setSysConfig(String[] sysConfig) {
		this.sysConfig = sysConfig;
	}
	public PortConfig[] getPortConfig() {
		return portConfig;
	}
	public void setPortConfig(PortConfig[] portConfig) {
		this.portConfig = portConfig;
	}
	
}

