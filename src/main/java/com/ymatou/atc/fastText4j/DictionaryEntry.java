package com.ymatou.atc.fastText4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fujie on 2017/2/17.
 */
public class DictionaryEntry {
    public String word;
    public long count;
    public int type;
    public List<Integer> subwords;

    public DictionaryEntry() {
        word = "";
        count = 0;
        subwords = new ArrayList<Integer>();
    }
}
