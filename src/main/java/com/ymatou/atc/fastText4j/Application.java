package com.ymatou.atc.fastText4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Created by fujie on 2017/2/16.
 */
public class Application {
    static public void printUsage() {
        System.out.println(
                "usage: fasttext <command> <args>\n\n"
                        + "The commands supported by fasttext are:\n\n"
                        + "  supervised          train a supervised classifier\n"
                        + "  test                evaluate a supervised classifier\n"
                        + "  predict             predict most likely labels\n"
                        + "  predict-prob        predict most likely labels with probabilities\n"
                        + "  skipgram            train a skipgram model\n"
                        + "  cbow                train a cbow model\n"
                        + "  print-vectors       print vectors given a trained model\n"
        );
    }

    static public void printPredictUsage(){
        System.out.println(
                "usage: fasttext predict[-prob] <model> <test-data> [<k>]\n\n"
                        + "  <model>      model filename\n"
                        + "  <test-data>  test data filename (if -, read from stdin)\n"
                        + "  <k>          (optional; 1 by default) predict top k labels\n"
        );
    }

    static private void predict(String[] argv) {
        int k = 1;
        if (argv.length == 3) {
            k = 1;
        } else if (argv.length == 4) {
            k = Integer.parseInt(argv[3]);
        } else {
            printPredictUsage();
            System.exit(1);
        }
        boolean print_prob = argv[0].equalsIgnoreCase("predict-prob");
        FastText fasttext = new FastText();
        fasttext.loadModel(argv[1]);

        String infile = argv[2];
        if (infile.equalsIgnoreCase("-"))
            fasttext.predict(System.in, k, print_prob);
        else {
            try {
                fasttext.predict(new FileInputStream(infile), k, print_prob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    static public void train(String[] argv) {

    }

    static public void test(String[] argv) {

    }

    static public void printVectors(String[] argv) {

    }

    public static void main (String[] argv) {
        if (argv.length<1) {
            printPredictUsage();
            System.exit(1);
        }
        String command = argv[0];
        if (command.equalsIgnoreCase("skipgram") || command.equalsIgnoreCase("cbow") || command.equalsIgnoreCase("supervised")) {
            train(argv);
        } else if (command.equalsIgnoreCase("test")) {
            test(argv);
        } else if (command.equalsIgnoreCase("print-vectors")) {
            printVectors(argv);
        } else if (command.equalsIgnoreCase("predict") || command.equalsIgnoreCase("predict-prob" )) {
            predict(argv);
        } else {
            printUsage();
            System.exit(1);
        }
        System.exit(0);
    }
}
