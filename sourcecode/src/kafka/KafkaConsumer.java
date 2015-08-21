
package kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

public class KafkaConsumer {

	ConsumerConnector consumerConn ;
	
	KafkaStream<byte[], byte[]> kafkaStream = null;
	
	ConsumerIterator<byte[], byte[]> consumerIter = null;
	
	String zookeeper;
	String groupId ;
	String topic;
	
	/**
	 * 
	 * @param zookeeper "192.168.1.1:2181,192.168.1.2:2181"
	 */
	public KafkaConsumer(String zookeeper, String groupId, String topic) {
		this.zookeeper = zookeeper;
		this.groupId = groupId;
		this.topic = topic;
		
		init();
	}
	
	public void init(){
		
		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeper);
		props.put("group.id", groupId);
		props.put("auto.offset.reset", "smallest");
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "2000");
		props.put("auto.commit.interval.ms", "1000");

		ConsumerConfig consumerConfig = new ConsumerConfig(props);

		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		
		consumerConn = Consumer.createJavaConsumerConnector( consumerConfig );
		
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConn
				.createMessageStreams(topicCountMap);
		
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
		
		kafkaStream = streams.get(0);
		
		
		consumerIter = kafkaStream.iterator();
	}

	public boolean hasNext(){
		return consumerIter.hasNext();
	}
	
	public MessageAndMetadata<byte[], byte[]> next(){
		return consumerIter.next();
	}
	
	public byte[] getNext(){
		byte[] ret = null ;
		if( consumerIter.hasNext() ){
			ret = consumerIter.next().message();
		}
		return ret;
	}
	
}
