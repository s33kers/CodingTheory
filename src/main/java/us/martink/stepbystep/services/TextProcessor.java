package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.Vector;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Created by tadas.
 */
public class TextProcessor {

    public static String withEncoding(Matrix matrix, double p, String text) {
        String binary = getTextBinary(text);

        int k = matrix.getMatrix().length;
        binary = correctBinaryLength(binary, k);

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        while (index < binary.length()) {
            String vector = binary.substring(index, Math.min(index + k, binary.length()));
            int[] encodedVector = Encoder.encodeVector(matrix.getMatrix(), Vector.textToVector(vector));
            Channel.sendThroughChannel(p, encodedVector);
            int[] decodeVector = Decoder.decodeVector(matrix, encodedVector);
            stringBuilder.append(Vector.vectorToString(decodeVector, ""));
            index += k;
        }

        byte[] result = new BigInteger(stringBuilder.toString(), 2).toByteArray();
        return new String(result, StandardCharsets.UTF_8);
    }

    public static String withoutEncoding(Matrix matrix, double p, String text) {
        String binary = getTextBinary(text);

        int k = matrix.getMatrix().length;
        binary = correctBinaryLength(binary, k);

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        while (index < binary.length()) {
            String vector = binary.substring(index, Math.min(index + k, binary.length()));
            int[] encodedVector = Vector.textToVector(vector);
            Channel.sendThroughChannel(p, encodedVector);
            stringBuilder.append(Vector.vectorToString(encodedVector, ""));
            index += k;
        }

        byte[] result = new BigInteger(stringBuilder.toString(), 2).toByteArray();
        return new String(result, StandardCharsets.UTF_8);
    }

    private static String correctBinaryLength(String binary, int k) {
        int vectorLengthLeft = binary.length() % k;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < vectorLengthLeft; i++) {
            stringBuilder.append("0");
        }
        return binary + stringBuilder.toString();
    }

    private static String getTextBinary(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }
}
