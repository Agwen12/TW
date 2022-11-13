package org.example;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActiveObject implements Runnable {


    private static final Integer BUFF_SIZE = Integer.parseInt(System.getProperty("buffer.size"));
    private Integer buffer = 0;
    private final BlockingQueue<MethodRequest> blockingQueue = new LinkedBlockingQueue<>();
    private final Queue<MethodRequest> innerQueue = new LinkedList<>();
    private final Logger logger = Logger.getLogger(ActiveObject.class);


    public ActiveObject() {
        PropertyConfigurator.configure(System.getProperty("logger.configuration"));
        logger.debug("Initialized AO with buffer_size = " + buffer);
    }

    public FutureResult enqueue(Task task) {
        try {
            FutureResult future = new FutureResult();
            logger.debug("Enqueueing :" + task);
            MethodRequest methodRequest = new MethodRequest(
                    () -> consumeOrProduce(task),
                    () -> checkIfPossible(task),
                    future,
                    task
            );
            blockingQueue.put(methodRequest);
            return future;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                doTask();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Optional<MethodRequest> getTask() throws InterruptedException {
        logger.debug("Trying to get task to run");
        if (!innerQueue.isEmpty()) {
            MethodRequest mRQ = innerQueue.peek();
            logger.debug("Inner queue head: " + mRQ);
            if(mRQ.guard()) {
                logger.debug("Getting task from inner queue");
                return Optional.of(innerQueue.poll());
            }
        }

        // Cannot run task from inner queue so waits for client to submit new RQ
        logger.trace("Taking RQ from front queue");
        long start = System.currentTimeMillis();
        MethodRequest mRQ = blockingQueue.take();
        logger.trace("Obtained RQ from front queue " + mRQ + " after " + (System.currentTimeMillis() - start) + " [ms]");

        if (mRQ.guard()) {
            logger.debug("Getting task from front queue");
            return Optional.of(mRQ);
        }

        logger.debug("Putting task in inner queue");
        innerQueue.add(mRQ);
        return Optional.empty();

    }

    private void doTask() throws InterruptedException {
        Optional<MethodRequest> methodRQ;
        methodRQ = getTask();
        methodRQ.ifPresent(methodRequest -> methodRequest.getFuture().setResult(methodRequest.call()));
//        logger.info("Buffer: " + buffer);

    }

    private Integer consumeOrProduce(Task task) {
        this.buffer += task.portion();
        return buffer;
    }

    private boolean checkIfPossible(Task task) {
        return buffer + task.portion() >= 0 && buffer + task.portion() < BUFF_SIZE;
    }
}
