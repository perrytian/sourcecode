package socket.demo;

import bytes.util.ByteArray;


public class Packet {
	
	private H2Header header = new H2Header();
	private byte[] data;
	
	public H2Header getHeader() {
		return header;
	}
	public void setHeader(H2Header header) {
		this.header.setHeader(header.getBytes());
	}
	public byte[] getData() {
		return data;
	}
	public void setBody(byte[] data) {
		header.setA1PacketSize(H2Constant.HEADER_LENGTH+data.length);
		this.data = data;
	}
	
	public byte[] toBytes(){
		ByteArray buffer = new ByteArray();
		buffer.append(header.getBytes());
		buffer.append(data);
		return buffer.toArray();
	}
}
