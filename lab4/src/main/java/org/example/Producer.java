package org.example;

public class Producer implements Runnable {

    Monitor monitor;

    public Producer(Monitor m){
        this.monitor = m;
    }

    public void produce() throws InterruptedException {
        while (true) {
            monitor.produce();
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
