package de.hawhamburg.hamann.huffman;
// Definiert das Paket, in dem diese Klassen gespeichert sind.

import com.github.jinahya.bit.io.*;
// Importiert eine Bibliothek, die das Arbeiten mit Bits und Bytes vereinfacht.

import java.io.*;
// Importiert grundlegende Klassen für die Ein-/Ausgabe.

import java.nio.file.Files;
import java.nio.file.Path;
// Importiert Klassen für Dateioperationen und Dateipfade.

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.stream.IntStream;
// Importiert Hilfsklassen für Datenstrukturen, Arrays, Stacks und Streams.

class BTree<D extends HuffNode> implements Comparable<BTree<D>> {
// Generische Binärbaumklasse. Die Daten im Baum müssen vom Typ `HuffNode` sein.

    D data;
    // Daten des Knotens, hier ein `HuffNode`, der ein Zeichen und dessen Häufigkeit speichert.

    BTree<D> links;
    // Linker Teilbaum des aktuellen Knotens.

    BTree<D> rechts;
    // Rechter Teilbaum des aktuellen Knotens.

    public BTree(D data) {
        this(data, null, null);
    }
    // Konstruktor für einen Knoten ohne Kinder.

    public BTree(D data, BTree<D> links, BTree<D> rechts) {
        this.data = data;
        this.links = links;
        this.rechts = rechts;
    }
    // Konstruktor für einen Knoten mit angegebenen Daten und Teilbäumen.

    public boolean isLeaf() {
        return links == null && rechts == null;
    }
    // Prüft, ob der aktuelle Knoten ein Blattknoten (kein Kindknoten) ist.

    @Override
    public int compareTo(BTree<D> other) {
        return Integer.compare(data.frequency, other.data.frequency);
    }
    // Vergleicht zwei Bäume basierend auf der Häufigkeit der Zeichen.
}

class HuffNode {
    int frequency;
    // Häufigkeit des Zeichens in der Eingabedatei.

    char character;
    // Das Zeichen selbst.

    public HuffNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }
    // Konstruktor, um einen Knoten mit einem Zeichen und dessen Häufigkeit zu erstellen.
}

class Huffman {
    private static final int CHARS = 256;
    // Maximale Anzahl an Zeichen (ASCII-Zeichensatz).

    private static final int ALIGN = 31;
    // Anzahl der Bits für die Speicherung der Häufigkeiten.

    private final Path filePath;
    // Pfad zur Eingabedatei.

    private final Path encodedPath;
    // Pfad zur Datei, in der die kodierten Daten gespeichert werden.

    private final Path decodedPath;
    // Pfad zur Datei, in der die dekodierten Daten gespeichert werden.

    private final int[] freq = new int[CHARS];
    // Array, das die Häufigkeit jedes Zeichens speichert.

    private final Integer[][] codeTable = new Integer[CHARS][ALIGN];
    // Tabelle zur Speicherung der Huffman-Codes für jedes Zeichen.

    private final PriorityQueue<BTree<HuffNode>> heap = new PriorityQueue<>();
    // Min-Heap zur Konstruktion des Huffman-Baums.

    private final Stack<Integer> path = new Stack<>();
    // Stack, um den aktuellen Pfad (0/1) während der Baumtraversierung zu speichern.

    public Huffman(Path filePath) {
        this.filePath = filePath;
        // Speichert den Pfad zur Eingabedatei.

        encodedPath = this.filePath.getParent()
                .resolve("encoded_" + filePath.getFileName());
        decodedPath = this.filePath.getParent()
                .resolve("decoded_" + filePath.getFileName());
        // Initialisiert Pfade für die kodierte und dekodierte Datei.
    }

