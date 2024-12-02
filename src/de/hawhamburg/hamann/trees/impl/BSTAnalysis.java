package de.hawhamburg.hamann.trees.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BSTAnalysis {
    public static void main(String[] args) {
        List<Double> measuredValues = new ArrayList<>();
        List<Double> theoreticalValues = new ArrayList<>();
        List<Integer> sizes = new ArrayList<>();

        for (int n = 100; n <= 10000; n += 100) {
            double totalPathLength = 0;

            for (int trial = 0; trial < 1000; trial++) {
                BSTree<Integer, Integer> bst = new BSTree<>();
                Set<Integer> keys = generateRandomKeys(n);

                for (int key : keys) {
                    bst.insert(key, key);
                }

                totalPathLength += bst.getAveragePathLength();
            }

            double avgPathLength = totalPathLength / 1000;
            measuredValues.add(avgPathLength);
            theoreticalValues.add(1.39 * Math.log(n) / Math.log(2) - 1.85);
            sizes.add(n);

            System.out.printf("n = %d, Average Path Length = %.4f\n", n, avgPathLength);
        }

        // Export results to CSV
        exportToCSV(sizes, measuredValues, theoreticalValues);

        // Optional: Plot results (use external tools or libraries)
        plotResults(sizes, measuredValues, theoreticalValues);
    }


    private static Set<Integer> generateRandomKeys(int n) {
        Set<Integer> keys = new HashSet<>();
        Random random = new Random();

        while (keys.size() < n) {
            keys.add(random.nextInt(Integer.MAX_VALUE));
        }

        return keys;
    }

    private static void plotResults(List<Integer> sizes, List<Double> measuredValues, List<Double> theoreticalValues) {
        // Export to CSV or use a Java charting library
        System.out.println("Plotting results...");
    }

    private static void exportToCSV(List<Integer> sizes, List<Double> measuredValues, List<Double> theoreticalValues) {
        try (PrintWriter writer = new PrintWriter(new File("bst_analysis.csv"))) {
            writer.println("n,Measured,Theoretical");

            for (int i = 0; i < sizes.size(); i++) {
                writer.printf("%d,%.4f,%.4f\n", sizes.get(i), measuredValues.get(i), theoreticalValues.get(i));
            }

            System.out.println("Data exported to bst_analysis.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

