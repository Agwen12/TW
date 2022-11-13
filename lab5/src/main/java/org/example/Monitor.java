package org.example;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private int bread = 0;
    final static Random rand = new Random();
    private final int BUFF_SIZE;
    private int cons;
    private int prods;

    final Lock lock = new ReentrantLock();
    final Condition restProd = lock.newCondition();
    final Condition Prod1 = lock.newCondition();
    final Condition Cons1 = lock.newCondition();

    final Condition restCons = lock.newCondition();

    private Boolean firstProdLocked = false;
    private Boolean firstConsLocked = false;
    public Monitor(int buff_size, int cons, int prods) {
        this.BUFF_SIZE = buff_size;
        this.cons = cons;
        this.prods = prods;
    }

    public void produce(int portion) throws InterruptedException {
        lock.lock();
        String threadName = Thread.currentThread().getName() + " ";
        try {
            while (firstProdLocked) {
                restProd.await();
            }

            while (bread + portion >= BUFF_SIZE) {
                firstProdLocked = true;
                Prod1.await();
            }
            bread += portion;
//            System.out.println(threadName + "     "+ (float) bread / (float) BUFF_SIZE);
            restProd.signal();
            firstProdLocked = false;
            Cons1.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume(int portion) throws InterruptedException {
        lock.lock();
        String threadName = Thread.currentThread().getName() + " ";
        try {
            while (firstConsLocked) {
                restCons.await();
            }

            while (bread - portion < 0)  {
                firstConsLocked = true;
                Cons1.await();
            }

            bread -= portion;
            restCons.signal();
            firstConsLocked = false;
            Prod1.signal();
        } finally {
            lock.unlock();
        }

    }
}

