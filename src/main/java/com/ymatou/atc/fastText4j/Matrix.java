package com.ymatou.atc.fastText4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by fujie on 2017/2/16.
 */
public class Matrix {
    public float[] data;
    public int m;
    public int n;


    public void load(InputStream in) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(in);
        m = (int)dataInputStream.readLong();
        n = (int)dataInputStream.readLong();
        data = new float[m*n];
        for (int i =0 ; i<m ; i++) {
            for (int j=0 ; j<n ;j++) {
                data[i*m+j] = dataInputStream.readFloat();
            }
        }
    }

    public float dotRow(Vector vec, int i) {
        assert (i>=0);
        assert (i<m);
        assert (vec.getM() == n);
        float d = 0.0f;
        for (int j=0 ; j<n; j++) {
            d += data[i*n+j]*vec.get(i);
        }
        return d;
    }
}
