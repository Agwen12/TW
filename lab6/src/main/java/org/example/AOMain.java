package org.example;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AOMain {

    public static final Logger logger = Logger.getLogger(AOMain.class);
    public static long timeOut;


    public static void main(String[] args) throws IOException {
        AOMain.setProperties();
        PropertyConfigurator.configure(System.getProperty("logger.configuration"));
        int consumerCount = Integer.parseInt(System.getProperty("producer.count"));
        int producersCount = Integer.parseInt(System.getProperty("consumer.count"));
        timeOut = Integer.parseInt(System.getProperty("task.timeout")) * 1000L;
        logger.info("Consumers: " + consumerCount);
        logger.info("Producers: " + producersCount);
        logger.info("Seed: " + System.getProperty("seed"));
        logger.info("Task timeout: " + timeOut);
        logger.info("Pid file: " + System.getProperty("pid.file"));
        logger.info("Logger configuration: " + System.getProperty("logger.configuration"));
        AOMain.writePid();

        ActiveObject activeObject = new ActiveObject();
        Thread activeObjectThread = new Thread(activeObject,"AO-Thread");
        activeObjectThread.start();

        Iterator<Integer> i1 = IntStream.range(0, consumerCount).iterator();
        Iterator<Integer> i2 = IntStream.range(0, producersCount).iterator();
        List<Thread> consumers = Stream
                .generate(() ->
                        new Thread(
                                new Client(Modes.CONSUMER, activeObject),
                                "Consumer-Thread-" + i1.next()))
                .limit(consumerCount)
                .toList();
        List<Thread> producers = Stream
                .generate(() ->
                        new Thread(
                                new Client(Modes.PRODUCER, activeObject),
                        "Producer-Thread-" + i2.next()))
                .limit(producersCount)
                .toList();

        consumers.forEach(Thread::start);
        producers.forEach(Thread::start);
    }

    public static void writePid() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("pid.file")));
            writer.write(String.valueOf(ProcessHandle.current().pid()));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setProperties() {
        InputStream input;
        String props = System.getProperty("properties.file");

        if (props != null && System.getProperty("seed") == null) {
            try {
                if (System.getProperty("seed") == null) {
                    // read from default
                    input = AOMain.class.getClassLoader().getResourceAsStream("app.properties");
                } else {
                    // read from defied file
                    input = new FileInputStream(System.getProperty("properties.file"));
                }
                Properties p = new Properties();
                p.load(input);
                p.forEach((k, v) -> System.setProperty((String) k, (String) v));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}