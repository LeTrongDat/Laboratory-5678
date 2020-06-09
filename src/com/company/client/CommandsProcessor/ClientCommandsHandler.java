package com.company.client.CommandsProcessor;

import com.company.client.Communication.ClientConsoleInputProcessor;
import com.company.client.Communication.ClientPrinter;
import com.company.shared.entity.CommandData;
import com.company.shared.annotations.Command;
import com.company.shared.exceptions.RecursiveException;
import com.company.shared.exceptions.WrongCommandFormatException;
import com.company.shared.entity.AstartesCategory;
import com.company.shared.entity.MeleeWeapon;
import com.company.shared.entity.SpaceMarine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class contains all handlers of all commands.
 * @Author Le Trong Dat
 */
public class ClientCommandsHandler implements ClientCommandsHandlerManipulation {
    private ClientPrinter resultClientPrinter;
    private ClientPrinter queryClientPrinter;
    private ArrayList<com.company.client.Communication.ClientConsoleInputProcessor> listClientConsoleInputProcessor;
    private com.company.client.Communication.ClientConsoleInputProcessor ClientConsoleInputProcessor;
    private HashSet<String> hashSet;

    public ClientCommandsHandler() {
        hashSet = new HashSet<>();
        listClientConsoleInputProcessor = new ArrayList<>();
    }

    public ClientPrinter getResultClientPrinter() {
        return resultClientPrinter;
    }

    public void setResultClientPrinter(ClientPrinter resultClientPrinter) {
        this.resultClientPrinter = resultClientPrinter;
    }

    public ClientPrinter getQueryClientPrinter() {
        return queryClientPrinter;
    }

    public void setQueryClientPrinter(ClientPrinter queryClientPrinter) {
        this.queryClientPrinter = queryClientPrinter;
    }

    public ArrayList<com.company.client.Communication.ClientConsoleInputProcessor> getListClientConsoleInputProcessor() {
        return listClientConsoleInputProcessor;
    }

    public void setListClientConsoleInputProcessor(ArrayList<com.company.client.Communication.ClientConsoleInputProcessor> listClientConsoleInputProcessor) {
        this.listClientConsoleInputProcessor = listClientConsoleInputProcessor;
    }

    public com.company.client.Communication.ClientConsoleInputProcessor getClientConsoleInputProcessor() {
        return ClientConsoleInputProcessor;
    }

    public void setClientConsoleInputProcessor(com.company.client.Communication.ClientConsoleInputProcessor clientConsoleInputProcessor) {
        ClientConsoleInputProcessor = clientConsoleInputProcessor;
    }

    public HashSet<String> getHashSet() {
        return hashSet;
    }

    public void setHashSet(HashSet<String> hashSet) {
        this.hashSet = hashSet;
    }

    public void addClientConsoleInputProcessor(com.company.client.Communication.ClientConsoleInputProcessor clientConsoleInputProcessor) {
        if (clientConsoleInputProcessor != null) listClientConsoleInputProcessor.add(clientConsoleInputProcessor);
        this.ClientConsoleInputProcessor = listClientConsoleInputProcessor.get(listClientConsoleInputProcessor.size() - 1);
    }


    /**
     * Using annotation to loop through all available commands and compare with the name of present command.
     * @param commandName the name of the command.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public CommandData processCommand(String commandName, Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        try {
            List<Method> methods = Arrays.stream(ClientCommandsHandlerManipulation.class.getDeclaredMethods())
                    .filter(x -> x.getDeclaredAnnotation(Command.class).name().equals(commandName))
                    .filter(x -> x.getDeclaredAnnotation(Command.class).param() == args.length)
                    .collect(Collectors.toList());
            if (methods.isEmpty()) throw new WrongCommandFormatException();
            return (CommandData) methods.get(0).invoke(this, new Object[]{args});
        } catch (WrongCommandFormatException e) {
            resultClientPrinter.print("print","Command does not exist. Please enter again. Type \"help\" to see the list of commands");
            return null;
        }
    }

    /** Print list available commands. */
    public CommandData printListCommand(Object... args) {
        return new CommandData("help", args);
    }

    /** Get information of collection. */
    public CommandData getGeneralInformation(Object... args) {
        return new CommandData("info", args);
    }

