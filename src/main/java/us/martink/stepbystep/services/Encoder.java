package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;

/**
 * Created by tadas.
 */
public class Encoder {

    public static int[] encodeVector(int[][] matrix, int[] vector) {
        return Matrix.multiplyByVectorT(matrix, vector);
    }
}
