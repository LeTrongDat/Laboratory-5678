package com.company.server.processor;

import com.company.server.io.Logback;
import com.company.server.io.ServerPrinter;
import com.company.shared.annotations.CommandAnnotation;
import com.company.shared.exceptions.WrongCommandFormatException;
import com.company.server.dao.writer.csv.OpenCSVWriter;
import com.company.shared.objects.*;
import com.company.shared.CommandData;

import java.io.*;
import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class contains all handlers of all commands.
 * @Author Le Trong Dat
 */
public class ServerCommandsHandler implements ServerCommandsHandlerManipulation{
    /**
     * @field pq: A collection that contains all objecs.
     * @field sc: Scanner for reading input from stdin.
     * @field listCommands: Use to track previous commands (Command "history").
     */
    private PriorityQueue<SpaceMarine> pq;
    private ArrayList<String> listCommands;
    private DateTimeFormatter dtf;
    private java.time.ZonedDateTime initializationDate;
    private ServerPrinter resultServerPrinter;

    public ServerCommandsHandler() {
        this.pq = new PriorityQueue<>();
        this.listCommands = new ArrayList<>();
        this.dtf = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy");
        this.initializationDate = LocalDateTime.now().atZone(ZoneId.of("UTC+7"));
    }
    public void setResultServerPrinter(ServerPrinter resultServerPrinter) {
        this.resultServerPrinter = resultServerPrinter;
    }

    /**
     * Using annotation to loop through all available commands and compare with the name of present command.
     * @param commandData the name of the command.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public synchronized void processCommand(CommandData commandData) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        try {
            List<Method> methods = Arrays.stream(ServerCommandsHandlerManipulation.class.getDeclaredMethods())
                    .filter(x -> x.getDeclaredAnnotation(CommandAnnotation.class).name().equals(commandData.getCommandName()))
                    .filter(x -> x.getDeclaredAnnotation(CommandAnnotation.class).param() == commandData.getCommandArguments().length)
                    .collect(Collectors.toList());
            if (methods.size() == 0) throw new WrongCommandFormatException();
            Logback.logback("Processing command " + commandData.getCommandName() + "...");
            methods.get(0).invoke(this, new Object[]{commandData.getCommandArguments()});
            listCommands.add(commandData.getCommandName());
            if (listCommands.size() > 6) listCommands.remove(0);
        } catch (WrongCommandFormatException e) {
            resultServerPrinter.print("print","Command does not exist. Please enter again. Type \"help\" to see the list of commands");
        }
    }

    /**
     * Print list available commands.
     */
    public void printListCommand(Object... args) {
        resultServerPrinter.print("rawPrint",
                Arrays.stream(ServerCommandsHandlerManipulation.class.getDeclaredMethods())
                .map(x -> x.getDeclaredAnnotation(CommandAnnotation.class))
                .map(x -> String.format("%-35s - %-50s\n", x.name(), x.usage()) + "> ")
                .reduce("", (a, b) -> a + b));
    }

    /**
     * Get information of collection.
     */
    public void getGeneralInformation(Object... args) {
        resultServerPrinter.print("print", "Type of collection: Priority Queue");
        resultServerPrinter.print("print", "Size of collection: " + pq.size());
        resultServerPrinter.print("print", "Initialization date: " + dtf.format(initializationDate));
    }

    /**
     * Get information of elements in the collection.
     */
    public void getDetailsInformation(Object... args) {
        resultServerPrinter.print("print", pq.toArray());
    }

    /**
     * Add new object into the collection.
     * @param sm new object
     */
    public void add(SpaceMarine sm) {
        pq.add(sm);
    }

    /**
     * Add new object from stdin into the collection.
     */
    public void addObject(Object... args) {
        SpaceMarine spaceMarine = (SpaceMarine)args[0];
        spaceMarine.setId(pq.size() + 1);                                   // need fix
        pq.add(spaceMarine);
        resultServerPrinter.print("print", "New Space Marine has been added");
    }

