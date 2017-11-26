package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;

/**
 * Created by tadas.
 */
public class Encoder {

    /**
     * Vektoriaus užkodavimas
     * @param matrix matrica
     * @param vector vektorius kurį reikia užkoduoti
     * @return užkoduotas vektorius
     */
    public static int[] encodeVector(int[][] matrix, int[] vector) {
        return Matrix.multiplyByVector(matrix, vector);
    }
}
