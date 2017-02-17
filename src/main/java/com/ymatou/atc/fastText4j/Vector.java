package com.ymatou.atc.fastText4j;

/**
 * Created by fujie on 2017/2/16.
 */
public class Vector {
    private long m;
    private float[] data;

    public Vector(int dim) {
        this.m = dim;
        data = new float[dim];
    }

    public long size() {
        return m;
    }

    public void zero() {
        for (int i =0 ; i<m; i++) {
            data[i] = 0.0f;
        }
    }

    public void mul(float a) {
        for (int i=0 ; i<m ; i++) {
            data[i] *= a;
        }
    }

    public void addRow(final Matrix A, int i) {
        assert (i>=0);
        assert (i < A.m);
        assert (m == A.n);
        for (int j =0 ; j< A.n ; j++) {
            data[j] += A.data[i*A.n +j];
        }
    }

    public void addRow(final Matrix A, int i, float a) {
        assert (i>=0);
        assert (i<A.m);
        assert (m  == A.n);
        for (int j=0 ; j<A.n; j++) {
            data[j] += a * A.data[i*A.n +j];
        }
    }

    public void mul(final Matrix A, final Vector vec) {
        assert (A.m == m);
        assert (A.n == vec.m);
        for (int i =0 ; i<m ; i++ ){
            data[i] = 0.0f;
            for (int j = 0 ; j<A.n ; j++) {
                data[i] += A.data[i*A.n+j] * vec.data[j];
            }
        }
    }

    public int argmax() {
        float max = data[0];
        int argmax = 0;
        for (int i=1; i<m ; i++) {
            if (data[i]>max) {
                max = data[i];
                argmax = i;
            }
        }
        return argmax;
    }

    public float get(int i) {
        return data[i];
    }

    public void set(int i, float v) {
        data[i] = v;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i<m ; i++) {
            sb.append(data[i]).append(" ");
        }
        return sb.toString();
    }

    public long getM() {
        return m;
    }
}