    /**
     * Change properties of the object whose id is equal to specified.
     */
    public void update(Object... args) {
        if (pq.isEmpty()) {
            resultServerPrinter.print("print", "The collection is empty.");
            return;
        }
        int id = (Integer)args[0];
        if (id < 0 || id > pq.size()) {
            resultServerPrinter.print("print", "The number of id should be in range (1, " + pq.size() + ")");
            return;
        }
        SpaceMarine sm = (SpaceMarine)args[1];
        sm.setId(id);
        pq.removeIf(x -> x.getId().equals(id));
        pq.add(sm);
        resultServerPrinter.print("print", "Updated collection.");
    }

    /**
     * Remove the object whose id is equal to specified.
     */
    public void removeById(Object... args) {
        pq.removeIf(sm -> sm.getId().equals((Integer)args[0]));
        resultServerPrinter.print("print", "Removed element.");
    }

    /**
     * Clear the collection.
     */
    public void clear(Object... args) {
        pq.clear();
        resultServerPrinter.print("print", "Cleared collection");
    }

    /**
     * Save the collection to csv file.
     * @throws IOException
     */
    public void save(Object... args) throws IOException {
        OpenCSVWriter.printOutput(pq);
        resultServerPrinter.print("print", "Saved collection to file.");
    }

    /**
     * Execute all commands through file.
     * @throws IllegalArgumentException
     */
    public void executeFile(Object... args) throws IllegalArgumentException { }

    /**
     * Terminate program.
     */
    public void exitProgram(Object... args) {
        System.exit(0);
    }

    /**
     * Add new element if it's greater than the maximum element in the collection.
     */
    public void addIfMax(Object... args) throws NullPointerException {
        SpaceMarine sm = (SpaceMarine)args[0];
        Object[] spaceMarines = pq.toArray();
        if (pq.isEmpty() || sm.compareTo((SpaceMarine)spaceMarines[spaceMarines.length - 1]) < 0) {
            pq.add(sm);
            resultServerPrinter.print("print", "New object was added into the collection");
        } else {
            resultServerPrinter.print("print", "New object is not greater than the current maximum object in the collection");
        }
    }

    /**
     * Add new element if it's smaller than the minimum element in the collection.
     */
    public void addIfMin(Object... args) throws NullPointerException {
        SpaceMarine sm = (SpaceMarine)args[0];
        if (pq.isEmpty() || sm.compareTo(pq.peek()) > 0) {
            pq.add(sm);
            resultServerPrinter.print("print", "New object was added into the collection");
        } else {
            resultServerPrinter.print("print", "New object is not less then the current minimum object in the collection");
        }
    }

    /**
     * Trace the last 6 commands.
     */
    public void showHistory(Object... args) {
        resultServerPrinter.print("print", listCommands.toArray());
    }

    /**
     * Remove object by its category.
     */
    public void removeByCategory(Object... args) {
        pq.removeIf(x -> x.getCategory().equals(AstartesCategory.valueOf((String)args[0])));
        resultServerPrinter.print("print", "Removed items whose category field value is equal to the specified");
    }

    /**
     * Count the number of elements whose category is greater than received category.
     */
    public void countGreaterThanCategory(Object... args) {
        resultServerPrinter.print("print", "The number of elements is: " + pq.stream()
                        .filter(x -> x.getCategory() != null)
                        .filter(x -> x.getCategory().ordinal() > AstartesCategory.valueOf((String) args[0]).ordinal())
                        .count());
    }

    /**
     * Display elements whose melee weapon is greater than the specified.
     */
    public void filterGreaterThanMeleeWeapon(Object... args) {
        resultServerPrinter.print("print", pq.stream()
                        .filter(x -> x.getMeleeWeapon() != null)
                        .filter(x -> x.getMeleeWeapon().ordinal() > MeleeWeapon.valueOf((String) args[0]).ordinal())
                        .map(x -> x.toString())
                        .reduce("", (a, b) -> a + b));
    }
}
