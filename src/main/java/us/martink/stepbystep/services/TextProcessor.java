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
        //tekstas paverciamas i binary
        String binary = getTextBinary(text);

        //kodo ilgis
        int k = matrix.getMatrix().length;
        binary = correctBinaryLength(binary, k);

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;

        //iteruojama per visa teksto binary seka
        while (index < binary.length()) {
            //is binary sekos imamas atitinkamo kodo ilgio k vektorius
            String vector = binary.substring(index, Math.min(index + k, binary.length()));
            //Gautas vektorius uzkoduojamas
            int[] encodedVector = Encoder.encodeVector(matrix.getMatrix(), Vector.textToVector(vector));
            //vektorius siunciamas kanalu su klaidos tikimybe p
            Channel.sendThroughChannel(p, encodedVector);
            //Gautas vektorius atkoduojamas naudojant StepByStep dekodavimo algoritma
            int[] decodeVector = Decoder.decodeVector(matrix, encodedVector);
            //gautas vektorius skaiciu masyvo pavidalu paverciamas i teksto pavidala ir dedamas i rezultatu aibe
            stringBuilder.append(Vector.vectorToString(decodeVector, ""));
            index += k;
        }

        byte[] result = new BigInteger(stringBuilder.toString(), 2).toByteArray();
        return new String(result, StandardCharsets.UTF_8);
    }

    public static String withoutEncoding(Matrix matrix, double p, String text) {
        //tekstas paverciamas i binary
        String binary = getTextBinary(text);

        //kodo ilgis
        int k = matrix.getMatrix().length;
        binary = correctBinaryLength(binary, k);

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;

        //iteruojama per visa teksto binary seka
        while (index < binary.length()) {
            //is binary sekos imamas atitinkamo kodo ilgio k vektorius
            String vector = binary.substring(index, Math.min(index + k, binary.length()));
            //Binary vektorius teksto pavidalu paverciamas i skaiciu masyvo pavidala
            int[] encodedVector = Vector.textToVector(vector);
            //vektorius siunciamas kanalu su klaidos tikimybe p
            Channel.sendThroughChannel(p, encodedVector);
            //gautas vektorius skaiciu masyvo pavidalu paverciamas i teksto pavidala ir dedamas i rezultatu aibe
            stringBuilder.append(Vector.vectorToString(encodedVector, ""));
            index += k;
        }

        //visas gautas rezultatas konvertuojamas is binary i byte
        byte[] result = new BigInteger(stringBuilder.toString(), 2).toByteArray();
        //binary i teksta su UTF-8 koduote
        return new String(result, StandardCharsets.UTF_8);
    }

    //Jei binary teksto ilgis neatitinka kodo ilgio prideti papildomus 0 gale
    private static String correctBinaryLength(String binary, int k) {
        int vectorLengthLeft = binary.length() % k;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < vectorLengthLeft; i++) {
            stringBuilder.append("0");
        }
        return binary + stringBuilder.toString();
    }

    //Teksta paversti i binary
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
