package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.Vector;

/**
 * Created by tadas.
 */
public class Decoder {

    public static int[] decodeVector(Matrix matrixG, Vector vector) {
        int[][] matrixH = matrixG.createParityCheckMatrix();
        return new int[0];
    }
}
