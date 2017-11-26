package us.martink.stepbystep.services;

import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tadas.
 */
public class Decoder {

    private Map<String, Integer> syndromes;
    private int[][] matrix;
    private int[][] matrixH;

    public Decoder(Matrix matrixG) {
        matrix = matrixG.getMatrix();
        //Matricai sukuriama jos kontroline matrica
        matrixH = matrixG.createParityCheckMatrix();
        //Matricai gaunama jos standartine lentele
        List<List<int[]>> table = calculateStandardTable(matrix);
        //Gaunami sidromai
        syndromes = calculateSyndromes(table, matrixH);
    }

    /**
     * Dekoduoja vektorių naudojant grandininio dekodavimo algoritmą
     * @param encodedVector vektorius, kurį reikia dekoduoti
     * @return dekoduotas vektorius
     */
    public int[] decodeVector(int[] encodedVector) {
        //StepByStep dekodavimas
        //dauginam vektorių iš kontrolinės matricos
        int[] syndrome = Matrix.multiplyByVectorT(matrixH, encodedVector);
        String syndromeText = Vector.vectorToString(syndrome, "");

        //gauname svorį pagal gautą sindromą
        int weight = syndromes.get(syndromeText);
        int i = 0;
        int lastWeight;

        //iteruojame kol svorį gausime 0, tada bus baigtas dekodavimas
        while (weight != 0) {
            //priskiriame paskutinį svorį
            lastWeight = weight;
            //keičiame vektoriaus bitą pozicijoje i
            encodedVector = Vector.changeVectorBit(encodedVector, i);

            syndrome = Matrix.multiplyByVectorT(matrixH, encodedVector);
            syndromeText = Vector.vectorToString(syndrome, "");
            weight = syndromes.get(syndromeText);

            //jei svoris toks pat, atstatome pakeitą bitą
            if (weight >= lastWeight) {
                Vector.changeVectorBit(encodedVector, i);
                weight = lastWeight;
            }
            i++;
        }

        return Arrays.copyOfRange(encodedVector, 0, matrix.length);
    }

    /**
     * Apskaičiuojama standartinė lentelė matricai
     * @param matrix matrica, kurios standartinę lentelę reikia gauti
     * @return standartinė lentelė
     */
    private static List<List<int[]>> calculateStandardTable(int[][] matrix) {
        int k = matrix.length;
        int n = matrix[0].length;
        List<int[]> codeWords = new ArrayList<>();
        int length = (int) Math.pow(2, k);
        for (int i = 0; i < length; i++) {
            codeWords.add(Matrix.multiplyByVector(matrix, Vector.intoBinaryArray(i, k)));
        }

        List<List<int[]>> table = new ArrayList<>();
        table.add(codeWords);

        for (int i = 0; i < Math.pow(2,n)/codeWords.size() - 1; i++) {
            setCosetRow(table, n, codeWords.size());
        }
        return table;
    }

    /**
     * Apskaičiuojamos klasės pagal kodus esančius lentelės table pradžioje
     * @param table standartinė lentelė
     * @param n kodo dimensija
     * @param size klasės ilgis
     */
    private static void setCosetRow(List<List<int[]>> table, int n, int size) {
        //visas galimų kombinacijų skaičius
        int combinations = (int) Math.pow(2, n);

        //gaunami visi galimi dešimtainiai skaičiai
        List<Integer> allDecimalNumbers = new ArrayList<>();
        for (int i = 0; i < combinations; i++) {
            allDecimalNumbers.add(i);
        }
        //iš standartinės lentelės gaunami visi jau panaudoti skaičiai dešimtainiame formate
        List<List<Integer>> takenDecimal = table.stream()
                .map(coset -> coset.stream()
                        .map(x -> Integer.parseInt(Vector.vectorToString(x, ""), 2))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        //iš visų galimų skaičių atimami jau panaudoti standartinėje lentelėje esantys skaičiai
        List<Integer> candidate = allDecimalNumbers.stream().filter((x) -> {
            for (List<Integer> row : takenDecimal) {
                for (int i = 0; i < row.size(); i++) {
                    if (row.indexOf(x) > -1)
                        return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        //atrinkti galimi skaičiai paverčiami į vektorius
        List<int[]> candidateVectors = candidate.stream()
                .map(number -> Vector.intoBinaryArray(number, n))
                .collect(Collectors.toList());

        //išrikiuojami pagal svorius, pradedant mažiausiu
        candidateVectors.sort(Comparator.comparingInt(Vector::getWeight));

        List<int[]> coset = new ArrayList<>();
        List<int[]> firstCoset = table.get(0);
        //paimamas pirmas vektorius, kuris bus klasės lyderiu
        int[] cosetLeader = candidateVectors.get(0);
        coset.add(cosetLeader);

        //iteruojama tiek kartų kiek reikia klasėje žodžių. Klasės lyderis kiekvieną kartą sudedamas vis su kitu kodu ir patalpinamas lentelėje
        for (int i = 1; i < size; i++) {
            coset.add(Vector.addVectors(cosetLeader, firstCoset.get(i)));
        }
        table.add(coset);
    }


    /**
     * Apskaičiuojami sindromai
     * @param table standartinė lentelė
     * @param matrixH kontrolinė matrica
     * @return sindromai su jų svoriais
     */
    private static Map<String, Integer> calculateSyndromes(List<List<int[]>> table, int[][] matrixH) {
        //Iš standartinės lentelės atfilruojami klasių lyderiai
        List<int[]> cosetLeaders = table.stream().map(column -> column.get(0)).collect(Collectors.toList());
        Map<String, Integer> syndromes = new HashMap<>();
        //iteruojama per visus klasių lyderius ir apskaičiuojamas jo sindromas, kuris su savo svoriu talpinamas sąraše
        for (int[] cosetLeader : cosetLeaders) {
            int[] syndrome = Matrix.multiplyByVectorT(matrixH, cosetLeader);
            syndromes.put(Vector.vectorToString(syndrome, ""), Vector.getWeight(cosetLeader));
        }
        return syndromes;
    }
}
