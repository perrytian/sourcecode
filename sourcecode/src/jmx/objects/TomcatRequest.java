package jmx.objects;

import java.io.Serializable;

/**
 * Tomcat Request
 * @author oss
 *
 */
public class TomcatRequest implements Serializable{
	
	private String connectorName;//http-8080,kj
	
	private int bytesSent;
	
	private String method;
	
	private String remoteAddr;
	
	private long requestBytesSent;
	
	private int contentLength;
	
	private long bytesReceived;
	
	private long requestProcessingTime;
	
	private String protocol;
	
	private String currentQueryString;
	
	private String maxRequestUri;
	
	private long requestBytesReceived;
	
	private int serverPort;
	
	private int stage;
	
	private int requestCount;
	
	private long maxTime;
	
	private long processingTime;
	
	private String currentUri;
	
	private int errorCount;
	

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public int getBytesSent() {
		return bytesSent;
	}

	public void setBytesSent(int bytesSent) {
		this.bytesSent = bytesSent;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public long getRequestBytesSent() {
		return requestBytesSent;
	}

	public void setRequestBytesSent(long requestBytesSent) {
		this.requestBytesSent = requestBytesSent;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public long getBytesReceived() {
		return bytesReceived;
	}

	public void setBytesReceived(long bytesReceived) {
		this.bytesReceived = bytesReceived;
	}

	public long getRequestProcessingTime() {
		return requestProcessingTime;
	}

	public void setRequestProcessingTime(long requestProcessingTime) {
		this.requestProcessingTime = requestProcessingTime;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getCurrentQueryString() {
		return currentQueryString;
	}

	public void setCurrentQueryString(String currentQueryString) {
		this.currentQueryString = currentQueryString;
	}

	public String getMaxRequestUri() {
		return maxRequestUri;
	}

	public void setMaxRequestUri(String maxRequestUri) {
		this.maxRequestUri = maxRequestUri;
	}

	public long getRequestBytesReceived() {
		return requestBytesReceived;
	}

	public void setRequestBytesReceived(long requestBytesReceived) {
		this.requestBytesReceived = requestBytesReceived;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	public String getCurrentUri() {
		return currentUri;
	}

	public void setCurrentUri(String currentUri) {
		this.currentUri = currentUri;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	
}
