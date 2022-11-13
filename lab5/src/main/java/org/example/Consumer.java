package org.example;

import java.util.Random;

public class Consumer implements Runnable {

    Monitor monitor;
    ThreadLogger logger;
    int portion;
    final Random rnd = new Random();

    public Consumer(Monitor m, ThreadLogger logger, int portion){
        this.monitor = m;
        this.logger = logger;
        this.portion = portion;
    }

    public void consume() throws InterruptedException {
        while (true) {
            int p = rnd.nextInt(portion);
            monitor.consume(p + 1);
            logger.incrementConsumers(Thread.currentThread().getName());
        }
    }

    @Override
    public void run() {
        try {
            consume();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
