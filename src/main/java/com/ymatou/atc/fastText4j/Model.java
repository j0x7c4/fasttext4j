package com.ymatou.atc.fastText4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by fujie on 2017/2/16.
 */
public class Model {

    private static final int SIGMOID_TABLE_SIZE = 512;
    private static final int MAX_SIGMOID = 8;
    private static final int LOG_TABLE_SIZE = 512;
    private static final int NEGATIVE_TABLE_SIZE = 10000000;
    private Matrix wi;
    private Matrix wo;
    private Args args;
    private Vector hidden;
    private Vector output;
    private Vector grad;
    private int hsz;
    private int isz;
    private int osz;
    private float loss;
    private long nexamples;
    private float[] t_sigmoid;
    private float[] t_log;

    private ArrayList<ArrayList<Integer>> paths;
    private ArrayList<ArrayList<Boolean>> codes;
    private ArrayList<ModelNode> tree;

    private ArrayList<Integer> negatives;
    private int negpos;

    //public MinStd_Rand minstd_rand;
    public Random rng;

    public Model(Matrix input, Matrix output, Args args, int seed) {
        this.hidden = new Vector(args.getDim());
        this.output = new Vector(output.m);
        this.grad = new Vector(args.getDim());
        this.rng = new Random(seed);

        this.wi = input;
        this.wo = output;
        this.args = args;
        this.isz = wi.m;
        this.osz = wo.m;
        this.hsz = args.getDim();
        this.negpos = 0;
        this.loss = 0.0f;
        this.nexamples = 1;
        initSigmoid();
        initLog();
    }

    private void initLog() {
        this.t_log = new float[LOG_TABLE_SIZE + 1];
        for (int i =0 ; i<LOG_TABLE_SIZE ; i++) {
            float x = (float)((float)i + 1e-5) / LOG_TABLE_SIZE;
            t_log[i] = (float)Math.log(x);
        }
    }

    private void initSigmoid() {
        this.t_sigmoid = new float[SIGMOID_TABLE_SIZE+1];
        for (int i =0 ; i<SIGMOID_TABLE_SIZE + 1; i++) {
            float x = i*2.0f*MAX_SIGMOID / SIGMOID_TABLE_SIZE - MAX_SIGMOID;
            t_sigmoid[i] = (float)(1.0 / ( 1.0 + Math.exp(-x)));
        }
    }

    public float log(float x) {
        if (x>1.0) {
            return 0.0f;
        }
        int i = (int)(x*LOG_TABLE_SIZE);
        return t_log[i];
    }

    public float sigmoid(float x) {
        if (x<-MAX_SIGMOID) {
            return 0.0f;
        } else if (x > MAX_SIGMOID) {
            return 1.0f;
        } else {
            int i = (int)((x + MAX_SIGMOID) * SIGMOID_TABLE_SIZE / MAX_SIGMOID / 2);
            return t_sigmoid[i];
        }
    }

    public void setTargetCounts(ArrayList<Long> counts) {
        assert (counts.size() == this.osz);
        if (args.getLoss() == LossName.ns) {
            initTableNegatives(counts);
        }
        if (args.getLoss() == LossName.hs) {
            buildTree(counts);
        }
    }

    private void buildTree(ArrayList<Long> counts) {
        tree = new ArrayList<ModelNode>(2 * osz - 1);
        paths = new ArrayList<ArrayList<Integer>>();
        codes = new ArrayList<ArrayList<Boolean>>();
        for (int i=0 ; i<2 *osz - 1; i++) {
            tree.set(i, new ModelNode());
        }
        for (int i=0 ; i<osz ; i++) {
            tree.get(i).count = counts.get(i);
        }
        int leaf = osz - 1;
        int node = osz;
        for ( int i = osz; i<2*osz-1 ; i++) {
            int[] mini = new int[2];
            for (int j =0 ; j<2 ; j++) {
                if (leaf>=0 && tree.get(leaf).count < tree.get(node).count) {
                    mini[j] = leaf --;
                } else {
                    mini[j] = node ++;
                }
            }
            tree.get(i).left = mini[0];
            tree.get(i).right = mini[i];
            tree.get(i).count = tree.get(mini[0]).count + tree.get(mini[1]).count;
            tree.get(mini[0]).parent = i;
            tree.get(mini[1]).parent = i;
            tree.get(mini[1]).binary = true;
        }
        for (int i=0 ; i<osz ; i++) {
            ArrayList<Integer> path = new ArrayList<Integer>();
            ArrayList<Boolean> code = new ArrayList<Boolean>();
            int j = i;
            while (tree.get(j).parent != -1) {
                path.add(tree.get(j).parent - osz);
                code.add(tree.get(j).binary);
                j = tree.get(j).parent;
            }
            paths.add(path);
            codes.add(code);
        }
    }

