package ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;


public class PingOperImpl implements PingOper{
	
	private final String SENT = "Sent = ";
	private final String CN_SENT = "已发送 = ";
	private final String RECEIVED = "Received = ";
	private final String CN_RECEIVED = "已接收 = ";
	private final String LOST = "Lost = ";
	private final String CN_LOST = "丢失 = ";
	
	private final String MINIMUM = "Minimum = ";
	private final String CN_MINIMUM = "最短 = ";
	private final String MAXIMUM = "Maximum = ";
	private final String CN_MAXIMUM = "最长 = ";
	private final String AVERAGE = "Average = ";
	private final String CN_AVERAGE = "平均 = ";
	
	private final String PACKETS = "Packets:";
	private final String CN_PACKETS = "数据包:";
	
	private final String PACKETSENT = "packets transmitted";
	private final String PACKETRECEIVED = "received";
	private final String PACKETLOSS = "packet loss";
	private final String TIME = "time";
	private final String RTT_TITLE = "rtt min/avg/max/mdev";
	
	
	public static boolean isIPAddress(String in)
	{
		boolean rtn = false;
		if(in == null){
			rtn = false;
			return rtn;
		}
		String ipList[] = in.split("\\.");
		if(ipList.length != 4)
		{
			rtn = false;
			return rtn;
		}
		rtn = true;
		for (int i = 0; i < ipList.length; i++)
		{
			int value = Integer.parseInt(ipList[i]);
			if(value < 0 || value > 255)
			{
				rtn = false;
				break;
			}
		}
		return rtn;
	}

	public PingResult pingTarget(String ip,Long packetsize,Long packetcount,Long timeout) throws Exception
	{
		if(!isIPAddress(ip))
		{
			Logger.out(Logger.ERROR, "IP地址格式错误:["+ip+"] ，ping测试失败");
			return null;
		}
		
		String osName = System.getProperty("os.name").toLowerCase();
		
		StringBuffer command = new StringBuffer();
		if(osName.contains("windows"))
		{
			command.append("ping");
			if(packetcount > 0){
				command.append(" -n "+packetcount);
			}
			if(packetsize > 0){
				command.append(" -l "+packetsize);
			}
			if(timeout > 0){
				command.append(" -w "+timeout);
			}
			
			command.append(" "+ip);
//			Logger.out(Logger.DEBUG, "运行PING测试:"+command.toString());
		}
		else if(osName.contains("linux"))
		{
			command.append("ping");
			if(packetcount > 0 ){
				command.append(" -c "+packetcount);
			}
			if(packetsize > 0){
				command.append(" -s "+packetsize);
			}
			/*
			if(timeout > 0){
				command.append(" -W "+timeout);
			}*/
			command.append(" "+ip);
//			Logger.out(Logger.DEBUG, "运行PING测试:"+command.toString());
		}
		else
		{
			command.append("ping " + ip + " 2");
			Logger.out(Logger.DEBUG, "运行PING测试:"+command.toString());
		}

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		// 请自行修改上面的ip位址
		try
		{
			process = runtime.exec(command.toString());
			is = process.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			if(osName.contains("windows")){
				return generateWindowsPingResult(ip,packetsize,packetcount,br);
			}else if(osName.contains("linux")){
				return generateLinuxPingResult(ip,packetsize,packetcount,br);
			}else{
				Logger.out(Logger.WARN, "操作系统:"+osName+" 没有处理PING测试结果的解析类");
				return null;
			}
		}
		catch(IOException e)
		{
			Logger.out(Logger.ERROR, e.getMessage());
			runtime.exit(1);
			return null;
		}finally{
			if(is != null){
				is.close();
			}
			if(isr != null){
				isr.close();
			}
			if(br != null){
				br.close();
			}
			if(process != null){
				process.destroy();
			}
			
		}

	}
	
	
	public static void main(String[] args) throws Exception{
		
		String s="\n";
		if(s.contains("你好")||s.contains("hello")){
			System.out.println("sdf");
		}
		System.out.println(1);
		System.out.println(s);
		System.out.println(1);
//		String common = "ping -n 5 -l 16 -w 1000  202.99.45.169";
//		
//		Runtime runtime = Runtime.getRuntime();
//		Process process = runtime.exec(common);
//		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//		String line=null;
//		StringBuffer sb = new StringBuffer();
//		int i =1;
//		while((line=br.readLine())!=null){
//			sb.append(i++).append("=======").append(line).append("\n");
//		}
//		System.out.print(sb.toString());
	}
	
	
	/**
	 * 解析生成WINDOWS PING测试结果
	 * @param ip
	 * @param packetsize
	 * @param packetcount
	 * @param br
	 * @return
	 */
	private PingResult generateWindowsPingResult(String ip,Long packetsize,Long packetcount,BufferedReader br){
		int sentCount = 0;
		int receivedCount = 0;
		double lostPercent = 0.0;
		
		int minimum = 0;
		int maximum = 0;
		int average = 0;
		
		String line = null;
		try {
			boolean isResult = false;
			while((line = br.readLine()) != null)
			{
				if(line.startsWith("请求超时")||line.toLowerCase().contains("request timed out."))
				{
					Logger.out(Logger.ERROR, "PING请求超时:测试IP["+ip+"]");
//					System.out.println("PING请求超时:测试IP["+ip+"]");
					continue;
				}
				else if(line.toLowerCase().startsWith("no answer from")
					|| line.toLowerCase().contains("unreachable")
					|| line.toLowerCase().contains("Destination Host Unreachable")
					|| line.contains("无法访问目标主机"))
				{
					Logger.out(Logger.ERROR, "PING请求无响应或目标不可达：测试IP["+ip+"]");
//					System.out.println("PING请求无响应或目标不可达：测试IP["+ip+"]");
					continue;
				}
				else if(line.toLowerCase().contains("request could not find host")
						|| line.contains("请求找不到主机")){
//					System.out.println("PING 请求找不到主机:测试目标["+ip+"]");
					Logger.out(Logger.ERROR, "PING 请求找不到主机:测试目标["+ip+"]");
					continue;
				}
				
				if(line.trim().startsWith(PACKETS)){
					isResult = true;
					String tempLine = line.trim().substring(line.trim().indexOf(PACKETS)+PACKETS.length());
					String[] tempResults = tempLine.split(",");
					for(String temp:tempResults){
						if(temp.trim().startsWith(SENT)){
							sentCount = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(SENT)+SENT.length()));
						}
						if(temp.trim().startsWith(RECEIVED)){
							receivedCount = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(RECEIVED)+RECEIVED.length()));
						}
						if(sentCount >0 && receivedCount >=0){
							lostPercent = ((double)(sentCount-receivedCount)/(double)sentCount)*100;
							lostPercent = round(lostPercent,2,BigDecimal.ROUND_HALF_UP);
						}
					}
					
				}
				if(line.trim().startsWith("Minimum")){
					isResult = true;
//					System.out.println(line);
					String[] tempResults =line.trim().split(",");
					for(String temp:tempResults){
						if(temp.trim().startsWith(MINIMUM)){
							minimum = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(MINIMUM)+MINIMUM.length(),temp.trim().indexOf("ms")));
//						System.out.println(minimum);
						}
						if(temp.trim().startsWith(MAXIMUM)){
							maximum = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(MAXIMUM)+MAXIMUM.length(),temp.trim().indexOf("ms")));
