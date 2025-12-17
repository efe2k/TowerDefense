package com.td.engine.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LoggerService {

    private static final Path LOG_PATH = Paths.get("savunma_gunlugu.txt");
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public synchronized void log(String msg) {
        String line = "[" + LocalTime.now().format(FMT) + "] " + msg + System.lineSeparator();
        try {
            Files.writeString(
                    LOG_PATH,
                    line,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // İstersen konsola da bas:
        System.out.print(line);
    }
}
