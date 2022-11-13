package org.example;

public class FutureResult {

    private volatile Integer result = null;

    public boolean isReady() {
        return result != null;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
