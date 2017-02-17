package com.ymatou.atc.fastText4j;

/**
 * Created by fujie on 2017/2/17.
 */
public class ModelNode {
    public int parent;
    public int left;
    public int right;
    public long count;
    boolean binary;

    public ModelNode() {
        parent = -1;
        left = -1;
        right = -1;
        count = (long)1e15;
        binary = false;
    }
}
