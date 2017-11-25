package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tadas.
 */
public class Decoder {

    public static int[] decodeVector(Matrix matrixG, Vector vector) {
        int[] encodedVector = vector.getVector();
        int[][] matrix = matrixG.getMatrix();
        int k = matrix.length;
        int n = matrix[0].length;

        int[][] matrixH = matrixG.createParityCheckMatrix();
        List<int[]> codeWords = new ArrayList<>();
        int length = (int) Math.pow(2, k);
        for (int i = 0; i < length; i++) {
            codeWords.add(Matrix.multiplyByVector(matrix, Vector.intoBinaryArray(i, k)));
        }

        List<List<int[]>> table = new ArrayList<>();
        table.add(codeWords);

        for (int i = 0; i < Math.pow(2,n)/codeWords.size() - 1 ; i++) {
            setCosetRow(table, n, codeWords.size());
        }

        Map<String, Integer> syndromes = calculateSyndromes(table, matrixH);

        //decoding
        int[] syndrome = Matrix.multiplyByVectorT(matrixH, encodedVector);
        String syndromeText = Vector.vectorToString(syndrome, "");
        int weight = syndromes.get(syndromeText);
        int position = 0;
        int lastWeight;

        while (weight != 0) {
            lastWeight = weight;
            encodedVector = Vector.changeVectorBit(encodedVector, position);
            syndrome = Matrix.multiplyByVectorT(matrixH, encodedVector);//transponuotas
            syndromeText = Vector.vectorToString(syndrome, "");
            weight = syndromes.get(syndromeText);

            if (weight >= lastWeight) {
                Vector.changeVectorBit(encodedVector, position);
                weight = lastWeight;
            }
            position++;
        }
        return encodedVector;
    }

    private static void setCosetRow(List<List<int[]>> table, int n, int size) {
        int combinations = (int) Math.pow(2, n);

        List<List<Integer>> takenNums = table.stream()
                .map(coset -> coset.stream()
                        .map(x -> Integer.parseInt(Vector.vectorToString(x, ""), 2))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<Integer> allPossibleNumbers = new ArrayList<>();
        for (int i = 0; i < combinations; i++) {
            allPossibleNumbers.add(i);
        }

        List<Integer> possNumbers = allPossibleNumbers.stream().filter((x) -> {
            for (List<Integer> row : takenNums) {
                for (int i = 0; i < row.size(); i++) {
                    if (row.indexOf(x) > -1)
                        return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        List<int[]> allPossibleVectors = possNumbers.stream()
                .map(number -> Vector.intoBinaryArray(number, n))
                .collect(Collectors.toList());

        allPossibleVectors.sort(Comparator.comparingInt(Vector::getWeight));

        List<int[]> coset = new ArrayList<>();
        List<int[]> firstCoset = table.get(0);
        int[] cosetLeader = allPossibleVectors.get(0);
        coset.add(cosetLeader);

        for (int i = 1; i < size; i++) {
            coset.add(Vector.addVectors(cosetLeader, firstCoset.get(i)));
        }
        table.add(coset);
    }


    private static Map<String, Integer> calculateSyndromes(List<List<int[]>> table, int[][] matrixH) {
        List<int[]> cosetLeaders = table.stream().map(column -> column.get(0)).collect(Collectors.toList());
        Map<String, Integer> syndromes = new HashMap<>();
        for (int[] cosetLeader : cosetLeaders) {
            int[] syndrome = Matrix.multiplyByVectorT(matrixH, cosetLeader);
            syndromes.put(Vector.vectorToString(syndrome, ""), Vector.getWeight(syndrome));
        }
        return syndromes;
    }
}
