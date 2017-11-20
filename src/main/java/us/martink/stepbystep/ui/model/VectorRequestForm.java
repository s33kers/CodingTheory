package us.martink.stepbystep.ui.model;

import us.martink.stepbystep.services.Vector;

/**
 * Created by tadas.
 */
public class VectorRequestForm extends RequestForm {

    private String vectorText;
    private int[] vector;
    private Vector encodedVector;

    public String getVectorText() {
        return vectorText;
    }

    public void setVectorText(String vectorText) {
        this.vectorText = vectorText;
    }

    public int[] getVector() {
        return vector;
    }

    public void setVector(int[] vector) {
        this.vector = vector;
    }

    public Vector getEncodedVector() {
        return encodedVector;
    }

    public void setEncodedVector(Vector encodedVector) {
        this.encodedVector = encodedVector;
    }
}
