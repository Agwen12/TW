package org.example;

import java.util.Random;

public class Producer implements Runnable {

    Monitor monitor;
    ThreadLogger logger;
    int portion;

    final Random rnd = new Random();
    public Producer(Monitor m, ThreadLogger logger, int portion){
        this.monitor = m;
        this.logger = logger;
        this.portion = portion;
    }

    public void produce() throws InterruptedException {
        while (true) {
            int p = rnd.nextInt(portion + 1);
            monitor.produce(p);
            logger.incrementProducers(Thread.currentThread().getName());
        }
    }

    @Override
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
