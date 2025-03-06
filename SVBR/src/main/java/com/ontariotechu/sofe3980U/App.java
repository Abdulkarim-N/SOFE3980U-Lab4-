package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.opencsv.*;

/**
 * Evaluate Single Variable Binary Regression
 */
public class App {
    public static void main(String[] args) {
        String filePath = "model_1.csv";
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
        
        int TP = 0, FP = 0, TN = 0, FN = 0;
        double BCE = 0.0;
        double threshold = 0.5;
        int n = allData.size();
        List<Double> y_true_list = new ArrayList<>();
        List<Double> y_pred_list = new ArrayList<>();

        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]);
            double y_pred = Double.parseDouble(row[1]);
            y_true_list.add((double) y_true);
            y_pred_list.add(y_pred);
            
            // Calculate BCE
            BCE += y_true * Math.log(y_pred + 1e-9) + (1 - y_true) * Math.log(1 - y_pred + 1e-9);
            
            // Convert predicted value to binary using threshold
            int y_pred_binary = (y_pred >= threshold) ? 1 : 0;
            
            // Confusion Matrix Calculation
            if (y_true == 1 && y_pred_binary == 1) {
                TP++;
            } else if (y_true == 0 && y_pred_binary == 1) {
                FP++;
            } else if (y_true == 0 && y_pred_binary == 0) {
                TN++;
            } else if (y_true == 1 && y_pred_binary == 0) {
                FN++;
            }
        }

        BCE = -BCE / n;

        double accuracy = (double) (TP + TN) / (TP + TN + FP + FN);
        double precision = (TP + FP) > 0 ? (double) TP / (TP + FP) : 0;
        double recall = (TP + FN) > 0 ? (double) TP / (TP + FN) : 0;
        double f1_score = (precision + recall) > 0 ? 2 * precision * recall / (precision + recall) : 0;

        // Calculate ROC Curve & AUC
        int n_positive = Collections.frequency(y_true_list, 1.0);
        int n_negative = Collections.frequency(y_true_list, 0.0);
        double[] x = new double[101];
        double[] y = new double[101];

        double auc = 0;
        for (int i = 0; i <= 100; i++) {
            double th = i / 100.0;
            int tp_count = 0, fp_count = 0, fn_count = 0, tn_count = 0;
            
            for (int j = 0; j < n; j++) {
                double pred = y_pred_list.get(j);
                int true_label = y_true_list.get(j).intValue();
                
                if (true_label == 1 && pred >= th) tp_count++;
                if (true_label == 0 && pred >= th) fp_count++;
                if (true_label == 1 && pred < th) fn_count++;
                if (true_label == 0 && pred < th) tn_count++;
            }
            
            double tpr = (double) tp_count / (tp_count + fn_count + 1e-9);
            double fpr = (double) fp_count / (fp_count + tn_count + 1e-9);
            
            x[i] = fpr;
            y[i] = tpr;
            
            if (i > 0) {
                auc += (y[i - 1] + y[i]) * Math.abs(x[i - 1] - x[i]) / 2;
            }
        }

        // Print results
        System.out.println("Binary Cross-Entropy (BCE): " + BCE);
        System.out.println("Confusion Matrix: ");
        System.out.println("TP: " + TP + " FP: " + FP);
        System.out.println("FN: " + FN + " TN: " + TN);
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F1 Score: " + f1_score);
        System.out.println("AUC-ROC: " + auc);
    }
}