//						System.out.println(maximum);
						}
						if(temp.trim().startsWith(AVERAGE)){
							average = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(AVERAGE)+AVERAGE.length(),temp.trim().indexOf("ms")));
//						System.out.println(average);
						}
					}
				}
				
				if(line.trim().contains(CN_PACKETS)){
					isResult = true;
					String tempLine = line.trim().substring(line.trim().indexOf(CN_PACKETS)+CN_PACKETS.length());
					String[] tempResults = tempLine.split("，");
					for(String temp:tempResults){
						if(temp.trim().startsWith(CN_SENT)){
							sentCount = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(CN_SENT)+CN_SENT.length()));
						}
						if(temp.trim().startsWith(CN_RECEIVED)){
							receivedCount = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(CN_RECEIVED)+CN_RECEIVED.length()));
						}
						if(sentCount >0 && receivedCount >=0){
							lostPercent = ((double)(sentCount-receivedCount)/(double)sentCount)*100;
							lostPercent = round(lostPercent,2,BigDecimal.ROUND_HALF_UP);
						}
					}
				}
				
				if(line.trim().contains(CN_MINIMUM)){
					isResult = true;
					String[] tempResults = line.trim().split("，");
					for(String temp:tempResults){
						if(temp.trim().startsWith(CN_MINIMUM)){
							minimum = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(CN_MINIMUM)+CN_MINIMUM.length(),temp.trim().indexOf("ms")));
						}
						if(temp.trim().startsWith(CN_MAXIMUM)){
							maximum = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(CN_MAXIMUM)+CN_MAXIMUM.length(),temp.trim().indexOf("ms")));
						}
						if(temp.trim().startsWith(CN_AVERAGE)){
							average = Integer.parseInt(temp.trim().substring(temp.trim().indexOf(CN_AVERAGE)+CN_AVERAGE.length(),temp.trim().indexOf("ms")));
						}
					}
				}
			}
			
			if(!isResult){
				Logger.out(Logger.WARN, "PING测试失败IP["+ip+"]");
//				System.out.println("PING测试失败");
			}
			PingResult result = new PingResult();
			result.setIp(ip);
			result.setPacketSize(packetsize);
			result.setPacketCount(packetcount);
			
			result.setSentCount(sentCount);
			result.setReceivedCount(receivedCount);
			result.setLost(lostPercent);
			result.setRttMinimum(minimum);
			result.setRttMaxmum(maximum);
			result.setRttAverage(average);
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	public double round(double value,int scale,int roundingMode){
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale,roundingMode);
		double ret = bd.doubleValue();
		bd = null;
		return ret;
	}
	
	
	/**
	 * 解析生成LINUX PING测试结果
	 * @param ip
	 * @param packetsize
	 * @param packetcount
	 * @param br
	 * @return
	 */
	private PingResult generateLinuxPingResult(String ip,Long packetsize,Long packetcount,BufferedReader br){
		int sentCount = 0;
		int receivedCount = 0;
		double lostPercent = 0.0;
		
		double minimum = 0.0;
		double maximum = 0.0;
		double average = 0.0;
		
		String line = null;
		try {
			boolean isResult = false;
			StringBuffer sb = new StringBuffer();
			int i =1;
			while((line = br.readLine()) != null)
			{
				sb.append("\n===%&&====").append(i++).append("===========").append(line);

				if(line.contains("请求超时")||line.toLowerCase().contains("request timed out."))
				{
					Logger.out(Logger.ERROR, "PING请求超时:测试IP["+ip+"]:" + line);
					continue;
				}
				else if(line.toLowerCase().startsWith("no answer from")
					|| line.toLowerCase().contains("unreachable")
					|| line.toLowerCase().contains("destination host unreachable"))
				{
					Logger.out(Logger.ERROR, "PING请求无响应或目标不可达：测试IP["+ip+"]:" + line);
					continue;
				}
				
				
				if(line.trim().contains("ping statistics")){
					isResult = true;
					continue;
				}
				
				if(isResult){
					//处理PING测试结果
					if(line.trim().contains(PACKETSENT)){
						String[] temp = line.trim().split(",");
						for(String iter:temp){
							if(iter.trim().contains(PACKETSENT)){
								sentCount = Integer.parseInt(iter.trim().substring(0,iter.indexOf(PACKETSENT)-1));
							}
							if(iter.trim().contains(PACKETRECEIVED)){
								receivedCount = Integer.parseInt(iter.trim().substring(0, iter.trim().indexOf(PACKETRECEIVED)-1));
								if(receivedCount<1){
									Logger.out(Logger.DEBUG, "\n===%&&====ping 结果是："+sb.toString());
								}
							}
							if(sentCount >0 && receivedCount >=0){
								lostPercent = ((double)(sentCount-receivedCount)/(double)sentCount)*100;
								lostPercent = round(lostPercent,2,BigDecimal.ROUND_HALF_UP);
							}
						}
					}
					if(line.trim().contains(RTT_TITLE)){
						String tempLine = line.replace(" ", "");
						String resultLine = tempLine.substring(tempLine.indexOf("=")+1, tempLine.indexOf("ms"));
						String[] rttResult = resultLine.split("/");
						minimum = Double.parseDouble(rttResult[0]);
						average = Double.parseDouble(rttResult[1]);
						maximum = Double.parseDouble(rttResult[2]);
					}
				}
			}
			
			if(!isResult){
				Logger.out(Logger.WARN, "PING测试失败IP["+ip+"]");
//				System.out.println("PING测试失败");
			}
			PingResult result = new PingResult();
			result.setIp(ip);
			result.setPacketSize(packetsize);
			result.setPacketCount(packetcount);
			
			result.setSentCount(sentCount);
			result.setReceivedCount(receivedCount);
			result.setLost(lostPercent);
			result.setRttMinimum(minimum);
			result.setRttMaxmum(maximum);
			result.setRttAverage(average);
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * 结果输出
	 */
	public void output(PingResult result){
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n===========PING测试参数=================\n");
		buffer.append("测试目标IP："+result.getIp()+"\n");
		buffer.append("测试包大小："+result.getPacketSize()+"(bytes)\n");
		buffer.append("测试包计数："+result.getPacketCount()+"\n");
		buffer.append("===========PING测试结果=================\n");
		
		buffer.append("发送包计数："+result.getSentCount()+"\n");
		buffer.append("接收包计数："+result.getReceivedCount()+"\n");
		buffer.append("丢     包     率："+result.getLost()+"%\n");
		buffer.append("最小时延："+result.getRttMinimum()+"ms\n");
		buffer.append("最大时延："+result.getRttMaxmum()+"ms\n");
		buffer.append("平均时延："+result.getRttAverage()+"ms\n");
		buffer.append("========================================\n");
//		System.out.println(buffer.toString());
		Logger.out(Logger.INFO, buffer.toString());
	}
	
//	public static void main(String[] args)
//	{
//		// Ping test = new Ping();
////		Vector<String> ipList = new Vector<String>();
//		// String ip = ipList.get(i);
//		//String ip = "172.0.58.12";
//		//String ip = "219.158.66.246";
//		String ip = "176.25.25.25";
//		PingOper ping = new PingOperImpl();
//		try {
//			PingResult pingResult = ping.pingTarget(ip,32L,5L,5000L);
//			ping.output(pingResult);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
