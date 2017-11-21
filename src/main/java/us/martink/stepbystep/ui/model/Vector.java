package us.martink.stepbystep.ui.model;

import java.util.List;
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

    public static String vectorToString(List<Integer> vector, String regex) {
        StringJoiner stringJoiner = new StringJoiner(regex);
        for (Integer integer : vector) {
            stringJoiner.add(Integer.toString(integer));
        }
        return stringJoiner.toString();
    }
}
