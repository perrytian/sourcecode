package ping;

import java.io.Serializable;
import java.util.Date;

/**
 * Ping 测试结果
 * @author mouse
 *
 */
public class PingResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ip;//测试目标IP地址
	//时延
	private double rttMinimum = 0.0;
	private double rttMaxmum = 0.0;
	private double rttAverage = 0.0;
	private long packetCount = 0;//包数量
	private long packetSize = 0;//包大小
	private int sentCount = 0;//发送包数
	private int receivedCount = 0;//接收包数
	private double lost = 0.0;
	private Date pingTime = new Date();
	private Date scheTime = new Date();
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public double getRttMinimum() {
		return rttMinimum;
	}
	public void setRttMinimum(double rttMinimum) {
		this.rttMinimum = rttMinimum;
	}
	public double getRttMaxmum() {
		return rttMaxmum;
	}
	public void setRttMaxmum(double rttMaxmum) {
		this.rttMaxmum = rttMaxmum;
	}
	public double getRttAverage() {
		return rttAverage;
	}
	public void setRttAverage(double rttAverage) {
		this.rttAverage = rttAverage;
	}
	public long getPacketCount() {
		return packetCount;
	}
	public void setPacketCount(long packetCount) {
		this.packetCount = packetCount;
	}
	public long getPacketSize() {
		return packetSize;
	}
	public void setPacketSize(long packetSize) {
		this.packetSize = packetSize;
	}
	public int getSentCount() {
		return sentCount;
	}
	public void setSentCount(int sentCount) {
		this.sentCount = sentCount;
	}
	public int getReceivedCount() {
		return receivedCount;
	}
	public void setReceivedCount(int receivedCount) {
		this.receivedCount = receivedCount;
	}
	public double getLost() {
		return lost;
	}
	public void setLost(double lost) {
		this.lost = lost;
	}
    public Date getPingTime() {
        return pingTime;
    }
    public void setPingTime(Date pingTime) {
        this.pingTime = pingTime;
    }
    public Date getScheTime() {
        return scheTime;
    }
    public void setScheTime(Date scheTime) {
        this.scheTime = scheTime;
    }
	
}
