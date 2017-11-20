package us.martink.stepbystep.services;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by tadas.
 */
public class Vector {

    private String vectorText;
    private int[] vector;
    private String transferredText;
    private int[] transferred;
    private List<Integer> mistakes = new ArrayList<>();
    private String mistakesText;

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

    public String getTransferredText() {
        return transferredText;
    }

    public void setTransferredText(String transferredText) {
        this.transferredText = transferredText;
    }

    public int[] getTransferred() {
        return transferred;
    }

    public void setTransferred(int[] transferred) {
        this.transferred = transferred;
    }

    public List<Integer> getMistakes() {
        return mistakes;
    }

    public void setMistakes(List<Integer> mistakes) {
        this.mistakes = mistakes;
    }

    public int getMistakesCount() {
        return mistakes.size();
    }

    public String getMistakesText() {
        return mistakesText;
    }

    public void setMistakesText(String mistakesText) {
        this.mistakesText = mistakesText;
    }

    public static String vectorToString(int[] vector, String regex) {
        StringJoiner stringJoiner = new StringJoiner(regex);
        for (int i : vector) {
            stringJoiner.add(Integer.toString(i));
        }
        return stringJoiner.toString();
    }

    public static  String vectorToString(List<Integer> vector, String regex) {
        StringJoiner stringJoiner = new StringJoiner(regex);
        for (Integer integer : vector) {
            stringJoiner.add(Integer.toString(integer));
        }
        return stringJoiner.toString();
    }
}
