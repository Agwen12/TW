package org.example;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class MethodRequest implements Callable<Integer> {

    private final Callable<Integer> task;
    private final Supplier<Boolean> guard;

    private final FutureResult future;
    private final Task t;

    public MethodRequest(Callable<Integer> task, Supplier<Boolean> guard, FutureResult future, Task t) {
        this.task = task;
        this.guard = guard;
        this.future = future;
        this.t = t;
    }

    @Override
    public Integer call() {
        try {
            return task.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean guard() {
        return guard.get();
    }

    public Task getTask() {
        return t;
    }
    public FutureResult getFuture() {
        return future;
    }

    @Override
    public String toString() {
        return "MQ[ " + t + " ]";
    }
}
