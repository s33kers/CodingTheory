package us.martink.stepbystep.ui.model;

/**
 * Created by tadas.
 */
public class VectorRequestForm extends RequestForm {

    private Vector simpleVector = new Vector();
    private Vector encodedVector;
    private Vector transferredVector;
    private Vector mistakes;

    public Vector getSimpleVector() {
        return simpleVector;
    }

    public void setSimpleVector(Vector simpleVector) {
        this.simpleVector = simpleVector;
    }

    public Vector getEncodedVector() {
        return encodedVector;
    }

    public void setEncodedVector(Vector encodedVector) {
        this.encodedVector = encodedVector;
    }

    public Vector getTransferredVector() {
        return transferredVector;
    }

    public void setTransferredVector(Vector transferredVector) {
        this.transferredVector = transferredVector;
    }

    public Vector getMistakes() {
        return mistakes;
    }

    public void setMistakes(Vector mistakes) {
        this.mistakes = mistakes;
    }
}
