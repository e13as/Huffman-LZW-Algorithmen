package de.hawhamburg.hamann.huffman;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            Path filePath = Path.of("resources", "huffman", "ad_7_DynamischesProgrammieren.pdf");

Huffman huffman = new Huffman(filePath);

            System.out.println("Starte Huffman-Encoding...");
            huffman.encode();
            System.out.println("Encoding abgeschlossen!");

            System.out.println("Starte Huffman-Decoding...");
            huffman.decode();
            System.out.println("Decoding abgeschlossen!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
