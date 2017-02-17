package com.ymatou.atc.fastText4j;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;

/**
 * Created by fujie on 2017/2/16.
 */
public class Dictionary {
    private static final int MAX_VOCAB_SIZE = 30000000;
    private static final int MAX_LINE_SIZE = 1024;

    private Args args;
    private int[] word2int;
    private ArrayList<DictionaryEntry> words;
    private ArrayList<Float> pdiscard;
    private int size;
    private int nwords;
    private int nlabels;
    private long ntokens;

    public static final String EOS = "</s>";
    public static final String BOW = "<";
    public static final String EOW = ">";

    public Dictionary(Args args) {
        this.args = args;
        this.size = 0;
        this.nwords = 0;
        this.nlabels = 0;
        this.ntokens = 0;
        word2int = new int[MAX_VOCAB_SIZE];
        words = new ArrayList<DictionaryEntry>();
        for (int i = 0 ; i<MAX_VOCAB_SIZE; i++)
            word2int[i] = -1;
    }

    public long hash(final String str) {
        long h = 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            h = h ^ (int)str.charAt(i);
            h = h * 16777619;
        }
        return h;
    }

    public int find(final String w) {
        int h = (int) (hash(w) % MAX_VOCAB_SIZE);
        while (word2int[h] != -1 && words.get(word2int[h]).word != w) {
            h = (h + 1) % MAX_VOCAB_SIZE;
        }
        return h;
    }

    public void load(InputStream in) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(in);
        words.clear();
        for (int i = 0; i < MAX_VOCAB_SIZE; i++) {
            word2int[i] = -1;
        }
        size = dataInputStream.readInt();
        nwords = dataInputStream.readInt();
        nlabels = dataInputStream.readInt();
        ntokens = dataInputStream.readLong();

        for (int i = 0; i < size; i++) {
            char c;
            DictionaryEntry e = new DictionaryEntry();
            while ((c = (char)dataInputStream.readByte()) != 0) {
                e.word+=c;
            }
            e.count = dataInputStream.readLong();
            e.type = dataInputStream.readByte() & 0xFF;
            words.add(e);
            word2int[find(e.word)] = i;
        }
        initTableDiscard();
        initNgrams();
    }

    private void initTableDiscard() {
        pdiscard = new ArrayList<Float>(size);
        for (int i = 0; i < size; i++) {
            float f = (float) words.get(i).count / ntokens;
            pdiscard.set(i, (float)sqrt(args.getT() / f) + (float)args.getT() / f);
        }
    }

    public void computeNgrams(final String word, List<Integer> ngrams) {
        for (int i = 0; i < word.length(); i++) {
            String ngram = "";
            if ((word.charAt(i) & 0xC0) == 0x80) continue;
            for (int j = i, n = 1; j < word.length() && n <= args.getMaxn(); n++) {
                ngram += word.charAt(j++);
                while (j < word.length() && (word.charAt(j) & 0xC0) == 0x80) {
                    ngram += word.charAt(j++);
                }
                if (n >= args.getMinn() && !(n == 1 && (i == 0 || j == word.length()))) {
                    int h = (int)(hash(ngram) % args.getBucket());
                    ngrams.add(nwords + h);
                }
            }
        }
    }

    private void initNgrams() {
        for (int i = 0; i < size; i++) {
            String word = BOW + words.get(i).word + EOW;
            words.get(i).subwords.add(i);
            computeNgrams(word, words.get(i).subwords);
        }
    }

    public ArrayList<Long> getCounts(EntryType type) {
        ArrayList<Long> counts = new ArrayList<Long>();
        for (DictionaryEntry w :words) {
            if (w.type == type.ordinal()) counts.add(w.count);
        }
        return counts;
    }

    public static int getMaxVocabSize() {
        return MAX_VOCAB_SIZE;
    }

    public static int getMaxLineSize() {
        return MAX_LINE_SIZE;
    }

    public Args getArgs() {
        return args;
    }

    public void setArgs(Args args) {
        this.args = args;
    }

    public int[] getWord2int() {
        return word2int;
    }


    public List<DictionaryEntry> getWords() {
        return words;
    }

    public void setWords(ArrayList<DictionaryEntry> words) {
        this.words = words;
    }

    public List<Float> getPdiscard() {
        return pdiscard;
    }

    public void setPdiscard(ArrayList<Float> pdiscard) {
        this.pdiscard = pdiscard;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNwords() {
        return nwords;
    }

    public void setNwords(int nwords) {
        this.nwords = nwords;
    }

    public int getNlabels() {
        return nlabels;
    }

    public void setNlabels(int nlabels) {
        this.nlabels = nlabels;
    }

    public long getNtokens() {
        return ntokens;
    }

    public void setNtokens(long ntokens) {
        this.ntokens = ntokens;
    }

    public static String getEOS() {
        return EOS;
    }

    public static String getBOW() {
        return BOW;
    }

    public static String getEOW() {
        return EOW;
    }

    public ArrayList<Integer> getLine(InputStream in , Random rng)
            throws IOException {
        String token;
        int ntokens = 0;
        ArrayList<Integer> words = new ArrayList<Integer>();
        ArrayList<Integer> labels = new ArrayList<Integer>();
        while (!(token = readWord(in)).equalsIgnoreCase("")) {
            int wid = getId(token);
            if (wid<0) continue;
            EntryType type = getType(wid);
            ntokens ++;
            if (type==EntryType.word && !discard(wid, rng)) {
                words.add(wid);
            }
            if (type==EntryType.label) {
                labels.add(wid - nwords);
            }
            if (words.size() > MAX_LINE_SIZE && args.getModel() != ModelName.sup) break;
            if (token == EOS) break;
        }
        return words;
    }

    public boolean discard(int id, Random rand) {
        assert(id >= 0);
        assert(id < nwords);
        if (args.getModel() == ModelName.sup) return false;
        return rand.nextGaussian() > pdiscard.get(id);
    }

    private EntryType getType(int wid) {
        assert (wid >= 0);
        assert (wid < size);
        return EntryType.values()[words.get(wid).type];
    }

    private int getId(String token) {
        int h = find(token);
        return word2int[h];
    }

    private String readWord(InputStream in) throws IOException {
        char c;
        String word = "";
        DataInputStream dataInputStream = new DataInputStream(in);
        while ((c = dataInputStream.readChar()) != -1) {
            if (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\0') {
                if (word.length()==0) {
                    if (c == '\n') {
                        word += EOS;
                        return word;
                    }
                    continue;
                } else {
                    return word;
                }
            }
            word+=c;
        }
        return word;
    }

    public void addNgrams(ArrayList<Integer> line, int n) {
        int line_size = line.size();
        for (int i=0 ; i<line_size ; i++) {
            int h = line.get(i);
            for (int j = i+1 ; j<line_size  && j<i+n ; j++) {
                h = h*116049371 + line.get(j);
                line.add(nwords + (h%args.getBucket()));
            }
        }
    }

    public String getLabel(int i) {
        assert (i>=0);
        assert (i<nlabels);
        return words.get(i + nwords).word;
    }
}
