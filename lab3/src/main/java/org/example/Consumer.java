package org.example;

public class Consumer implements Runnable {

    Monitor monitor;

    public Consumer(Monitor m){
        this.monitor = m;
    }

    public void consume() throws InterruptedException {
        while (true) {
            monitor.consume();
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
