package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;

import java.nio.charset.StandardCharsets;

/**
 * Created by tadas.
 */
public class TextProcessor {

    public static String withEncoding(Matrix matrix, double p, String text) {
        byte[] result = ByteProcessor.withEncoding(matrix, p, text.getBytes(StandardCharsets.UTF_8));
        return new String(result, StandardCharsets.UTF_8);
    }

    public static String withoutEncoding(Matrix matrix, double p, String text) {
        byte[] result = ByteProcessor.withoutEncoding(matrix, p, text.getBytes(StandardCharsets.UTF_8));
        return new String(result, StandardCharsets.UTF_8);
    }
}
