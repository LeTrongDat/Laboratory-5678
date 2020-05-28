package com.company.server.dao.writer.csv;

import com.company.shared.entity.SpaceMarine;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

/**
 * Third-party library for parsing CSV file.
 * This class only for writing data to file under format CSV.
 * @author Le Trong Dat
 */
public class OpenCSVWriter {
    private OpenCSVWriter(){};
    public static void printOutput(PriorityQueue<SpaceMarine> pq) throws IOException {
        try {
            String csvFile = System.getenv("CSVFile");
            OutputStream outputStream = Files.newOutputStream(Paths.get(csvFile));
            PrintWriter writer = new PrintWriter(outputStream);
            String[] headerRecord = {"Name", "Coordinates", "Health", "Astartes Category", "Weapon", "Melee weapon", "Chapter"};
            writer.println(String.join(", ", headerRecord));
            //writer.write("\n");
            for (SpaceMarine sm : pq) {
                writer.println(sm.csv());
                //writer.write("\n");
            }
            writer.close();
        } catch (FileSystemException e) {
            System.out.print("Write permission denied. \n> ");
        }
    }
}
