package us.martink.stepbystep.ui.model;

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

    public static int[][] generateRandomMatrix(int n, int k) {
        int[][] matrix = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                if (n-j > 0) {
                    matrix[i][j] = (i == j ? 1 : 0);
                } else {
                    matrix[i][j] = (Math.random() % 2 == 0 ? 1 : 0);
                }
            }

        }
        return matrix;
    }

    public static String matrixToString(int[][] matrix) {
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

    //todo 3x2 blogai, check
    public static int[] multiplyByVector(int[][] matrix, int[] vector) {
        int[] multVector = new int[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            int sum = 0;
            int[] row = matrix[i];
            for (int j = 0; j < row.length; j++) {
                sum += row[j]*vector[j];
            }
            multVector[i] = sum % q;
        }

        return multVector;
    }
}
