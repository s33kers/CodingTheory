package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;

import java.nio.charset.StandardCharsets;

/**
 * Created by tadas.
 */
public class TextProcessor {

    /**
     * Tekstą paverčia į baitus ir peruoda apdoroti į ByteProcessor. Gražintą baitų rezultatą verčia į tekstą. Naudojamas užkodavimas ir dekodavimas siuntimo metu.
     * @param matrix matrica
     * @param p klaidos tikimybė
     * @param text siunčiamas tekstas
     * @return gautas tekstas po siuntimo
     */
    public static String withEncoding(Matrix matrix, double p, String text) {
        byte[] result = ByteProcessor.withEncoding(matrix, p, text.getBytes(StandardCharsets.UTF_8));
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * Tekstą paverčia į baitus ir peruoda apdoroti į ByteProcessor. Gražintą baitų rezultatą verčia į tekstą. Nenaudojamas užkodavimas ir dekodavimas siuntimo metu.
     * @param matrix matrica
     * @param p klaidos tikimybė
     * @param text siunčiamas tekstas
     * @return gautas tekstas po siuntimo
     */
    public static String withoutEncoding(Matrix matrix, double p, String text) {
        byte[] result = ByteProcessor.withoutEncoding(matrix, p, text.getBytes(StandardCharsets.UTF_8));
        return new String(result, StandardCharsets.UTF_8);
    }
}
