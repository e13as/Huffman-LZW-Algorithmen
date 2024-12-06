package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.collections.impl.HAWListImpl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Klasse zur empirischen Analyse von Binary Search Trees (BST).
 * Diese Klasse misst die durchschnittliche Pfadlänge eines zufälligen Knotens im BST
 * und vergleicht sie mit einer theoretischen Referenzfunktion.
 */
public class BSTAnalysis {

    /**
     * Hauptmethode, die die Analyse des Binary Search Trees (BST) durchführt.
     * Ergebnisse werden in einer CSV-Datei gespeichert.
     */
    public static void main(String[] args) {
        // Listen für Baumgrößen, gemessene Werte und theoretische Werte
        HAWListImpl<Integer> sizes = new HAWListImpl<>();
        HAWListImpl<Double> measuredValues = new HAWListImpl<>();
        HAWListImpl<Double> theoreticalValues = new HAWListImpl<>();

        // Analyse der Baumgrößen von 100 bis 10.000 in Schritten von 100
        for (int n = 100; n <= 10000; n += 100) {
            double totalPathLength = 0.0;

            // Wiederhole die Messung 1.000 Mal für jede Baumgröße
            for (int trial = 0; trial < 1000; trial++) {
                BSTree<Integer, Integer> bst = new BSTree<>();
                Set<Integer> keys = generateRandomKeys(n);

                // Einfügen der Schlüssel in den BST
                for (int key : keys) {
                    bst.insert(key, key);
                }

                // Durchschnittliche Pfadlänge des aktuellen Baums berechnen
                totalPathLength += bst.getAveragePathLength();
            }

            // Berechnung der durchschnittlichen Pfadlänge über alle Trials
            double avgPathLength = totalPathLength / 1000;

            // Ergebnisse speichern
            sizes.add(n);
            measuredValues.add(avgPathLength);
            theoreticalValues.add(calculateTheoreticalValue(n));

            // Ergebnisse für die aktuelle Größe ausgeben
            System.out.printf("n = %d, Average Path Length = %.2f, Theoretical = %.2f\n", n, avgPathLength, calculateTheoreticalValue(n));
        }

        // Ergebnisse in eine CSV-Datei exportieren
        exportToCSV(sizes, measuredValues, theoreticalValues);

        System.out.println("Analyse abgeschlossen. Ergebnisse wurden exportiert.");
    }

    /**
     * Generiert eine Menge zufälliger eindeutiger Schlüsselwerte.
     *
     * @param n Anzahl der zu generierenden Schlüsselwerte.
     * @return Eine Menge eindeutiger Integer-Werte.
     */
    private static Set<Integer> generateRandomKeys(int n) {
        Set<Integer> keys = new HashSet<>();
        Random random = new Random();

        // Zufällige Schlüssel generieren
        while (keys.size() < n) {
            keys.add(random.nextInt(Integer.MAX_VALUE));
        }

        return keys;
    }

    /**
     * Berechnet den theoretischen Wert der durchschnittlichen Pfadlänge.
     *
     * @param n Die Anzahl der Schlüssel im Baum.
     * @return Der theoretische Wert gemäß der Funktion 1.39 * log2(n) - 1.85.
     */
    private static double calculateTheoreticalValue(int n) {
        return 1.39 * (Math.log(n) / Math.log(2)) - 1.85;
    }

    /**
     * Exportiert die Ergebnisse der Analyse in eine CSV-Datei.
     *
     * @param sizes             Die Baumgrößen.
     * @param measuredValues    Die gemessenen durchschnittlichen Pfadlängen.
     * @param theoreticalValues Die theoretischen Werte.
     */
    private static void exportToCSV(HAWListImpl<Integer> sizes, HAWListImpl<Double> measuredValues, HAWListImpl<Double> theoreticalValues) {
        try (PrintWriter writer = new PrintWriter(new File("bst_analysis.csv"))) {
            // Schreibe die Kopfzeile der CSV-Datei
            writer.println("n,Measured,Theoretical");

            // Schreibe die relevanten Datenzeilen
            for (int i = 0; i < sizes.getSize(); i++) {
                writer.printf(Locale.US, "%d,%.2f,%.2f\n",
                        sizes.get(i),
                        measuredValues.get(i),
                        theoreticalValues.get(i)
                );
            }

            System.out.println("Daten erfolgreich in 'bst_analysis.csv' exportiert.");
        } catch (IOException e) {
            // Fehler beim Schreiben der Datei
            e.printStackTrace();
        }
    }

}
