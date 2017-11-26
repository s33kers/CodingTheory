package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;

/**
 * Created by tadas.
 */
public class Encoder {

    //vektoriaus uzkodavimas su matrica
    public static int[] encodeVector(int[][] matrix, int[] vector) {
        return Matrix.multiplyByVector(matrix, vector);
    }
}
