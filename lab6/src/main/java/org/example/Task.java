package org.example;

public record Task(Integer portion) {

    @Override
    public String toString() {
        if(portion > 0) {
            return "PRODUCE [" + portion + "]";
        }
        else {
            return "CONSUME [" + -portion + "]";
        }
    }
}
