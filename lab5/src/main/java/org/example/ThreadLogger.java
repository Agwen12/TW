package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class ThreadLogger implements Runnable{



    Map<String, Integer> activitiesProducers;
    Map<String, Integer> activitiesConsumers;

    public ThreadLogger() {
        activitiesConsumers = new HashMap<>();
        activitiesProducers = new HashMap<>();
    }

    public void incrementProducers(String i) {
        if (activitiesProducers.containsKey(i)) {
            activitiesProducers.replace(i, activitiesProducers.get(i) + 1);
        } else {
            activitiesProducers.put(i, 1);
        }
    }
    public void incrementConsumers(String i) {
        if (activitiesConsumers.containsKey(i)) {
            activitiesConsumers.replace(i, activitiesConsumers.get(i) + 1);
        } else {
            activitiesConsumers.put(i, 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Consumers\n");
        builder.append(activitiesConsumers);

        builder.append("\nProducers\n");
        builder.append(activitiesProducers);
        return builder.toString();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(this);
        }
    }
}
