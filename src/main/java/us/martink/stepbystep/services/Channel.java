package us.martink.stepbystep.services;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tadas.
 */
public class Channel {

    private static final int q = 2;

    public static List<Integer> sendThroughChannel(double p, int[] vector) {
        List<Integer> mistakes = new ArrayList<>();

        for (int i = 0; i < vector.length; i++) {
            if (p > Math.random()) {
                vector[i] = (vector[i] + 1) % q;
                mistakes.add(i);
            }
        }

        return mistakes;
    }
}
