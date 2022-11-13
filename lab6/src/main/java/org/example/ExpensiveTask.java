package org.example;

public class ExpensiveTask {

    private static final int ITERATIONS = 100;


    public static Stats work(FutureResult future) {
        int iterations = 0;
        int sum = 0;
        long start = System.currentTimeMillis();
        long now = System.currentTimeMillis();
        while (!future.isReady() || now - start < AOMain.   timeOut) {
            sum += Math.cos(1.324234);
            iterations += 1;
            now = System.currentTimeMillis();
        }
        long end = System.currentTimeMillis();

        return new Stats(end - start, iterations, future.getResult());
    }
}
