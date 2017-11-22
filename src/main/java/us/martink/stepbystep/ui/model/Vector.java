package us.martink.stepbystep.ui.model;

import java.util.StringJoiner;

/**
 * Created by tadas.
 */
public class Vector {

    private String vectorText;
    private int[] vector;

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

    public int getVectorSize() {
        return vector.length;
    }

    public static String vectorToString(int[] vector, String regex) {
        StringJoiner stringJoiner = new StringJoiner(regex);
        for (int i : vector) {
            stringJoiner.add(Integer.toString(i));
        }
        return stringJoiner.toString();
    }

    public static int[] textToVector(String vectorText, String s) {
        int[] vector = new int[vectorText.length()];
        for (int i = 0; i < vectorText.length(); i++)
        {
            int value = vectorText.charAt(i) - '0';
            vector[i] = value;
        }
        return vector;
    }
}