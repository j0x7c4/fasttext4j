package com.ymatou.atc.fastText4j;

import java.io.*;

/**
 * Created by fujie on 2017/2/16.
 */
public class Args {
    private double r;
    private int dim;
    private int ws;
    private int epoch;
    private int minCount;
    private int minCountLabel;
    private int neg;
    private int wordNgrams;
    private int loss;
    private int model;
    private int bucket;
    private int minn;
    private int maxn;
    private int thread;
    private int lrUpdateRate;
    private double t;
    private String label ;
    private int verbose;
    private String pretrainedVectors;

    public Args () {
        r = 0.05;
        dim = 100;
        ws = 5;
        epoch = 5;
        minCount = 5;
        minCountLabel = 0;
        neg = 5;
        wordNgrams = 1;
        loss = LossName.ns.ordinal();
        model = ModelName.sg.ordinal();
        bucket = 2000000;
        minn = 3;
        maxn = 6;
        thread = 12;
        lrUpdateRate = 100;
        t = 1e-4;
        label = "__label__";
        verbose = 2;
        pretrainedVectors = "";
    }

    public void load(BufferedReader in) throws IOException {
        String[] line = in.readLine().split(" ");
        dim = Integer.parseInt(line[0]);
        ws = Integer.parseInt(line[1]);
        epoch = Integer.parseInt(line[2]);
        minCount = Integer.parseInt(line[3]);
        neg = Integer.parseInt(line[4]);
        wordNgrams = Integer.parseInt(line[5]);
        loss = Integer.parseInt(line[6]) - 1;
        model = Integer.parseInt(line[7]) - 1;
        bucket = Integer.parseInt(line[8]);
        minn = Integer.parseInt(line[9]);
        maxn = Integer.parseInt(line[10]);
        lrUpdateRate = Integer.parseInt(line[11]);
        t = Double.parseDouble(line[12]);
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }

    public int getWs() {
        return ws;
    }

    public void setWs(int ws) {
        this.ws = ws;
    }

    public int getEpoch() {
        return epoch;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMinCountLabel() {
        return minCountLabel;
    }

    public void setMinCountLabel(int minCountLabel) {
        this.minCountLabel = minCountLabel;
    }

    public int getNeg() {
        return neg;
    }

    public void setNeg(int neg) {
        this.neg = neg;
    }

    public int getWordNgrams() {
        return wordNgrams;
    }

    public void setWordNgrams(int wordNgrams) {
        this.wordNgrams = wordNgrams;
    }

    public LossName getLoss() {
        return LossName.values()[loss];
    }

    public void setLoss(LossName loss) {
        this.loss = loss.ordinal();
    }

    public ModelName getModel() {
        return ModelName.values()[model];
    }

    public void setModel(ModelName model) {
        this.model = model.ordinal();
    }

    public int getBucket() {
        return bucket;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }

    public int getMinn() {
        return minn;
    }

    public void setMinn(int minn) {
        this.minn = minn;
    }

    public int getMaxn() {
        return maxn;
    }

    public void setMaxn(int maxn) {
        this.maxn = maxn;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getLrUpdateRate() {
        return lrUpdateRate;
    }

    public void setLrUpdateRate(int lrUpdateRate) {
        this.lrUpdateRate = lrUpdateRate;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getVerbose() {
        return verbose;
    }

    public void setVerbose(int verbose) {
        this.verbose = verbose;
    }

    public String getPretrainedVectors() {
        return pretrainedVectors;
    }

    public void setPretrainedVectors(String pretrainedVectors) {
        this.pretrainedVectors = pretrainedVectors;
    }
}
