package us.martink.stepbystep.ui.model;

import java.io.Serializable;

/**
 * Created by tadas.
 */
public class RequestForm implements Serializable {

    private String nText;
    private int n;
    private String kText;
    private int k;
    private String pText;
    private double p;
    private Matrix matrix = new Matrix();

    public String getnText() {
        return nText;
    }

    public void setnText(String nText) {
        this.nText = nText;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getkText() {
        return kText;
    }

    public void setkText(String kText) {
        this.kText = kText;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public String getpText() {
        return pText;
    }

    public void setpText(String pText) {
        this.pText = pText;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
