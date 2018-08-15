package service;


import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import thread.ConsumerThread;
import utils.CommonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/6/8 0008.
 */
public class Consumer {

    private final ConsumerConnector connector;
    private final String topic;
    private final Integer threadNum;
    private ExecutorService executor;
    /**
     * 创建消费者的配置类
     * @return
     */
    public ConsumerConfig createConsumerConfig(){
        return new ConsumerConfig(CommonUtils.loadProperties(1));
    }

    public Consumer(String topic,Integer threadNum){
        this.connector = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
        this.topic = topic;
        this.threadNum = threadNum;
    }

    public void startWork(){
        Map<String,Integer> topicMap = new HashMap<>();
        topicMap.put(topic,threadNum);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = connector.createMessageStreams(topicMap);
        List<KafkaStream<byte[], byte[]>> kafkaStreams = messageStreams.get(topic);
        executor = Executors.newFixedThreadPool(threadNum);
        for (final KafkaStream<byte[], byte[]> kafkaStream:kafkaStreams){
            executor.submit(new ConsumerThread(kafkaStream));
        }
    }
}
