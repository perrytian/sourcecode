package ping;


public interface PingOper
{
	public PingResult pingTarget(String ip,Long packetsize,Long packetcount,Long timeout) throws Exception;
	public void output(PingResult result);

}

