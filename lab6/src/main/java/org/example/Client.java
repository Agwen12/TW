package org.example;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.Random;

public class Client implements Runnable {


    private final Logger logger = Logger.getLogger(Client.class);
    private final ActiveObject activeObject;
    private final Integer range = Integer.parseInt(System.getProperty("buffer.size")) / 2 - 1;
    private final Random rand = new Random(Integer.parseInt(System.getProperty("seed")));
    private final Modes mode;

    public Client(Modes mode, ActiveObject activeObject) {
        PropertyConfigurator.configure(System.getProperty("logger.configuration"));
        this.activeObject = activeObject;
        this.mode = mode;

    }

    @Override
    public void run() {
        while (true) {
            Integer portion = getRandomValue();
            Task task = new Task(portion);
            FutureResult future = activeObject.enqueue(task);
            Stats stats = ExpensiveTask.work(future);
            logger.info(stats);
        }
    }

    private Integer getRandomValue() {
        if (mode == Modes.CONSUMER) {
            return -(rand.nextInt(range) + 1);
        }
        return rand.nextInt(range) + 1;
    }

    @Override
    public String toString() {
        return mode.toString();
    }
}
