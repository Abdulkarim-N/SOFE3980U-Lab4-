package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

public class App {

	public static void SVCR(String filePaths){

		String filePath = filePaths;
        FileReader filereader;
        List<String[]> allData;
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file");
            return;
        }
        System.out.println("Data Set: " + filePaths);
        double mseSum = 0.0;
        double maeSum = 0.0;
        double mareSum = 0.0;
        double epsilon = 1e-10;
        int count = 0;

        for (String[] row : allData) {
            double yTrue = Double.parseDouble(row[0]);
            double yPredicted = Double.parseDouble(row[1]);

            double error = yTrue - yPredicted;
            mseSum += error * error;
            maeSum += Math.abs(error);
            mareSum += Math.abs(error) / (Math.abs(yTrue) + epsilon);

            System.out.printf("y_true: %.4f \t y_predicted: %.4f \n", yTrue, yPredicted);
            count++;
            if (count == 10) {
                break;
            }
        }

        if (count > 0) {
            double mse = mseSum / count;
            double mae = maeSum / count;
            double mare = (mareSum / count) * 100;

            System.out.printf("Overall MSE: %.4f\n", mse);
            System.out.printf("Overall MAE: %.4f\n", mae);
            System.out.printf("Overall MARE: %.4f%%\n", mare);
        }
    }

    public static void main(String[] args) {

		String filePath1 = "model_1.csv";
		String filePath2 = "model_2.csv";
		String filePath3 = "model_3.csv";

		SVCR(filePath1);
		SVCR(filePath2);
		SVCR(filePath3);

}
}