package us.martink.stepbystep.ui.model;

/**
 * Created by tadas.
 */
public class PhotoRequestForm extends RequestForm {

    private boolean done;

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
}
