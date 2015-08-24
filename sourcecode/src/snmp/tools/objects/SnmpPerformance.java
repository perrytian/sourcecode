package snmp.tools.objects;

import java.io.Serializable;
import java.util.Date;

public class SnmpPerformance implements Serializable{
	
	protected Long id;
	
	protected PortPerformance[] portPerf;
	
	protected Date datatime;
	
	public Date getDatatime() {
		return datatime;
	}

	public void setDatatime(Date datatime) {
		this.datatime = datatime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PortPerformance[] getPortPerf() {
		return portPerf;
	}

	public void setPortPerf(PortPerformance[] portPerf) {
		this.portPerf = portPerf;
	}
	
	
}
