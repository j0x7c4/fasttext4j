package com.ymatou.atc.fastText4j;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by fujie on 2017/2/16.
 */
public class FastText {

    private Args args;
    private Dictionary dict;
    private Matrix input;
    private Matrix output;
    private Model model;
    private long tokenCount;
    private long start;

    public void loadModel(InputStream in) throws IOException {
        args = new Args();
        dict = new Dictionary(args);
        input = new Matrix();
        output = new Matrix();
        args.load(in);
        dict.load(in);
        input.load(in);
        output.load(in);
        model = new Model(input, output, args, 0);
        if (args.getModel() == ModelName.sup) {
            model.setTargetCounts(dict.getCounts(EntryType.label));
        } else {
            model.setTargetCounts(dict.getCounts(EntryType.word));
        }
    }

    public void loadModel(String s) {
        try {
            FileInputStream fin = new FileInputStream(new File(s));
            loadModel(fin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void predict(String str, int k, ArrayList<Float> probs, ArrayList<String> labels) throws IOException {
        predict(new ByteArrayInputStream(str.getBytes("UTF-8")), k ,probs, labels);
    }

    public void predict(InputStream in, int k, ArrayList<Float> probs, ArrayList<String> labels) throws IOException {
        ArrayList<Prediction> modelPredictions;
        ArrayList<Integer> words = dict.getLine(in, model.rng);
        dict.addNgrams(words, args.getWordNgrams());
        if (words.isEmpty()) return;
        Vector hidden = new Vector(args.getDim());
        Vector output = new Vector(dict.getNlabels());
        modelPredictions = model.predict(words, k, hidden, output);
        probs.clear();
        labels.clear();
        for (Prediction pre: modelPredictions) {
            probs.add(pre.getFirst());
            labels.add(dict.getLabel(pre.getSecond()));
        }
        return;
    }

    public void predict(InputStream in, int k, boolean print_prob) {

    }
}
