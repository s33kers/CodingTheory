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
    private String matrixText;
    private int[][] matrix;

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

    public String getMatrixText() {
        return matrixText;
    }

    public void setMatrixText(String matrixText) {
        this.matrixText = matrixText;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }
}
