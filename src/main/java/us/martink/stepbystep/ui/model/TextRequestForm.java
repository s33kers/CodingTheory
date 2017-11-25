package us.martink.stepbystep.ui.model;

/**
 * Created by tadas.
 */
public class TextRequestForm extends RequestForm {

    private Text enteredText = new Text();
    private Text encodedText;
    private Text plainText;

    public Text getEnteredText() {
        return enteredText;
    }

    public void setEnteredText(Text enteredText) {
        this.enteredText = enteredText;
    }

    public Text getEncodedText() {
        return encodedText;
    }

    public void setEncodedText(Text encodedText) {
        this.encodedText = encodedText;
    }

    public Text getPlainText() {
        return plainText;
    }

    public void setPlainText(Text plainText) {
        this.plainText = plainText;
    }
}
