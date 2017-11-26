package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.Vector;

import java.math.BigInteger;

/**
 * Created by tadas.
 */
public class ByteProcessor {

    /**
     * Užkoduoja bytes su matrix, siunčia kanalu su tikimybe p, atkoduoja gautą rezultatą, kurį ir gražina
     * @param matrix - matrica
     * @param p - klaidos tikimybė
     * @param bytes - baitai, kurie turi būti siunčiami
     * @return gautas atkoduotas rezultatas
     */
    public static byte[] withEncoding(Matrix matrix, double p, byte[] bytes) {
        //bitai paverciamas i binary ir i teksta
        String binary = getTextBinary(bytes);

        //kodo ilgis
        int k = matrix.getMatrix().length;
        int vectorLengthLeft = binary.length() % k;
        binary = correctBinaryLength(binary, vectorLengthLeft);

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
        String result = stringBuilder.toString();
        if (vectorLengthLeft != 0) {
            result = result.substring(0, result.length()-vectorLengthLeft);
        }

        //visas gautas rezultatas konvertuojamas is binary i byte
        return new BigInteger(result, 2).toByteArray();
    }

    /**
     * Baitus siunčia kanalu su tikimybe p ir gautą rezultatą gražina
     * @param matrix - matrica
     * @param p - klaidos tikimybė
     * @param bytes - baitai, kurie turi būti siunčiami
     * @return gautas rezultatas
     */
    public static byte[] withoutEncoding(Matrix matrix, double p, byte[] bytes) {
        //bitai paverciamas i binary ir i teksta
        String binary = getTextBinary(bytes);

        //kodo ilgis
        int k = matrix.getMatrix().length;
        int vectorLengthLeft = binary.length() % k;
        binary = correctBinaryLength(binary, vectorLengthLeft);

        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;

        //iteruojama per visa teksto binary seka
        while (index < binary.length()) {
            //is binary sekos imamas atitinkamo kodo ilgio k vektorius
            String vectorText = binary.substring(index, Math.min(index + k, binary.length()));
            //Binary vektorius teksto pavidalu paverciamas i skaiciu masyvo pavidala
            int[] vector = Vector.textToVector(vectorText);
            //vektorius siunciamas kanalu su klaidos tikimybe p
            Channel.sendThroughChannel(p, vector);
            //gautas vektorius skaiciu masyvo pavidalu paverciamas i teksto pavidala
            stringBuilder.append(Vector.vectorToString(vector, ""));
            index += k;
        }
        String result = stringBuilder.toString();
        if (vectorLengthLeft != 0) {
            result = result.substring(0, result.length()-vectorLengthLeft);
        }

        //visas gautas rezultatas konvertuojamas is binary i byte
        return new BigInteger(result, 2).toByteArray();
    }

    /**
     * Tikrina ar bitai yra reikiamo ilgio - galima bus suskaidyti į vienodus k ilgio vektorius. Jei ne - gale prirašo tiek kiek trūksta 0.
     * @param binary bitų seka
     * @param vectorLengthLeft kiek reikia pataisyti bitu
     * @return tinkamo ilgio bitų seka
     */
    private static String correctBinaryLength(String binary, int vectorLengthLeft) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < vectorLengthLeft; i++) {
            stringBuilder.append("0");
        }
        return binary + stringBuilder.toString();
    }

    /**
     * Iš baitų gauna bitų seką teksto pavidalu
     * @param bytes baitų seka
     * @return bitai teksto pavidale
     */
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
