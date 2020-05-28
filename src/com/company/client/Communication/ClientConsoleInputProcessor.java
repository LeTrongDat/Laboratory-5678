package com.company.client.Communication;

import com.company.shared.exceptions.InvalidValueException;
import com.company.shared.exceptions.RecursiveException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class ClientConsoleInputProcessor {
    private Scanner stdInputStream;
    private Scanner fileStream;
    private String nameOfFileStream;
    private Scanner currentStream;
    private ClientPrinter queryClientPrinter;
    private ClientPrinter resultClientPrinter;
    private HashSet<String> hashSet;
    private ArrayList<ClientConsoleInputProcessor> clientConsoleInputProcessors;

    public ClientConsoleInputProcessor() {
        stdInputStream = new Scanner(System.in);
        setCurrentStream(0);
    }
    public String getNameOfFileStream() {
        return nameOfFileStream;
    }
    public void setQueryClientPrinter(ClientPrinter queryClientPrinter) {
        this.queryClientPrinter = queryClientPrinter;
    }
    public void setResultClientPrinter(ClientPrinter resultClientPrinter) {
        this.resultClientPrinter = resultClientPrinter;
    }
    public void setCurrentStream(int id) {
        switch (id) {
            case 0:
                currentStream = stdInputStream;
                break;
            default: currentStream = fileStream;
        }
    }
    public void setHashSet(HashSet hashSet) {
        this.hashSet = hashSet;
    }
    public void setClientConsoleInputProcessors(ArrayList<ClientConsoleInputProcessor> clientConsoleInputProcessors) {
        this.clientConsoleInputProcessors = clientConsoleInputProcessors;
    }
    public void setFileStream(String fileName) throws RecursiveException {
        try {
            if (this.hashSet.contains(fileName)) throw new RecursiveException();
            InputStream inputStream = Files.newInputStream(Paths.get(fileName));
            fileStream = new Scanner(inputStream);
            nameOfFileStream = fileName;
            setCurrentStream(1);
        } catch (FileSystemException e) {
            queryClientPrinter.print("print", "Read permission denied.");
        } catch (IOException e) {
            queryClientPrinter.print("print", "File not found. Please enter your file name again");
        }
    }

    public String getString(Integer type) {
        while (currentStream.hasNextLine()) {
            String str = currentStream.nextLine().trim();
            queryClientPrinter.print("rawPrint", "> ");
            if (!type.equals(0)) return str;
            if (str.equals("")) {
                queryClientPrinter.print("print", "You should enter field or command (type \"help\" to see all commands)");
                continue;
            }
            return str;
        }
        this.hashSet.remove(nameOfFileStream);
        this.clientConsoleInputProcessors.remove(clientConsoleInputProcessors.size() -1);
        resultClientPrinter.print("print","Executed script in the file " + this.nameOfFileStream);
        if (clientConsoleInputProcessors.size() == 1) setCurrentStream(0);
        return null;
    }

    public int getInt(double min_value, double max_value) {
        while (true) {
            try {
                int res = Integer.parseInt(getString(0));
                if (res < min_value || res > max_value) throw new InvalidValueException();
                return res;
            } catch (NumberFormatException e) {
                queryClientPrinter.print("print","This parameter should be a number");
            } catch (InvalidValueException e) {
                queryClientPrinter.print("print", "The number should be in the range (" + min_value + ", " + max_value + ")");
            }
        }
    }

    @Nullable
    public <E extends Enum<E>> Object getEnum(Class<E> enumClass) {
        while (true) {
            try {
                String str = getString(1);
                if (str.equals("")) return null;
                return Enum.valueOf(enumClass, str);
            } catch (IllegalArgumentException e) {
                queryClientPrinter.print("print", "This parameter should be one of the enum members or empty string (equivalent NULL)");
            }
        }
    }
}
