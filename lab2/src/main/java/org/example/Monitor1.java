package org.example;

public class Monitor1 {

    private boolean held = false;
    private final int[] waiters;
    private final int[] awakenings;

    public Monitor1(int numOfWaiters) {
        this.waiters = new int[numOfWaiters];
        this.awakenings = new int[numOfWaiters];

    }

    synchronized void acquire() throws InterruptedException {
        while (held)
            wait();
        held = true;   /* mine! */

    }

    synchronized void release() {
        held = false;
        notify();
    }

    synchronized void waitOnChannel(int cid) throws InterruptedException {
        release();
        waiters[cid]++;
        int myWaiterNumber = waiters[cid];
        while (awakenings[cid] < myWaiterNumber)
            wait();
        acquire();
    }

    synchronized void signalOnChannel(int cid) {
        if (awakenings[cid] == waiters[cid])    /* no waiters waiting on this channel */
            return;
        awakenings[cid]++;            /* awaken one waiter on this channel */
        notifyAll();
    }

    synchronized void broadcastOnChannel(int cid) {
        awakenings[cid] = waiters[cid];        /* awaken all waiters on this channel */
        notifyAll();
    }
}
