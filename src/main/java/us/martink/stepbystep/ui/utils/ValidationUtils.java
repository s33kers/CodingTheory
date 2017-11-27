package us.martink.stepbystep.ui.utils;

import org.apache.commons.lang3.StringUtils;
import us.martink.stepbystep.ui.model.Matrix;
import us.martink.stepbystep.ui.model.PhotoRequestForm;
import us.martink.stepbystep.ui.model.RequestForm;
import us.martink.stepbystep.ui.model.TextRequestForm;
import us.martink.stepbystep.ui.model.VectorRequestForm;

import java.util.Arrays;

/**
 * Created by tadas.
 */
public class ValidationUtils {

    public static String validateBeforeVectorDecoding(VectorRequestForm vectorRequest) {
        String validate = validateBeforeVectorEncoding(vectorRequest);
        if (validate != null) {
            return validate;
        }

        if (vectorRequest.getTransferredVector().getVectorText().length() != vectorRequest.getMatrix().getMatrix()[0].length) {
            return "Blogas vektoriaus gauto iš kanalo ilgis";
        }
        return null;
    }

    public static String validateBeforeVectorEncoding(VectorRequestForm vectorRequest) {
        String validate = validateBasic(vectorRequest);
        if (validate != null) {
            return validate;
        }

        if (StringUtils.isNoneBlank(vectorRequest.getSimpleVector().getVectorText())) {
            int vector = getInteger(vectorRequest.getnText());
            if (vector < 0) {
                return "Blogas vektorius";
            }
            if (vectorRequest.getSimpleVector().getVectorText().length() != vectorRequest.getK()) {
                return "Vektoriaus ilgis turi būti " + vectorRequest.getK();
            }
            int[] simpleVector = new int[vectorRequest.getSimpleVector().getVectorText().length()];
            for (int i = 0; i < vectorRequest.getSimpleVector().getVectorText().length(); i++) {
                int value = vectorRequest.getSimpleVector().getVectorText().charAt(i) - '0';
                if (value != 0 && value != 1) {
                    return "Vektorius turi būti sudarytas iš kūno q = 2 elementų. (0 arba 1)";
                }
                simpleVector[i] = value;
            }
            vectorRequest.getSimpleVector().setVector(simpleVector);
        } else {
            return "Vektorius yra privalomas";
        }
        return null;
    }

    public static String validateBeforeTextSend(TextRequestForm textRequest) {
        String validate = validateBasic(textRequest);
        if (validate != null) {
            return validate;
        }

        if (StringUtils.isEmpty(textRequest.getEnteredText().getTextValue())) {
            return "Tekstas yra privalomas";
        }
        return null;
    }


    public static String validateBeforePhotoSend(PhotoRequestForm vectorRequest) {
        String validate = validateBasic(vectorRequest);
        if (validate != null) {
            return validate;
        }
        return null;
    }

    private static String validateMatrix(RequestForm requestForm) {
        Matrix matrix = requestForm.getMatrix();
        if (StringUtils.isNoneBlank(matrix.getMatrixText())) {
            String[] matrixRows = matrix.getMatrixText().split("\n");

            if (matrixRows.length != requestForm.getK()) {
                return "matricos eilučių skaičius turi būti lygus k";
            }
            matrix.setMatrix(new int[requestForm.getK()][requestForm.getN()]);
            for (int i = 0; i < matrixRows.length; i++) {
                String matrixRow = matrixRows[i];
                String[] rowValues = matrixRow.split(" ");
                int[] matrixRowValues = Arrays.stream(rowValues).mapToInt(value -> Integer.parseInt(value.trim())).filter(value -> value == 0 || value == 1).toArray();
                if (matrixRowValues.length != rowValues.length) {
                    return "Neteisinga matrica. Eilutė: " + i;
                }
                if (matrixRowValues.length != requestForm.getN()) {
                    return "Stulpelių skaičius turi būti lygus n. Eilutė: " + i;
                }

                for (int j = 0; j < requestForm.getK(); j++) {
                    if ((j == i && matrixRowValues[j] != 1) || (j != i && matrixRowValues[j] != 0)) {
                        return "Matrica turi būti standartinio pavidalo. Eilutė: " + i + " Stulpelis: " + j;
                    }
                }

                matrix.getMatrix()[i] = matrixRowValues;
            }
        } else {
            matrix.setMatrix(Matrix.generateRandomMatrix(requestForm.getN(), requestForm.getK()));
            matrix.setMatrixText(Matrix.matrixToText(matrix.getMatrix()));
        }
        return null;
    }

    private static String validateBasic(RequestForm requestForm) {
        if (StringUtils.isNoneBlank(requestForm.getpText())) {
            double p = getDouble(requestForm.getpText());
            if (p > 1 || p < 0) {
                return "p turi būti intervale [0,1]";
            }
            requestForm.setP(p);
        } else {
            return "p yra privalomas";
        }

        if (StringUtils.isNoneBlank(requestForm.getkText())) {
            int k = getInteger(requestForm.getkText());
            if (k <= 0) {
                return "k turi būti virš 0";
            }
            requestForm.setK(k);
        } else {
            return "k yra privalomas";
        }

        if (StringUtils.isNoneBlank(requestForm.getnText())) {
            int n = getInteger(requestForm.getnText());
            if (n <= 0) {
                return "n turi būti virš 0";
            }
            requestForm.setN(n);
        } else {
            return "n yra privalomas";
        }

        String validate = validateMatrix(requestForm);
        if (validate != null) {
            return validate;
        }

        return null;
    }

    private static int getInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
