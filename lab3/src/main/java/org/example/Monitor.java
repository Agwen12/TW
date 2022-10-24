package org.example;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private int bread = 1;
    final static Random rand = new Random();
    private final int BUFF_SIZE;

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    public Monitor(int buff_size) {
        this.BUFF_SIZE = buff_size;
    }

    public void produce() throws InterruptedException {
        lock.lock();
        try {
            int g = rand.nextInt(BUFF_SIZE / 2 - 1) + 1;
            while (bread + g >= BUFF_SIZE) notFull.await();
            System.out.println(Thread.currentThread().getName() + " Before: " + bread);

            bread += g;
            notEmpty.signal();
            System.out.println(Thread.currentThread().getName() + " After:  " + bread);
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            int g = rand.nextInt(BUFF_SIZE / 2 - 1) + 1;
            while (bread - g <= 0) notEmpty.await();
            System.out.println(Thread.currentThread().getName() + " Before: " + bread);


            bread -= g;
            notFull.signal();
            System.out.println(Thread.currentThread().getName() + " After:  " + bread);
        } finally {
            lock.unlock();
        }

    }
}

