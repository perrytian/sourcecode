package socket.demo;

import java.util.List;


/**
 * 
 * @author jianghao
 * 
 */
public class Request {

	private H2Header header;

	private List<byte[]> params;

	public H2Header getHeader() {
		return header;
	}

	public void setHeader(H2Header header) {
		this.header = header;
	}

	public List<byte[]> getParams() {
		return params;
	}

	public void setParams(List<byte[]> params) {
		this.params = params;
	}

	public String toString(){
		StringBuffer ret = new StringBuffer();
		String serviceName = ServiceCodeConstant.serviceCodeMap.get(header.getA3ServiceCode());
		if(serviceName!=null)
			ret.append(ServiceCodeConstant.serviceCodeMap.get(header.getA3ServiceCode()));
		else{
			ret.append('?');
			ret.append('(');
			ret.append(new String(header.getBytes(),6,6));
			ret.append(')');
		}
		if(params!=null){
			ret.append(':');
			for(int i=0;i<params.size();i++){
				ret.append('(');
				ret.append(new String(params.get(i)));
				ret.append(')');
			}
		}
		return ret.toString();
	}
}
