package socket.demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import util.ArraySearch;
import config.xml.DESC.ConfigManager;


public class Dispatcher  implements Runnable{
	
	    private static Logger logger = Logger.getLogger(Dispatcher.class);
		
		// 中断标志
		private boolean isRunning = false;
		private int delayPar;
		private Thread thread;
		
		//监听端口
		private int port ;
		
		private InetSocketAddress serverAddress;
		private Selector selector = null;
		private ServerSocketChannel serverChannel = null;
		//private HashSet<SocketInstance> connecting = new HashSet<SocketInstance>();
		
		private ProcessorEngine processorEngine;
		private int[] ipArray = null;
		
		public Dispatcher(ProcessorEngine processorEngine){
			this.processorEngine = processorEngine;
			initIpArray();
		}
		
		private void initIpArray() {
			List<String> ips = ConfigManager.getInstance().getIps();
			ipArray = new int[ips.size()];
			int j = 0;
			for (int k = 0; k < ips.size(); k++) {
				try {
					ipArray[j++] = InetAddress.getByName(ips.get(k)).hashCode();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			Arrays.sort(ipArray);
		}
	
		public void run(){
			while(isRunning){
				try{
					selector();
					if(isRunning)
						open();
				}catch(IOException e){
					e.printStackTrace();
				}catch(Throwable e){
					logger.error("Dispatcher异常:",e);
				}finally{
					close();
				}
			}
		}
	
		private void open()throws IOException {
			// 初始化，分别实例化一个选择器，一个服务器端可选择通道
			selector = Selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(serverAddress); // 将该套接字绑定到服务器某一可用端口
			serverChannel.register(selector,SelectionKey.OP_ACCEPT);
		}
	// 结束时释放资源 throws IOException
		private void close(){
			//关闭所有客户端
			for(SelectionKey key : selector.keys()){
				Channel sc = (Channel)key.channel();
				try{
					if(sc.isOpen())
						sc.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}

			//关闭侦听
			try{
				if(serverChannel!=null&&serverChannel.isOpen()){
					serverChannel.close();
					serverChannel = null;
				}
				if(selector.isOpen())
					selector.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		public void start(){
			
			String serverAddress = ConfigManager.getInstance().getServerAddress();
			port = ConfigManager.getInstance().getServerPort();
			
			if(serverAddress!=null &&serverAddress.length()>0&& port>0){
				this.serverAddress = new InetSocketAddress(serverAddress,port);
			}else{
				//默认监听
				this.serverAddress = new InetSocketAddress("0.0.0.0",3304);
			}
			try {
				open();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			isRunning=true;
			delayPar = 1;
			thread = new Thread(this,"Socket dispatcher");
			thread.start();
			logger.info("HDAP 服务监听引擎启动:监听地址["+serverAddress+"],监听端口["+port+"]");
			synchronized(this){
				try {
					wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		public void stop(){
			isRunning = false;
			selector.wakeup();
			try{
				thread.join();
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
			thread = null;
		}
		
		public boolean isRunning(){
			return isRunning;
		}
		
	
	
	     // 监听端口，当通道准备好时进行相应操作 throws IOException, InterruptedException
		private void selector() {
			// 服务器端通道注册OP_ACCEPT事件
			try {
				// 当有(已注册的事件)发生时,select()返回值将大于0
				int acceptCount;
				while ( (acceptCount=selector.select(delayPar)) >= 0&&isRunning) {
					
					if(acceptCount>0){
						// 取得所有已经准备好的所有选择键 使用迭代器对选择键进行轮询
						Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
						while (keys.hasNext()) {
							SelectionKey key = keys.next();
							keys.remove();
							selector.selectedKeys().remove(key);// 删除当前将要处理的选择键
							if (key.isAcceptable()) { // 如果是有客户端连接请求
								
								ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
								SocketChannel sc = ssc.accept();
								
								int ipHashCode = sc.socket().getInetAddress()
										.hashCode();

								/*logger.info("发现客户端连接请求：对端IP地址["
										+ sc.socket().getInetAddress()
												.getHostAddress() + "],对方端口["
										+ sc.socket().getPort() + "]");*/
								if (ArraySearch.splitHalf(ipArray, ipHashCode, 0, ipArray.length - 1) != -1) {
									/*if(logger.isDebugEnabled()){
										logger.debug("连接请求：对端IP地址["
												+ sc.socket().getInetAddress()
														.getHostAddress() + "]");
									}*/
									// 设置对应的通道为异步方式并注册感兴趣事件
									sc.configureBlocking(false);
									// 将注册的事件与该套接字联系起来
									SelectionKey readRemoteKey = sc.register(
											selector, SelectionKey.OP_READ);
									SocketInstance remoteInstance = new SocketInstance(
											readRemoteKey, processorEngine,
											ConfigManager.getInstance()
													.getIdleTime());
									readRemoteKey.attach(remoteInstance);
								} else {
									logger.info("非法连接请求：对端IP地址["
											+ sc.socket().getInetAddress()
													.getHostAddress() + "]");
									try{
										sc.socket().close();
									}catch(IOException e){
										e.printStackTrace();
									}
									
								}
							}
							/*else if(key.isConnectable()){
								logger.info("isConnectable");
								SocketChannel sc = (SocketChannel)key.channel();
								SocketInstance si = (SocketInstance)key.attachment();
								if(sc.isConnectionPending()){
									try{
										sc.finishConnect();
										SelectionKey newKey = key.interestOps(SelectionKey.OP_READ);
										si.setSelectionKey(newKey);
										newKey.attach(si);
										si.connected();
									}catch(IOException e){
										si.disconnect();
									}
								}else{
									si.disconnect();
								}
								
							}*/
							else if (key.isReadable()) { // 如果是通道读准备好事件
								readFromRemote(key);
							}else if (key.isWritable()) { // 如果是通道写准备好事件
								// 取得套接字后处理，方法同上
								writeToRemote(key);
							}else if(!key.isValid()){
								SocketInstance si = (SocketInstance)key.attachment();
								si.disconnect();
							}
						}
					}
					//产生刷新
					for(SelectionKey key:selector.keys()){
						SocketInstance si = (SocketInstance)key.attachment();
						if(si!=null)
							si.refresh();
					}
					//connects();
					if(delayPar==1){
						delayPar = 1000;
						synchronized(this){
							notifyAll();
						}
					}
				}
			} catch (ClosedChannelException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		/**
		 * 调用SocketChannel的connect()方法连接远端，得到socketChannel实例
		 * @param instance 
		
		private void connects(){
			synchronized(connecting){
				Iterator<SocketInstance> i = connecting.iterator();
				while(i.hasNext()){
					SocketInstance instance = i.next();
					i.remove();
					try{
						SocketChannel channel = SocketChannel.open();
						channel.configureBlocking(false);
						channel.connect(instance.getSocketAddress());
						SelectionKey key = channel.register(selector, SelectionKey.OP_CONNECT, instance);
						instance.setSelectionKey(key);
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		 */
		/*
		public void connect(SocketInstance instance){
			synchronized(connecting){
				connecting.add(instance);
			}
			wakeup();
		}
		*/
		public void wakeup(){
			if(isRunning)
				selector.wakeup();
		}
		
		// 对通道的写操作
		public void writeToRemote(SelectionKey key) {
			SocketInstance si = (SocketInstance)key.attachment();
			try {
				si.dispatcherWrite(this);
			} catch (IOException ex) {
				ex.printStackTrace();
				si.disconnect();
			}
		}

		// 对通道的读操作
		public void readFromRemote(SelectionKey key) {
			// 取得选择键对应的通道和套接字
			SocketInstance si = (SocketInstance)key.attachment();
			try{
				si.dispatcherRead();
			} catch (IOException ex) {
				ex.printStackTrace();
				si.disconnect();
			}
		}
		
}
