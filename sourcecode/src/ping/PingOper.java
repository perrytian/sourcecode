package ping;

import com.cusi.cnms3.adapter.object.PingResult;


public interface PingOper
{
	public PingResult pingTarget(String ip,Long packetsize,Long packetcount,Long timeout) throws Exception;
	public void output(PingResult result);

}

