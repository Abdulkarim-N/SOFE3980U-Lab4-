package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class App {
    // Helper function to find the index of the maximum value in an array
    private static int argMax(float[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    public static void main(String[] args) {
        String filePath = "model.csv"; // Ensure this path is correct
        List<String[]> allData;

        try (FileReader filereader = new FileReader(filePath);
             CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build()) {
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
            return;
        }

        int count = 0;
        float[] y_predicted = new float[5];
        int[] confusionMatrix = new int[5 * 5]; // 5x5 confusion matrix
        float crossEntropy = 0.0f;

        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]) - 1; // Convert to zero-based index
            for (int i = 0; i < 5; i++) {
                y_predicted[i] = Float.parseFloat(row[i + 1]);
            }

            // Calculate cross-entropy
            crossEntropy -= Math.log(y_predicted[y_true]);

            // Update confusion matrix
            int predictedClass = argMax(y_predicted);
            confusionMatrix[y_true * 5 + predictedClass]++;
            
        }

        // Calculate average cross-entropy
        crossEntropy /= allData.size();
        System.out.println("\nCross-Entropy: " + crossEntropy);

        // Print confusion matrix
        System.out.println("\nConfusion Matrix:");
        System.out.println("Rows: True Class, Columns: Predicted Class");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(confusionMatrix[i * 5 + j] + "\t");
            }
            System.out.println();
        }
    }

}