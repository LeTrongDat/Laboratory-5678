package com.company.server.dao.reader.csv;

import java.io.*;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.company.shared.objects.*;
import com.company.server.processor.ServerCommandsHandler;

/**
 * Third-party library for parsing CSV file.
 * This class only for reading CSV file.
 * @see <a href = "http://opencsv.sourceforge.net/"></a>
 * @author Le Trong Dat
 */
public class  OpenCSVReader {
    private OpenCSVReader(){};
    public static void getInput(ServerCommandsHandler serverCommandsHandler) {
        try {
            String csvFile = System.getenv("CSVFile");
            InputStream inputStream = Files.newInputStream(Paths.get(csvFile));
            Scanner sc = new Scanner(inputStream);
            String strings = sc.nextLine();
            while (sc.hasNextLine()) {
                strings = sc.nextLine();
                String[] line = strings.replaceAll("[\\(\\)\"\" \\s]", "").split(",");
                SpaceMarine spaceMarine = new SpaceMarine(
                        line[0],
                        new Coordinates(Integer.parseInt(line[1]), Integer.parseInt(line[2])),
                        Integer.parseInt(line[3]),
                        line[4].equals("null") ? null : AstartesCategory.valueOf(line[4]),
                        line[5].equals("null") ? null : Weapon.valueOf(line[5]),
                        line[6].equals("null") ? null : MeleeWeapon.valueOf(line[6]),
                        new Chapter(line[7], Long.parseLong(line[8]))
                );
                serverCommandsHandler.add(spaceMarine);
            }
        } catch (FileSystemException e) {
            System.out.println("> Read permission denied.");
        } catch (IOException e) {
            System.out.println("> Data file does not exist.");
        }
    }
}