    public float getLoss() {
        return loss / nexamples;
    }

    private void initTableNegatives(ArrayList<Long> counts) {
        float z = 0.0f;
        negatives = new ArrayList<Integer>();
        for (int i =0 ; i<counts.size(); i++) {
            z += Math.pow(counts.get(i), 0.5);
        }
        for (int i = 0; i<counts.size(); i++) {
            float c = (float)Math.pow(counts.get(i), 0.5);
            for (int j=0 ; j<c * NEGATIVE_TABLE_SIZE / z ; j++) {
                negatives.add(i);
            }
        }
        Collections.shuffle(counts, rng);
    }

    public ArrayList<Prediction> predict(ArrayList<Integer> input, int k, Vector hidden, Vector output) {
        assert (k>0);
        ArrayList<Prediction> heap = new ArrayList<Prediction>();
        computeHidden(input, hidden);
        if (args.getLoss() == LossName.hs) {
            dfs(k , 2*osz-2 , 0.0f,  heap, hidden);
        } else {
            findKBest(k, heap, hidden, output);
        }
        Heap.heapSort(heap);
        return heap;
    }

    public ArrayList<Prediction> predict(ArrayList<Integer> input, int k) {
        return predict(input, k, this.hidden, this.output);
    }


    void computeHidden(ArrayList<Integer> input, Vector hidden) {
        assert(hidden.size() == hsz);
        hidden.zero();
        for (int i : input) {
            hidden.addRow(wi, i);
        }
        hidden.mul(1.0f / input.size());
    }

    void dfs(int k, int node, float score, ArrayList<Prediction> heap, Vector hidden) {
        if (heap.size() == k && score < heap.get(0).getFirst()) {
            return;
        }

        if (tree.get(node).left == -1 && tree.get(node).right == -1) {
            heap.add(new Prediction(score, node));
            Heap.adjustHeap(heap, 0, heap.size());
            if (heap.size() > k) {
                Heap.popHeap(heap);
            }
            return;
        }

        float f = sigmoid(wo.dotRow(hidden, node - osz));
        dfs(k, tree.get(node).left, score + log(1.0f - f), heap, hidden);
        dfs(k, tree.get(node).right, score + log(f), heap, hidden);
    }

    void findKBest(int k, ArrayList<Prediction> heap,Vector hidden, Vector output) {
        computeOutputSoftmax(hidden, output);
        for (int i = 0; i < osz; i++) {
            if (heap.size() == k && log(output.get(i)) < heap.get(0).getFirst()) {
                continue;
            }
            heap.add(new Prediction(log(output.get(i)), i));
            Heap.adjustHeap(heap, 0, heap.size());
            if (heap.size() > k) {
                Heap.popHeap(heap);
            }
        }
    }

    private void computeOutputSoftmax(Vector hidden, Vector output) {
        output.mul(wo, hidden);
        float max = output.get(0), z = 0.0f;
        for (int i = 0; i < osz; i++) {
            max = Math.max(output.get(i), max);
        }
        for (int i = 0; i < osz; i++) {
            output.set(i, (float)Math.exp(output.get(i) - max));
            z += output.get(i);
        }
        for (int i = 0; i < osz; i++) {
            output.set(i, output.get(i)/z);
        }
    }
}
