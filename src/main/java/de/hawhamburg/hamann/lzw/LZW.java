package de.hawhamburg.hamann.lzw;

import com.github.jinahya.bit.io.*; // Import für Bit-Eingabe/Ausgabe-Bibliothek

import java.io.*; // Import für Ein-/Ausgabeströme
import java.nio.file.Files; // Import für Dateisystemoperationen
import java.nio.file.Path; // Import für Pfadoperationen
import java.util.*; // Import für Datenstrukturen wie List und Map

public class LZW {

    private static final boolean DEBUG = true; // Aktiviert Debug-Ausgaben
    private static final int CHAR_BIT_SIZE = 8; // Bitgröße für ein Zeichen (1 Byte)
    private final Path filePath; // Pfad zur Eingabedatei
    private final Path encodedPath; // Pfad zur codierten Ausgabe
    private final Path decodedPath; // Pfad zur dekodierten Ausgabe

    private final int dictBitSize; // Maximale Bitgröße für Wörterbucheinträge
    private final int chars; // Anzahl der möglichen Zeichen (2^8 für ASCII)
    private HashMap<List<Character>, Integer> dictionary; // Wörterbuch für Kodierung und Dekodierung

    // Konstruktor: Initialisiert Pfade und Variablen
    public LZW(Path filePath, int dictBitSize) {
        this.filePath = filePath; // Setzt den Dateipfad
        Path parent = this.filePath.getParent(); // Holt das übergeordnete Verzeichnis
        if (parent == null) {
            parent = Path.of("."); // Falls kein übergeordnetes Verzeichnis existiert, aktuelles Verzeichnis verwenden
        }
        this.encodedPath = parent.resolve("encoded_" + filePath.getFileName()); // Pfad für codierte Datei
        this.decodedPath = parent.resolve("decoded_" + filePath.getFileName()); // Pfad für dekodierte Datei
        this.dictBitSize = dictBitSize; // Maximale Bitgröße für Wörterbucheinträge
        this.chars = (int) Math.pow(2, CHAR_BIT_SIZE); // Anzahl der Zeichen (256 für ASCII)
    }

