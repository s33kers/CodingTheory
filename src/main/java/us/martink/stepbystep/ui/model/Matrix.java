package us.martink.stepbystep.ui.model;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by tadas.
 */
public class Matrix {

    private static final int q = 2;

    private String matrixText;
    private int[][] matrix;

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

    //Atsitiktine matrica standartiniu pavidalu
    public static int[][] generateRandomMatrix(int n, int k) {
        int[][] matrix = new int[k][n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                if (k-j > 0) {
                    matrix[i][j] = (i == j ? 1 : 0);
                } else {
                    matrix[i][j] = (Math.random() % 2 == 0 ? 1 : 0);
                }
            }

        }
        return matrix;
    }

    //Matricos daugyba is vektoriaus
    public static int[] multiplyByVectorT(int[][] matrix, int[] vector) {
        //Tikriname ar masyvo ilgis atitinka su vektoriaus ilgiu
        if (vector.length != matrix[0].length) {
            throw new IllegalArgumentException();
        }
        int[] multVector = new int[matrix.length];
        //iteruojama per matricos eilutes
        for (int i = 0; i < matrix.length; i++) {
            int sum = 0;
            int[] row = matrix[i];
            //kiekviena matricos eilutes nari dauginam su atitinkamu vektoriaus nariu
            for (int j = 0; j < row.length; j++) {
                sum += row[j]*vector[j];
            }
            sum = sum % q;
            multVector[i] = sum;
        }

        return multVector;
    }

    //Matricos daugyba is vektoriaus
    public static int[] multiplyByVector(int[][] matrix, int[] vector) {
        //Tikriname ar masyvo dimencija atitinka su vektoriaus ilgiu
        if (vector.length != matrix.length) {
            throw new IllegalArgumentException();
        }
        int[] multVector = new int[matrix[0].length];
        //iteruojame per matricos stulpelius
        for (int i = 0; i < matrix[0].length; i++) {
            int sum = 0;
            //iteruojame per vektoriu
            //kiekviena vektoriaus reiksme dauginame is matricos stulepio reiksmes
            for (int j = 0; j < vector.length; j++) {
                int[] row = matrix[j];
                sum += row[i]*vector[j];
            }
            sum = sum % q;
            multVector[i] = sum;
        }

        return multVector;
    }

    //Kontrolines matricos sukurimas
    public int[][] createParityCheckMatrix() {
        int k = matrix.length;
        int n = matrix[0].length;
        int[][] newMatrix = new int[n-k][n];

        //iteruojam per matricos eilutes
        //transponuotos matricos eiluciu skaicius n-k
        for (int i = 0; i < n - k; i++) {
            int[] newRow = new int[n];

            //transponuojama - senos matricos stulpelius suraso i naujos matricos eilute
            for (int j = 0; j < k; j++) {
                int[] row = this.matrix[j];
                int cell = row[k + i];
                newRow[j] = cell;
            }

            //likusia matricos dali uzpildome vienetine matrica,
            //arba jei yra perkeliame likusia matrica
            for (int j = k; j < n; j++) {
                if (j - k < k && k > i) {
                    newRow[j] = matrix[j-k][i];
                } else if (i == j - k) {
                    newRow[j] = 1;
                } else {
                    newRow[j] = 0;
                }

            }
            //pridedame nauja eilute i nauja matrica
            newMatrix[i] = newRow;
        }
        return newMatrix;
    }

    public static String matrixToText(int[][] matrix) {
        StringJoiner rows = new StringJoiner("\n");
        for (int[] aMatrix : matrix) {
            StringJoiner columns = new StringJoiner(" ");
            for (int anAMatrix : aMatrix) {
                columns.add(Integer.toString(anAMatrix));
            }
            rows.add(columns.toString());
        }
        return rows.toString();
    }

    public static int[][] textToMatrix(String matrixText, int k, int n) {
        int[][] matrix =  new int[k][n];
        String[] matrixRows = matrixText.split("\n");
        for (int i = 0; i < matrixRows.length; i++) {
            String matrixRow = matrixRows[i];
            String[] rowValues = matrixRow.split(" ");
            int[] matrixRowValues = Arrays.stream(rowValues).mapToInt(value -> Integer.parseInt(value.trim())).filter(value -> value == 0 || value == 1).toArray();
            matrix[i] = matrixRowValues;
        }
        return matrix;
    }
}
