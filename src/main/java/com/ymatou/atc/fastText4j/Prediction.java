package com.ymatou.atc.fastText4j;

/**
 * Created by fujie on 2017/2/17.
 */
public class Prediction implements Comparable {
    private float first;
    private int second;

    public Prediction() {

    }

    public Prediction(float a, int b) {
        first = a;
        second = b;
    }

    public float getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public void setFirst(float first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int compareTo(Object o) {
        Prediction p = (Prediction)o;
        return this.getFirst() < p.getFirst()?1:-1;
    }
}