    // Kodierungsfunktion
    public void encode() {
        initDictionary(); // Wörterbuch initialisieren

        try (InputStream is = new FileInputStream(filePath.toFile())) { // Eingabestrom öffnen
            try (OutputStream os = new FileOutputStream(encodedPath.toFile())) { // Ausgabestrom für codierte Datei öffnen
                BitOutput bitOut = new DefaultBitOutput(new StreamByteOutput(os)); // Bit-Ausgabe initialisieren

                List<Character> prefix = new ArrayList<>(); // Präfix für Kodierung
                int codeNummer = chars; // Startwert für Codes jenseits des ASCII-Bereichs
                int statStringLength = 0; // Statistik: Gesamtzahl der Zeichen in kodierten Strings
                int statCodes = 0; // Statistik: Gesamtzahl der generierten Codes

                int current; // Aktuelles Zeichen
                while ((current = is.read()) != -1) { // Lesen, bis das Ende der Datei erreicht ist
                    List<Character> extendedPrefix = new ArrayList<>(prefix); // Präfix erweitern
                    extendedPrefix.add((char) current); // Aktuelles Zeichen anhängen

                    if (!dictionary.containsKey(extendedPrefix)) { // Wenn erweiterter Präfix nicht im Wörterbuch
                        dictionary.put(extendedPrefix, codeNummer++); // Eintrag hinzufügen
                        int code = dictionary.get(prefix); // Code des aktuellen Präfixes holen
                        bitOut.writeInt(true, dictBitSize, code); // Code in die Ausgabe schreiben
                        statStringLength += prefix.size(); // Statistik aktualisieren
                        statCodes++;

                        if (DEBUG) { // Debug-Ausgabe
                            System.out.println(String.format("Kodiert: Präfix = %s | Code = %d | Eingefügt: %s mit Code = %d",
                                    prefix, code, extendedPrefix, codeNummer - 1));
                        }

                        if (codeNummer >= (1 << dictBitSize)) { // Wörterbuchgröße überschritten
                            initDictionary(); // Wörterbuch zurücksetzen
                            codeNummer = chars; // Codes neu starten
                        }

                        prefix = new ArrayList<>(); // Präfix zurücksetzen
                        prefix.add((char) current); // Neues Präfix starten
                    } else {
                        prefix = extendedPrefix; // Präfix erweitern
                    }
                }

                if (!prefix.isEmpty()) { // Letztes Präfix verarbeiten
                    int code = dictionary.get(prefix); // Code des Präfixes holen
                    bitOut.writeInt(true, dictBitSize, code); // Code in die Ausgabe schreiben
                    statStringLength += prefix.size(); // Statistik aktualisieren
                    statCodes++;
                }

                bitOut.align(1); // Ausgabe auf Byte-Grenze ausrichten

                System.out.println("Mittlere codierte Stringlänge: " + (1.0 * statStringLength / statCodes));
                System.out.println("Kompressionsrate: " + calculateCompressionRate() + "%");
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // Ausnahmebehandlung
        }
    }

    // Dekodierungsfunktion
    public void decode() {
        initDictionary(); // Wörterbuch initialisieren

        try (InputStream is = new FileInputStream(encodedPath.toFile())) { // Codierten Eingabestrom öffnen
            BitInput bitIn = new DefaultBitInput(new StreamByteInput(is)); // Bit-Eingabe initialisieren
            try (OutputStream os = new FileOutputStream(decodedPath.toFile())) { // Dekodierten Ausgabestrom öffnen

                int codeNummer = chars; // Startwert für neue Codes
                int current = bitIn.readInt(true, dictBitSize); // Ersten Code lesen
                List<Character> prefix = getByNum(current); // Präfix für ersten Code
                if (prefix == null || prefix.isEmpty()) { // Überprüfen, ob gültig
                    throw new IllegalStateException("Ungültiger Startcode: " + current);
                }
                writeString(os, prefix); // Präfix in die Ausgabe schreiben

                while (is.available() > 0) { // Lesen, bis Ende der Datei erreicht
                    current = bitIn.readInt(true, dictBitSize); // Nächsten Code lesen
                    List<Character> entry;

                    if (dictionary.containsValue(current)) { // Wenn Code im Wörterbuch
                        entry = getByNum(current);
                    } else { // Wenn Code nicht im Wörterbuch
                        entry = new ArrayList<>(prefix);
                        entry.add(prefix.get(0));
                    }

                    writeString(os, entry); // Zeichenfolge in die Ausgabe schreiben

                    List<Character> newEntry = new ArrayList<>(prefix); // Neuen Eintrag erstellen
                    newEntry.add(entry.get(0)); // Ersten Buchstaben des neuen Codes hinzufügen
                    dictionary.put(newEntry, codeNummer++); // Wörterbuch aktualisieren

                    if (DEBUG) { // Debug-Ausgabe
                        System.out.println(String.format("Dekodiert: %s | Eingefügt: %s mit Code = %d",
                                entry, newEntry, codeNummer - 1));
                    }

                    if (codeNummer >= (1 << dictBitSize)) { // Wörterbuchgröße überschritten
                        initDictionary(); // Wörterbuch zurücksetzen
                        codeNummer = chars; // Codes neu starten
                    }

                    prefix = entry; // Präfix aktualisieren
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e); // Ausnahmebehandlung
        }
    }

    // Initialisiert das Wörterbuch mit ASCII-Zeichen
    private void initDictionary() {
        dictionary = new HashMap<>(); // Neues Wörterbuch erstellen
        for (int i = 0; i < chars; i++) { // Für jedes Zeichen im ASCII-Bereich
            dictionary.put(List.of((char) i), i); // Eintrag hinzufügen
        }
        if (DEBUG) {
            System.out.println("Wörterbuch initialisiert mit " + chars + " Einträgen.");
        }
    }

    // Hilfsmethode: Holt die Zeichenfolge zu einem Code
    private List<Character> getByNum(int code) {
        for (Map.Entry<List<Character>, Integer> entry : dictionary.entrySet()) {
            if (entry.getValue() == code) {
                return entry.getKey();
            }
        }
        return null; // Code nicht gefunden
    }

    // Schreibt eine Zeichenfolge in einen Ausgabestrom
    private void writeString(OutputStream os, List<Character> charList) throws IOException {
        for (char c : charList) { // Für jedes Zeichen in der Liste
            os.write(c); // Zeichen in den Ausgabestrom schreiben
        }
    }

    // Berechnet die Kompressionsrate
    public double calculateCompressionRate() throws IOException {
        long originalSize = Files.size(filePath); // Größe der Originaldatei
        long compressedSize = Files.size(encodedPath); // Größe der komprimierten Datei
        return (1.0 - (double) compressedSize / originalSize) * 100; // Kompressionsrate berechnen
    }
}
