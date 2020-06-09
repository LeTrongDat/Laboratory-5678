package com.company.server.io.impl;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    public static void logback(String str) {
        DateTimeFormatter dtm = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();

        System.out.print(str + "\n> ");

        try {
            FileWriter fileWriter = new FileWriter("logback.txt", true);

            fileWriter.append(dtm.format(now)).append(": ").append(str).append(String.valueOf('\n'));

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
