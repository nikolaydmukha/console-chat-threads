package ru.home.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private static Log instance;

    private Log() {
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void loggingMessage(String mes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy ");
        File file = getLogFile();
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(LocalDateTime.now().format(formatter) + " " + mes + "\n");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private File getLogFile() {
        AppConfig appConfig = AppConfig.getInstance();
        String path = appConfig.getLog();

        File f = new File(path);
        return f;
    }

}
