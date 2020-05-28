package com.company.server.io;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logback {
    private static FileWriter fileWriter;


    public synchronized static void logback(String str) {
        DateTimeFormatter dtm = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.print(str + "\n> ");
        try {
            fileWriter = new FileWriter("logback.txt", true);
            fileWriter.append(dtm.format(now) + ": " + str + '\n');
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
