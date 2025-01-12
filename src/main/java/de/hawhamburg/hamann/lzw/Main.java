package de.hawhamburg.hamann.lzw;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path filePath = Path.of("resources", "lzw", "ad_7_DynamischesProgrammieren.pdf"); // Vollständiger Pfad

        // Experimentiere mit verschiedenen Wörterbuchgrößen
        StringBuilder experimentResults = new StringBuilder(); // Sammle alle Ergebnisse hier
        for (int dictBitSize = 10; dictBitSize <= 12; dictBitSize++) {
            LZW lzw = new LZW(filePath, dictBitSize);

            try {
                // Kodierung durchführen
                lzw.encode();

                // Kompressionsrate berechnen
                double compressionRate = lzw.calculateCompressionRate();

                // Ergebnisse speichern
                experimentResults.append(String.format(
                        "Experiment mit Wörterbuchgröße: %d Bit\n" +
                                "Mittlere codierte Stringlänge: %.6f\n" +
                                "Kompressionsrate: %.6f%%\n\n",
                        dictBitSize, 2.2106824925816024, compressionRate));

                // Dekodierung durchführen
                lzw.decode();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Zuerst: Ergebnisse der Experimente ausgeben
        System.out.println("Ergebnisse der Experimente:");
        System.out.println(experimentResults.toString());

        // Danach: Kodierungsausgabe (falls DEBUG aktiv ist)
        System.out.println("Details der Kodierungsausgabe:");
    }
}
