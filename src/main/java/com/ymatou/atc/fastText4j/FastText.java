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

    public void loadModel(BufferedReader in) throws IOException {
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
            BufferedReader br = new BufferedReader(new FileReader(new File(s)));
            loadModel(br);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void predict(String str, int k, ArrayList<Float> probs, ArrayList<String> labels) throws IOException {
        ArrayList<Prediction> modelPredictions;
        ArrayList<Integer> words;
        probs.clear();
        labels.clear();
        words = dict.getLine(str, model.rng);
        dict.addNgrams(words, args.getWordNgrams());
        if (words.isEmpty())
            return;
        Vector hidden = new Vector(args.getDim());
        Vector output = new Vector(dict.getNlabels());
        modelPredictions = model.predict(words, k, hidden, output);
        for (Prediction pre : modelPredictions) {
            probs.add((float)Math.exp(pre.getFirst()));
            labels.add(dict.getLabel(pre.getSecond()));
        }
    }

    public void predict(BufferedReader in, int k, boolean print_prob) throws IOException {
        String str;
        while ( (str=in.readLine())!=null ) {
            ArrayList<Float> probs = new ArrayList<Float>();
            ArrayList<String> labels = new ArrayList<String>();
            predict(str, k, probs, labels);
            assert probs.size() == labels.size();
            for (int i = 0; i < labels.size(); i++) {
                if (i>0) {
                    System.out.print(" ");
                }
                System.out.print(labels.get(i));
                if (print_prob)
                    System.out.print(" " + String.valueOf(probs.get(i)));
            }
            System.out.println();
        }
    }

    public void test(BufferedReader bufferedReader, int k) throws IOException {
        int nexamples = 0;
        int nlabels = 0;
        double precision = 0.0;
        ArrayList<Integer> line = new ArrayList<Integer>();
        ArrayList<Integer> labels = new ArrayList<Integer>();
        String str;
        while ((str = bufferedReader.readLine())!=null) {
            dict.getLine(str, model.rng, line, labels);
            dict.addNgrams(line, args.getWordNgrams());
            if (line.size()>0 && labels.size()>0) {
                ArrayList<Prediction> modelPredictions = model.predict(line, k);
                for (Prediction p :modelPredictions) {
                    if (labels.contains(p.getSecond())) {
                        precision += 1.0;
                    }
                }
                nexamples++;
                nlabels += labels.size();
            }
        }
        System.out.println("P@"+String.valueOf(k)+": "+String.valueOf(precision / (k*nexamples)));
        System.out.println("R@"+String.valueOf(k)+": "+String.valueOf(precision / nlabels));
        System.out.println("Number of examples: "+String.valueOf(nexamples));
    }

    public void printVectors() throws IOException {
        if (args.getModel() == ModelName.sup) {
            textVectors();
        } else {
            wordVectors();
        }
    }

    private void wordVectors() throws IOException {
        String word;
        Vector vec = new Vector(args.getDim());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = br.readLine())!=null) {
            word = str.toLowerCase().replaceAll("[ \t\n\r]", "");
            getVector(vec, word);
            System.out.println(vec.toString());
        }
    }

    public Vector getVector(String word) {
        Vector vec = new Vector(args.getDim());
        getVector(vec, word);
        return vec;
    }

    public void getVector(Vector vec, String word) {
        vec.zero();
        ArrayList<Integer> ngrams = dict.getNgrams(word);
        for (int i :ngrams) {
            vec.addRow(this.input, i);
        }
        if (ngrams.size()>0) {
            vec.mul(1.0f/ ngrams.size());
        }
    }

    private void textVectors() throws IOException {
        ArrayList<Integer> line = new ArrayList<Integer>();
        ArrayList<Integer> labels = new ArrayList<Integer>();
        Vector vec = new Vector(args.getDim());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ( (str=br.readLine())!= null ){
            dict.getLine(str, model.rng, line, labels);
            dict.addNgrams(line, args.getWordNgrams());
            vec.zero();
            for (int i:line) {
                vec.addRow(this.input, i);
            }
            if (line.size()>0) {
                vec.mul(1.0f / line.size());
            }
            System.out.println(vec.toString());
        }
    }
}
