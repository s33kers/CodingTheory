package us.martink.stepbystep.services;

/**
 * Created by tadas.
 */
public class Encoder {

    public static int[] encodeVector(int[][] matrix, int[] vector) {
        return Matrix.multiplyByVector(matrix, vector);
    }
}
