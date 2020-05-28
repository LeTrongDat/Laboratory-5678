package com.company.server.processor;

import com.company.server.user.UserManagement;
import com.company.server.io.Logback;
import com.company.server.io.Collector;
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
    private List<String> listCommands;
    private DateTimeFormatter dtf;
    private java.time.ZonedDateTime initializationDate;
    private Collector<String> messageCollector;
    private UserManagement userManagement; //
    private boolean status; //

    public boolean getStatus() {
        return status;
    }

    public ServerCommandsHandler() {
        this.pq = new PriorityQueue<>();
        this.listCommands = new ArrayList<>();
        this.dtf = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy");
        this.initializationDate = LocalDateTime.now().atZone(ZoneId.of("UTC+7"));
        this.status = false;
    }
    public void setMessageCollector(Collector<String> messageCollector) {
        this.messageCollector = messageCollector;
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
            messageCollector.collect("Command does not exist. Please enter again. Type \"help\" to see the list of commands");
        }
    }

    /**
     * collect list available commands.
     */
    @Override
    public void printListCommand(Object... args) {
        Arrays.stream(ServerCommandsHandlerManipulation.class.getDeclaredMethods())
                .map(x -> x.getDeclaredAnnotation(CommandAnnotation.class))
                .map(x -> String.format("%-35s - %-50s\n", x.name(), x.usage()))
                .forEach(x -> messageCollector.collect(x));
    }

    /**
     * Get information of collection.
     */
    @Override
    public void getGeneralInformation(Object... args) {
        messageCollector.collect( "Type of collection: Priority Queue");
        messageCollector.collect( "Size of collection: " + pq.size());
        messageCollector.collect( "Initialization date: " + dtf.format(initializationDate));
    }

    /**
     * Get information of elements in the collection.
     */
    @Override
    public void getDetailsInformation(Object... args) {
        messageCollector.collect(Arrays.toString(pq.toArray()));
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
    @Override
    public void addObject(Object... args) {
        SpaceMarine spaceMarine = (SpaceMarine)args[0];
        spaceMarine.setId(pq.size() + 1);                                   // need fix
        pq.add(spaceMarine);
        messageCollector.collect( "New Space Marine has been added");
    }

    /**
     * Change properties of the object whose id is equal to specified.
     */
    @Override
    public void update(Object... args) {
        if (pq.isEmpty()) {
            messageCollector.collect( "The collection is empty.");
            return;
        }
        int id = (Integer)args[0];
        if (id < 0 || id > pq.size()) {
            messageCollector.collect( "The number of id should be in range (1, " + pq.size() + ")");
            return;
        }
        SpaceMarine sm = (SpaceMarine)args[1];
        sm.setId(id);
        pq.removeIf(x -> x.getId().equals(id));
        pq.add(sm);
        messageCollector.collect( "Updated collection.");
    }

    /**
     * Remove the object whose id is equal to specified.
     */
    @Override
    public void removeById(Object... args) {
        pq.removeIf(sm -> sm.getId().equals((Integer)args[0]));
        messageCollector.collect( "Removed element.");
    }

    /**
     * Clear the collection.
     */
    @Override
    public void clear(Object... args) {
        pq.clear();
        messageCollector.collect( "Cleared collection");
    }

    /**
     * Save the collection to csv file.
     * @throws IOException
     */
    @Override
    public void save(Object... args) throws IOException {
        OpenCSVWriter.printOutput(pq);
        messageCollector.collect( "Saved collection to file.");
    }

    /**
     * Execute all commands through file.
     * @throws IllegalArgumentException
     */
    @Override
    public void executeFile(Object... args) throws IllegalArgumentException { }

    /**
     * Terminate program.
     */
    @Override
    public void exitProgram(Object... args) {
        System.exit(0);
    }

    /**
     * Add new element if it's greater than the maximum element in the collection.
     */
    @Override
    public void addIfMax(Object... args) throws NullPointerException {
        SpaceMarine sm = (SpaceMarine)args[0];
        Object[] spaceMarines = pq.toArray();
        if (pq.isEmpty() || sm.compareTo((SpaceMarine)spaceMarines[spaceMarines.length - 1]) < 0) {
            pq.add(sm);
            messageCollector.collect( "New object was added into the collection");
        } else {
            messageCollector.collect( "New object is not greater than the current maximum object in the collection");
        }
    }

    /**
     * Add new element if it's smaller than the minimum element in the collection.
     */
    @Override
    public void addIfMin(Object... args) throws NullPointerException {
        SpaceMarine sm = (SpaceMarine)args[0];
        if (pq.isEmpty() || sm.compareTo(pq.peek()) > 0) {
            pq.add(sm);
            messageCollector.collect( "New object was added into the collection");
        } else {
            messageCollector.collect( "New object is not less then the current minimum object in the collection");
        }
    }

    /**
     * Trace the last 6 commands.
     */
    @Override
    public void showHistory(Object... args) {
        messageCollector.collect(Arrays.toString(listCommands.toArray()));
    }

    /**
     * Remove object by its category.
     */
    @Override
    public void removeByCategory(Object... args) {
        pq.removeIf(x -> x.getCategory().equals(AstartesCategory.valueOf((String)args[0])));
        messageCollector.collect( "Removed items whose category field value is equal to the specified");
    }

    /**
     * Count the number of elements whose category is greater than received category.
     */
    @Override
    public void countGreaterThanCategory(Object... args) {
        messageCollector.collect( "The number of elements is: " + pq.stream()
                        .filter(x -> x.getCategory() != null)
                        .filter(x -> x.getCategory().ordinal() > AstartesCategory.valueOf((String) args[0]).ordinal())
                        .count());
    }

    /**
     * Display elements whose melee weapon is greater than the specified.
     */
    @Override
    public void filterGreaterThanMeleeWeapon(Object... args) {
        messageCollector.collect( pq.stream()
                        .filter(x -> x.getMeleeWeapon() != null)
                        .filter(x -> x.getMeleeWeapon().ordinal() > MeleeWeapon.valueOf((String) args[0]).ordinal())
                        .map(x -> x.toString())
                        .reduce("", (a, b) -> a + b));
    }

    /**
     * Sign up new account
     */
    @Override
    public void signUp(Object... args) {
    }

    /**
     * Log into the database.
     */
    @Override
    public void logIn(Object... args) {
    }
}
