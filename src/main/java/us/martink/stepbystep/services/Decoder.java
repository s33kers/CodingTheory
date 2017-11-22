package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tadas.
 */
public class Decoder {

    public static int[] decodeVector(Matrix matrixG, Vector vector) {
        int[][] matrix = matrixG.getMatrix();
        int k = matrix.length;
        int n = matrix[0].length;

        int[][] matrixH = matrixG.createParityCheckMatrix();
        List<int[]> codeWords = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            codeWords.add(Matrix.multiplyByVector(matrix, Vector.textToVector(Integer.toBinaryString(i))));
        }

        List<List<int[]>> table = new ArrayList<>();
        table.add(codeWords);

        for (int i = 0; i < Math.pow(2,n)/codeWords.size() - 1 ; i++) {
            setCosetRow(table, n, codeWords.size());
        }

//        int syndromesToWeight = getSyndromes(table, matrixH);
        return new int[0];
    }

    private static void setCosetRow(List<List<int[]>> table, int n, int size) {
        int combinations = 2^n;

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
                .map(number -> Vector.textToVector(Integer.toBinaryString(number)))
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
}
