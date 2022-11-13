package org.example;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private int bread = 1;
    final static Random rand = new Random();
    private final int BUFF_SIZE;
    private final int PORTION_SIZE;

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    public Monitor(int buff_size) {
        this.BUFF_SIZE = buff_size;
        this.PORTION_SIZE = BUFF_SIZE / 2  + 1;
    }

    public void produce() throws InterruptedException {
        lock.lock();
        try {
            int portion = rand.nextInt(PORTION_SIZE) + 1;
            while (bread + portion >= BUFF_SIZE) notFull.await();
            System.out.println(Thread.currentThread().getName() + " Before: " + bread);

            bread += portion;
            notEmpty.signal();
            System.out.println(Thread.currentThread().getName() + " After:  " + bread);
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            int portion = rand.nextInt(PORTION_SIZE) + 1;
            while (bread - portion < 0) notEmpty.await();
            System.out.println(Thread.currentThread().getName() + " Before: " + bread);


            bread -= portion;
            notFull.signal();
            System.out.println(Thread.currentThread().getName() + " After:  " + bread);
        } finally {
            lock.unlock();
        }

    }
}

