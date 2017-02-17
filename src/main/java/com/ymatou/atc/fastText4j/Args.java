package com.ymatou.atc.fastText4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public void load(InputStream in) throws IOException {

        DataInputStream dataInputStream = new DataInputStream(in);
        dim = dataInputStream.readInt();
//        byte[] bytes = new byte[4];
//        in.read(bytes, 0, 4);
//        int dim =  bytes[0] & 0xFF |
//                (bytes[1] & 0xFF) << 8 |
//                (bytes[2] & 0xFF) << 16 |
//                (bytes[3] & 0xFF) << 24;
        //        dim = dataInputStream.readInt();
//        ws = dataInputStream.readInt();
//        epoch = dataInputStream.readInt();
//        minCount = dataInputStream.readInt();
//        neg = dataInputStream.readInt();
//        wordNgrams = dataInputStream.readInt();
//        loss = dataInputStream.readInt();
//        model = dataInputStream.readInt();
//        bucket = dataInputStream.readInt();
//        minn = dataInputStream.readInt();
//        maxn = dataInputStream.readInt();
//        lrUpdateRate = dataInputStream.readInt();
//        t = dataInputStream.readDouble();
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
