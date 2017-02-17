package com.ymatou.atc.fastText4j;

import java.util.ArrayList;

/**
 * Created by fujie on 2017/2/17.
 */
public class Heap {
    public static void adjustHeap(ArrayList<Prediction> data, int i, int sz){
        int nChild = 0;
        Prediction temp;
        for (; 2*i+1<sz; i=nChild) {
            nChild = 2*i + 1;
            //left child > right child
            if (nChild < sz -1 &&  (data.get(nChild)).compareTo(data.get(nChild+1))<0) {
                ++nChild;
            }
            //now > child
            if (data.get(i).compareTo(data.get(nChild))<0) {
                temp = data.get(i);
                data.set(i, data.get(nChild));
                data.set(nChild, temp);
            } else break;
        }
    }

    public static void heapSort(ArrayList<Prediction> data){
        for (int sz = data.size() ; sz>0 ; sz--) {
            adjustHeap(data, 0, sz);
            Prediction temp = data.get(0);
            data.set(0, data.get(sz-1));
            data.set(sz-1, temp);
        }
    }


    public static void popHeap(ArrayList<Prediction> data) {
        data.remove(0);
    }
}
