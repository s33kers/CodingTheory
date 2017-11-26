package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.Vector;

import java.math.BigInteger;

/**
 * Created by tadas.
 */
public class ByteProcessor {

    public static byte[] withEncoding(Matrix matrix, double p, byte[] bytes) {
        //bitai paverciamas i binary ir i teksta
        String binary = getTextBinary(bytes);

        //kodo ilgis
        int k = matrix.getMatrix().length;
        binary = correctBinaryLength(binary, k);

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        Decoder decoder = new Decoder(matrix);
        //iteruojama per visa teksto binary seka
        while (index < binary.length()) {
            //is binary sekos imamas atitinkamo kodo ilgio k vektorius
            String vector = binary.substring(index, Math.min(index + k, binary.length()));
            //Gautas vektorius uzkoduojamas
            int[] encodedVector = Encoder.encodeVector(matrix.getMatrix(), Vector.textToVector(vector));
            //vektorius siunciamas kanalu su klaidos tikimybe p
            Channel.sendThroughChannel(p, encodedVector);
            //Gautas vektorius atkoduojamas naudojant StepByStep dekodavimo algoritma
            int[] decodeVector = decoder.decodeVector(encodedVector);
            //gautas vektorius skaiciu masyvo pavidalu paverciamas i teksto pavidala ir dedamas i rezultatu aibe
            stringBuilder.append(Vector.vectorToString(decodeVector, ""));
            index += k;
        }

        //visas gautas rezultatas konvertuojamas is binary i byte
        return new BigInteger(stringBuilder.toString(), 2).toByteArray();
    }

    public static byte[] withoutEncoding(Matrix matrix, double p, byte[] bytes) {
        //bitai paverciamas i binary ir i teksta
        String binary = getTextBinary(bytes);

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
            //gautas vektorius skaiciu masyvo pavidalu paverciamas i teksto pavidala
            stringBuilder.append(Vector.vectorToString(encodedVector, ""));
            index += k;
        }

        //visas gautas rezultatas konvertuojamas is binary i byte
        return new BigInteger(stringBuilder.toString(), 2).toByteArray();
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
    private static String getTextBinary(byte[] bytes) {
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
