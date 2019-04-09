package com.berkay.Kafka;

import com.berkay.Database.JDBC;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

import static com.berkay.StartWebApplication.TOPIC;

public class SampleConsumer extends Thread {
    private KafkaConsumer<String, String> kafkaConsumer;
    private String topicName;
    private JDBC databaseInsert;
    private String consumerArray[];
    private String comparisonArray[];
    public static ArrayList<Long> arrayForIter;
    public static KafkaConsumer<String, String> kafkaConsumerNew;
    public static int istCounter = 0;
    public static int mosCounter = 0;
    public static int tokCounter = 0;
    public static int beiCounter = 0;
    public static int lonCounter = 0;

    public SampleConsumer() {
        databaseInsert = new JDBC();
        String bootstrapServer = "localhost:9092";
        String keyDeserializer = StringDeserializer.class.getName();
        String valueDeserializer = StringDeserializer.class.getName();

        String groupID = "consumerGroup";
        String offsetReset  = "earliest";
        arrayForIter = new ArrayList<>();

        topicName = TOPIC;

        Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,keyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,valueDeserializer);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,groupID);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,offsetReset);

        kafkaConsumer = new KafkaConsumer<>(properties);
//        kafkaConsumerNew = new KafkaConsumer<>(properties);
//        kafkaConsumerNew.subscribe(Collections.singleton(topicName));

//            ConsumerRecords<String, String> consumerRecordsNew = kafkaConsumerNew.poll(5000);
//            for(ConsumerRecord<String, String> cons : consumerRecordsNew){
//                arrayForIter.add(cons.offset());
//                comparisonArray = cons.value().split(" ");
//
//            }


    }


    @Override
    public void run() {
        kafkaConsumer.subscribe(Arrays.asList(topicName));
        while(true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(5000);
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
//                System.out.println(consumerRecord.value());
                consumerArray = consumerRecord.value().split(" ");
                databaseInsert.insertTable(consumerRecord.value(),consumerArray[3],consumerArray[0],consumerArray[1]);
                arrayForIter.add(consumerRecord.offset());
                switch (consumerArray[3]){
                    case "Istanbul":
                        istCounter++;break;
                    case "Moscow":
                        mosCounter++;break;
                    case "Tokyo":
                        tokCounter++;break;
                    case "London":
                        lonCounter++;break;
                    case "Beijing":
                        beiCounter++;break;
                    default:break;
                }
            }

        }
    }


}