    /** Get information of elements in the collection. */
    public CommandData getDetailsInformation(Object... args) {
        return new CommandData("show", args);
    }

    /** Add new object from stdin into the collection. */
    public CommandData addObject(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return new CommandData("add", ClientCommandsHelper.getNewObject(SpaceMarine.class, ClientConsoleInputProcessor, queryClientPrinter));
    }

    /** Change properties of the object whose id is equal to specified. */
    public CommandData update(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        try {
            int id = Integer.parseInt( (String) args[0] );
            return new CommandData("update", id,
                    ClientCommandsHelper.getNewObject(SpaceMarine.class, ClientConsoleInputProcessor, queryClientPrinter));
        } catch (NumberFormatException | NullPointerException e) {
            resultClientPrinter.print("print", "Wrong number format");
            return null;
        }
    }

    /** Remove the object whose id is equal to specified. */
    public CommandData removeById(Object... args) {
        try {
            int id = Integer.parseInt((String)args[0]);
            return new CommandData("remove_by_id", id);
        } catch (NumberFormatException | NullPointerException e) {
            queryClientPrinter.print("print", "Wrong format number");
            return null;
        }
    }

    /** Clear the collection. */
    public CommandData clear(Object... args) {
        return new CommandData("clear", args);
    }

    /** Save the collection to csv file. */
    public CommandData save(Object... args) {
        return new CommandData("save", args);
    }

    /** Execute all commands through file. */
    public CommandData executeFile(Object... args) throws IllegalArgumentException {
        try {
            com.company.client.Communication.ClientConsoleInputProcessor clientConsoleInputProcessor1 = new ClientConsoleInputProcessor();
            clientConsoleInputProcessor1.setQueryClientPrinter(this.queryClientPrinter);
            clientConsoleInputProcessor1.setResultClientPrinter(this.resultClientPrinter);
            clientConsoleInputProcessor1.setHashSet(this.hashSet);
            clientConsoleInputProcessor1.setClientConsoleInputProcessors(this.listClientConsoleInputProcessor);

            clientConsoleInputProcessor1.setFileStream((String)args[0]);

            this.addClientConsoleInputProcessor(clientConsoleInputProcessor1);
            this.hashSet.add((String)args[0]);
            queryClientPrinter.setMode(true);
        } catch (RecursiveException e) {
            resultClientPrinter.print("print", "Cycle found.");
        }
        return null;
    }

    /** Terminate program. */
    public void exitProgram(Object... args) {
        System.out.println("The application will be terminated in few seconds.");
        System.exit(0);
    }

    /** Add new element if it's greater than the maximum element in the collection.*/
    public CommandData addIfMax(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return new CommandData("add_if_max", ClientCommandsHelper.getNewObject(SpaceMarine.class, ClientConsoleInputProcessor, queryClientPrinter));
    }

    /** Add new element if it's smaller than the minimum element in the collection. */
    public CommandData addIfMin(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return new CommandData("add_if_min", ClientCommandsHelper.getNewObject(SpaceMarine.class, ClientConsoleInputProcessor, queryClientPrinter));
    }

    /** Trace the last 6 commands.*/
    public CommandData showHistory(Object... args) {
        return new CommandData("history", args);
    }

    /** Remove object by its category.*/
    public CommandData removeByCategory(Object... args) {
        try {
            AstartesCategory.valueOf( (String) args[0] );
            return new CommandData("remove_any_by_category", args);
        } catch (IllegalArgumentException e) {
            resultClientPrinter.print("print", "Wrong category");
            return null;
        }
    }

    /** Count the number of elements whose category is less then received category.*/
    public CommandData countGreaterThanCategory(Object... args) {
        try {
            AstartesCategory.valueOf( (String) args[0] );
            return new CommandData("count_greater_than_category", args);
        } catch (IllegalArgumentException e) {
            resultClientPrinter.print("print", "Wrong category");
            return null;
        }
    }

    /** Display elements whose melee weapon is greater then the specified.*/
    public CommandData filterGreaterThanMeleeWeapon(Object... args) {
        try {
            MeleeWeapon.valueOf( (String) args[0] );
            return new CommandData("filter_greater_than_melee_weapon", args);
        } catch (IllegalArgumentException e) {
            resultClientPrinter.print("print", "Wrong melee weapon");
            return null;
        }
    }
}
