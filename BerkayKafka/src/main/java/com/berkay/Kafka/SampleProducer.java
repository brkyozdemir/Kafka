package com.berkay.Kafka;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SampleProducer extends Thread {
    private static final Logger log = Logger.getLogger(SampleProducer.class);
    private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Boolean isAsync;
    private Timestamp timestamp;
    private Date date;

    public static final String[] cityNames = {
            "Istanbul",
            "Tokyo",
            "Moscow",
            "Beijing",
            "London"
    };

    public static final String[] logLevels = {
            "WARN",
            "FATAL",
            "DEBUG",
            "ERROR",
            "INFO"
    };

    public static final String KAFKA_SERVER_URL = "localhost";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final String CLIENT_ID = "SampleProducer";

    public SampleProducer(String topic, Boolean isAsync) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKA_SERVER_URL + ":" + KAFKA_SERVER_PORT);
        properties.put("client.id", CLIENT_ID);
        properties.put("linger.ms", 500);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(properties);
        this.topic = topic;
        this.isAsync = isAsync;
    }

    public void run() {
        Random random = new Random();
        int messageNo = 1;

        while (true) {
            date = new Date();
            timestamp = new Timestamp(date.getTime());
            String randomLevel = logLevels[random.nextInt(logLevels.length)];
            String randomCity = cityNames[random.nextInt(cityNames.length)];
            String desc = "Hello-from-" + randomCity;
            String messageStr = timestamp + " " + randomLevel + " " + randomCity + "     " + desc;
            log.fatal(messageStr);
            long startTime = System.currentTimeMillis();
            if (isAsync) { // Send asynchronously
                producer.send(new ProducerRecord<>(topic,
                        messageNo,
                        messageStr), new DemoCallBack(startTime, messageNo, messageStr));
            } else { // Send synchronously
                try {
                    producer.send(new ProducerRecord<>(topic,
                            messageNo,
                            messageStr)).get();
//                    System.out.println("Sent message: (" + messageNo + ", " + messageStr + ")");
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            messageNo++;
        }
    }
}

class DemoCallBack implements Callback {

    private final long startTime;
    private final int key;
    private final String message;

    public DemoCallBack(long startTime, int key, String message) {
        this.startTime = startTime;
        this.key = key;
        this.message = message;
    }

    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
//            System.out.println(
//                    "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
//                            "), " +
//                            "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }

}
