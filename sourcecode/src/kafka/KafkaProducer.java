package kafka;
import java.net.ConnectException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {

	String brokers = null;
	String topic = null;

	Producer<Integer, String> producer;
	private final Properties props = new Properties();

	public int partitionMethod = -1; // random

	public KafkaProducer(String brokers) {
		this(brokers, null);
	}

	/**
	 * 
	 * @param brokers
	 *            "192.168.1.6:9092,192.168.1.7:9092,192.168.1.8:9092,192.168.1.8:9092,192.168.1.9:9092"
	 * @param topic
	 */
	public KafkaProducer(String brokers, String topic) {
		this.brokers = brokers;
		this.topic = topic;
		init();
	}
	
	public void init(){
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("metadata.broker.list", brokers);
		props.put("partitioner.key", String.valueOf(partitionMethod));
		props.put("partitioner.class", "com.cucc.cdrmonitor.kafka.SimplePartitioner");

		producer = new Producer<Integer, String>(new ProducerConfig(props));
	}

	public void send(String content) throws Exception{
		KeyedMessage<Integer, String> message = new KeyedMessage<Integer, String>(
				topic, content);
		//producer.send(message);	
	}

	public void send(List<String> contents) throws Exception {
		List<KeyedMessage<Integer, String>> ms = new LinkedList<KeyedMessage<Integer, String>>();
		for (String i : contents) {
			ms.add(new KeyedMessage<Integer, String>(topic, i));
		}
		producer.send(ms);
	}
	
	
	public void close(){
		try{
			producer.close();
		} catch(Exception e){
			
		}
	}

}
