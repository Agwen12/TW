package org.example;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static final int BUFF_SIZE = 10;
    public static final int PRODUCER_COUNT = 2;
    public static final int CONSUMER_COUNT = 2;



    public static void main(String[] args) {

        List<Thread> producers = new LinkedList<>();
        List<Thread> consumers = new LinkedList<>();

        Monitor m = new Monitor(BUFF_SIZE);

        for (int i = 0; i < PRODUCER_COUNT; i++) {
            producers.add(new Thread(new Producer(m), "Producer-Thread-" + i));
        }

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            consumers.add(new Thread(new Consumer(m), "Consumer-Thread-" + i));
        }

        consumers.forEach(Thread::start);
        producers.forEach(Thread::start);
    }
}