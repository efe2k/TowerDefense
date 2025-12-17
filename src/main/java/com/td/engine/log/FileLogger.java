package com.td.engine.log;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {

    private static final String FILE = "savunma_gunlugu.txt";
    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("HH:mm:ss");

    // Dosyanın başına senaryo bilgisi
    public static void header(String scenario, int lives, int money) {
        try (FileWriter fw = new FileWriter(FILE, false)) {
            fw.write("Simülasyon Başladı. Senaryo: '" + scenario + "'\n");
            fw.write("Başlangıç Can: " + lives + ", Para: " + money + ".\n");
            fw.write("------------------------------------------------------\n");
        } catch (IOException e) {
            System.err.println("LOG başlık yazılamadı: " + e.getMessage());
        }
    }

    // Satır satır ekleme
    public static void log(String msg) {
        try (FileWriter fw = new FileWriter(FILE, true)) {
            fw.write("[" + sdf.format(new Date()) + "] " + msg + "\n");
        } catch (IOException e) {
            System.err.println("LOG yazılamadı: " + e.getMessage());
        }
    }
}
