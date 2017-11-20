package us.martink.stepbystep.ui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import us.martink.stepbystep.services.Channel;
import us.martink.stepbystep.services.Encoder;
import us.martink.stepbystep.services.Matrix;
import us.martink.stepbystep.services.Vector;
import us.martink.stepbystep.ui.model.VectorRequestForm;

import java.util.Arrays;

/**
 * Created by tadas.
 */

@Controller
public class VectorController {

    @RequestMapping("/vector")
    public String vector (Model model) {
        model.addAttribute("requestVector", new VectorRequestForm());
        return "vector";
    }

    @RequestMapping(value="/vector", method= RequestMethod.POST, params="action=Užkoduoti")
    public String save(@ModelAttribute VectorRequestForm vectorRequest, Model model) {
        String validation = validate(vectorRequest);
        if (validation != null) {
            model.addAttribute("validation", validation);
            model.addAttribute("requestVector", vectorRequest);
            return "vector";
        }

        vectorRequest.setEncodedVector(new Vector());
        //encode vector
        vectorRequest.getEncodedVector().setVector(Encoder.encodeVector(vectorRequest.getMatrix(), vectorRequest.getVector()));
        vectorRequest.getEncodedVector().setVectorText(Vector.vectorToString(vectorRequest.getEncodedVector().getVector(), ""));

        //transfer vector
        int[] transferredVector = vectorRequest.getEncodedVector().getVector().clone();
        vectorRequest.getEncodedVector().setMistakes(Channel.sendThroughChannel(vectorRequest.getP(), transferredVector));
        vectorRequest.getEncodedVector().setMistakesText(Vector.vectorToString(vectorRequest.getEncodedVector().getMistakes(), " "));
        vectorRequest.getEncodedVector().setTransferred(transferredVector);
        vectorRequest.getEncodedVector().setTransferredText(Vector.vectorToString(transferredVector, ""));

        model.addAttribute("requestVector", vectorRequest);
        return "vector";
    }

    private String validate(VectorRequestForm vectorRequest) {
        if (StringUtils.isNoneBlank(vectorRequest.getpText())) {
            double p = getDouble(vectorRequest.getpText());
            if (p > 1 || p < 0) {
                return "p turi būti tarp 0 ir 1";
            }
            vectorRequest.setP(p);
        } else {
            return "p yra privalomas";
        }

        if (StringUtils.isNoneBlank(vectorRequest.getkText())) {
            int k = getInteger(vectorRequest.getkText());
            if (k <= 0) {
                return "k turi būti virš 0";
            }
            vectorRequest.setK(k);
        } else {
            return "k yra privalomas";
        }

        if (StringUtils.isNoneBlank(vectorRequest.getnText())) {
            int n = getInteger(vectorRequest.getnText());
            if (n <= 0) {
                return "n turi būti virš 0";
            }
            vectorRequest.setN(n);
        } else {
            return "n yra privalomas";
        }

        if (StringUtils.isNoneBlank(vectorRequest.getVectorText())) {
            int vector = getInteger(vectorRequest.getnText());
            if (vector < 0) {
                return "blogas vektorius";
            }
            if (vectorRequest.getVectorText().length() != vectorRequest.getN()) {
                return "vektoriaus ilgis turi būti lygus " + vectorRequest.getN();
            }
            int[] matrix = new int[vectorRequest.getVectorText().length()];
            for (int i = 0; i < vectorRequest.getVectorText().length(); i++)
            {
                int value = vectorRequest.getVectorText().charAt(i) - '0';
                if (value != 0 && value != 1) {
                    return "vektorius turi būti sudarytas iš kūno q = 2 elementų. (0 ir 1)";
                }
                matrix[i] = value;
            }
            vectorRequest.setVector(matrix);
        } else {
            return "vektorius yra privalomas";
        }

        if (StringUtils.isNoneBlank(vectorRequest.getMatrixText())) {
            String[] matrixRows = vectorRequest.getMatrixText().split("\n");

            if (matrixRows.length != vectorRequest.getN()) {
                return "matricos eilučių skaičius turi būti lygus n";
            }
            vectorRequest.setMatrix(new int[vectorRequest.getK()][vectorRequest.getN()]);
            for (int i = 0; i < matrixRows.length; i++) {
                String matrixRow = matrixRows[i];
                String[] rowValues = matrixRow.split(" ");
                int[] matrixRowValues = Arrays.stream(rowValues).mapToInt(value -> Integer.parseInt(value.trim())).filter(value -> value == 0 || value == 1).toArray();
                if (matrixRowValues.length != rowValues.length) {
                    return "Neteisingas matricos formatas. Eilutė: " + i;
                }
                if (matrixRowValues.length != vectorRequest.getK()) {
                    return "Stulpelių skaičius turi būti lygus k. Eilutė: " + i;
                }

                for (int j = 0; j < vectorRequest.getN(); j++) {
                    if ((j == i && matrixRowValues[j] != 1) || (j != i && matrixRowValues[j] != 0)) {
                        return "Matrica turi būti standartinio pavidalo. Eilutė: " + i + " Stulpelis: " + j;
                    }
                }
                vectorRequest.getMatrix()[i] = matrixRowValues;
            }
        } else {
            vectorRequest.setMatrix(Matrix.generateRandomMatrix(vectorRequest.getN(), vectorRequest.getK()));
            vectorRequest.setMatrixText(Matrix.matrixToString(vectorRequest.getMatrix()));
        }
        return null;
    }

    private static int getInteger(String s) {
        try {
           return Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    private static double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}
