package org.example;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {

    public static final int BUFF_SIZE = 20;
    public static final int PRODUCER_COUNT_BIG = 2;
    public static final int PRODUCER_COUNT_SMALL = 3;

    public static final int CONSUMER_COUNT_BIG = 2;
    public static final int CONSUMER_COUNT_SMALL = 3;



    public static void main(String[] args) {

        List<Thread> producers = new LinkedList<>();
        List<Thread> consumers = new LinkedList<>();

        Monitor m = new Monitor(BUFF_SIZE, CONSUMER_COUNT_SMALL + CONSUMER_COUNT_BIG,PRODUCER_COUNT_BIG + PRODUCER_COUNT_SMALL);
        ThreadLogger logger = new ThreadLogger();
        new Thread(logger, "Logger-Thread-").start();

        for (int i = 0; i < PRODUCER_COUNT_BIG; i++) {
            producers.add(new Thread(new Producer(m, logger, BUFF_SIZE / 2 - 1), "P-B-" + i));
        }

        for (int i = 0; i < PRODUCER_COUNT_SMALL; i++) {
            producers.add(new Thread(new Producer(m, logger, BUFF_SIZE / 2 - 4 ), "P-S-" + i));
        }

        for (int i = 0; i < CONSUMER_COUNT_BIG; i++) {
            consumers.add(new Thread(new Consumer(m, logger, BUFF_SIZE / 2 - 1), "C-B-" + i));
        }
        for (int i = 0; i < CONSUMER_COUNT_SMALL; i++) {
            consumers.add(new Thread(new Consumer(m, logger, BUFF_SIZE / 2 - 4), "C-S-" + i));
        }

        consumers.forEach(Thread::start);
        producers.forEach(Thread::start);
    }
}