    public void encode() {
        IntStream.range(0, freq.length)
                .forEach(i -> freq[i] = 0);
        // Setzt alle Häufigkeiten auf 0 zurück.

        calculateCharacterFrequencies();
        // Liest die Eingabedatei und berechnet die Häufigkeiten aller Zeichen.

        BTree<HuffNode> root = buildHuffmanTree();
        // Erstellt den Huffman-Baum basierend auf den Zeichenhäufigkeiten.

        calculateCodeFromHuffmanTree(root);
        // Traversiert den Baum und berechnet die Huffman-Codes für jedes Zeichen.

        try {
            Files.deleteIfExists(encodedPath);
            Files.createFile(encodedPath);
            // Löscht die alte Datei (falls vorhanden) und erstellt eine neue Datei.

            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(encodedPath.toFile()))) {
                StreamByteOutput sbOut = new StreamByteOutput(out);
                BitOutput bitOut = new DefaultBitOutput(sbOut);
                // Initialisiert die Ausgabe für Bits.

                writeCharacterFrequencies(bitOut);
                // Schreibt die Zeichenhäufigkeiten in die Datei.

                encodeDataFile(bitOut);
                // Kodiert die Daten und schreibt sie in die Datei.

                bitOut.align(1);
                // Passt die Ausrichtung an (fügt ggf. Padding-Bits hinzu).
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeHeap() {
        for (char c = 0; c < CHARS; c++) {
            if (freq[c] > 0) heap.add(new BTree<>(new HuffNode(c, freq[c])));
        }
        // Fügt alle Zeichen mit einer Häufigkeit > 0 in den Min-Heap ein.
    }

    private void calculateCharacterFrequencies() {
        try (InputStream in = new BufferedInputStream(new FileInputStream(filePath.toFile()))) {
            StreamByteInput sbIn = new StreamByteInput(in);
            BitInput bitIn = new DefaultBitInput(sbIn);

            for (; ; ) freq[bitIn.readChar(8)]++;
            // Liest jedes Zeichen aus der Datei und erhöht die entsprechende Häufigkeit.

        } catch (EOFException ignored) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initializeHeap();
        // Initialisiert den Min-Heap mit den berechneten Häufigkeiten.
    }

    private BTree<HuffNode> buildHuffmanTree() {
        while (heap.size() > 1) {
            BTree<HuffNode> tree1 = heap.remove();
            BTree<HuffNode> tree2 = heap.remove();
            // Entfernt die beiden Knoten mit den kleinsten Häufigkeiten.

            heap.add(new BTree<>(new HuffNode((char) 0, tree1.data.frequency + tree2.data.frequency), tree1, tree2));
            // Erstellt einen neuen Knoten als Eltern der beiden kleinsten Knoten.
        }
        return heap.remove();
        // Gibt die Wurzel des Huffman-Baums zurück.
    }

    private void calculateCodeFromHuffmanTree(BTree<HuffNode> tree) {
        if (tree.isLeaf()) {
            codeTable[tree.data.character] = Arrays.copyOf(path.toArray(), path.size(), Integer[].class);
            // Speichert den Code für ein Blatt (Zeichen).

            return;
        }
        path.push(0);
        calculateCodeFromHuffmanTree(tree.links);
        path.pop();
        // Rekursive Traversierung nach links.

        path.push(1);
        calculateCodeFromHuffmanTree(tree.rechts);
        path.pop();
        // Rekursive Traversierung nach rechts.
    }

    private void encodeDataFile(BitOutput bitOut) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(filePath.toFile()))) {
            StreamByteInput sbIn = new StreamByteInput(in);
            BitInput bitIn = new DefaultBitInput(sbIn);

            for (; ; ) {
                char c = bitIn.readChar(8);
                for (Integer bit : codeTable[c]) {
                    bitOut.writeBoolean(bit == 1);
                }
                // Liest ein Zeichen und schreibt dessen Code-Bits in die Ausgabedatei.
            }
        } catch (EOFException ignored) {
        }
    }

    private void writeCharacterFrequencies(BitOutput output) throws IOException {
        for (int i = 0; i < freq.length; i++) {
            output.writeInt(true, ALIGN, freq[i]);
            // Schreibt die Häufigkeit jedes Zeichens in die kodierte Datei.
        }
    }

    public void decode() {
        IntStream.range(0, freq.length)
                .forEach(i -> freq[i] = 0);
        // Setzt alle Häufigkeiten auf 0 zurück.

        try (InputStream in = new BufferedInputStream(new FileInputStream(encodedPath.toFile()))) {
            StreamByteInput sbIn = new StreamByteInput(in);
            BitInput bitIn = new DefaultBitInput(sbIn);

            readCharacterFrequencies(bitIn);
            // Liest die Häufigkeiten aus der kodierten Datei.

            initializeHeap();
            // Initialisiert den Min-Heap.

            BTree<HuffNode> root = buildHuffmanTree();
            // Baut den Huffman-Baum.

            decodeDataFile(root, bitIn);
            // Dekodiert die Daten aus der kodierten Datei.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void decodeDataFile(BTree<HuffNode> root, BitInput bitIn) throws IOException {
        int count = root.data.frequency;
        // Anzahl der Zeichen in der Originaldatei.

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(decodedPath.toFile()))) {
            StreamByteOutput sbOut = new StreamByteOutput(out);
            BitOutput bitOut = new DefaultBitOutput(sbOut);

            for (int i = 0; i < count; i++) {
                bitOut.writeChar(8, decodeR(root, bitIn));
                // Dekodiert ein Zeichen und schreibt es in die Ausgabedatei.
            }
        } catch (EOFException ignored) {
        }
    }

    private void readCharacterFrequencies(BitInput bitIn) throws IOException {
        for (int i = 0; i < freq.length; i++) {
            freq[i] = bitIn.readInt(true, ALIGN);
            // Liest die Häufigkeiten jedes Zeichens aus der kodierten Datei.
        }
    }

    private char decodeR(BTree<HuffNode> tree, BitInput bitIn) throws IOException {
        if (tree.isLeaf()) return tree.data.character;
        // Gibt das Zeichen zurück, wenn ein Blattknoten erreicht ist.

        if (!bitIn.readBoolean()) return decodeR(tree.links, bitIn);
        return decodeR(tree.rechts, bitIn);
        // Navigiert durch den Baum basierend auf den Bits (0 = links, 1 = rechts).
    }
}
