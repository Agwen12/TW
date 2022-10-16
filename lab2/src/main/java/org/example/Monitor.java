package org.example;

public class Monitor {

    private int bread = 1;
    private final int BUFF_SIZE;

    public Monitor(int buff_size) {
        this.BUFF_SIZE = buff_size;
    }

    public synchronized void produce() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Before: " + bread);
        while (bread >= BUFF_SIZE) wait();

        bread++;
        System.out.println(Thread.currentThread().getName() + " After: " + bread);
        notifyAll();
    }

    public synchronized void consume() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Before: " + bread);
        while (bread == 0) wait();

        bread--;
        System.out.println(Thread.currentThread().getName() + " After: " + bread);
        notifyAll();
    }
}
