package com.berkay;

import com.berkay.Database.JDBC;
import com.berkay.Kafka.SampleConsumer;
import com.berkay.Kafka.SampleProducer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StartWebApplication {

    public static final String TOPIC = "topicForTEB";
    private static JDBC database;

    public static void main(String[] args) {
        boolean isAsync = false;
        database = new JDBC();
        database.createDatabase();
        database.createTable();
        SpringApplication.run(StartWebApplication.class, args);

        SampleProducer producerThread = new SampleProducer(TOPIC, isAsync);
        producerThread.start();
        SampleConsumer consumerThread = new SampleConsumer();
        consumerThread.start();
//

    }

}