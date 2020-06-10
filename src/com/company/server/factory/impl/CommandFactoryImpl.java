package com.company.server.factory.impl;

import com.company.server.controller.UserController;
import com.company.server.dao.repo.SpaceMarineRepository;
import com.company.server.dao.repo.impl.SpaceMarineRepositoryImpl;
import com.company.server.factory.CommandFactory;
import com.company.server.io.impl.Log;
import com.company.server.io.Collector;
import com.company.shared.entity.User;
import com.company.shared.annotations.Command;
import com.company.shared.exceptions.UnauthorizedUserException;
import com.company.shared.exceptions.WrongCommandFormatException;
import com.company.shared.entity.*;
import com.company.shared.entity.CommandData;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class contains all handlers of all commands.
 * @Author Le Trong Dat
 */
public class CommandFactoryImpl implements CommandFactory {

    private PriorityQueue<SpaceMarine> spaceMarines;

    private List<String> commandLog;

    private Collector<String> responder;

    private Boolean authorized;

    private SpaceMarineRepository repo;

    public CommandFactoryImpl() {
        init();
    }

    public void setResponder(Collector<String> responder) {
        this.responder = responder;
    }

    @Override
    public void processCommand(@NotNull CommandData commandData) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        try {
            Optional<Method> opt = getMethod(commandData.getCommandName(), commandData.getCommandArguments().length);

            if (!opt.isPresent()) throw new WrongCommandFormatException();

            if (!commandData.getCommandName().equals("sign_up") && !commandData.getCommandName().equals("log_in") && !authorized)
                throw new UnauthorizedUserException();

            Log.logback("Processing command " + commandData.getCommandName() + "...");

            this.commandLog.add(commandData.getCommandName());

            if (this.commandLog.size() > 6) this.commandLog.remove(0);

            opt.get().invoke(this, new Object[]{commandData.getCommandArguments()});
        } catch (WrongCommandFormatException e) {
            this.responder.collect("Command does not exist. Please enter again. Type \"help\" to see the list of commands");

            e.printStackTrace();
        } catch (UnauthorizedUserException e) {
            this.responder.collect("The user is unauthorized. Please log in (by command log_in) or sign up (by command sign_up) new user.");

            e.printStackTrace();
        }
    }


    @Command(name = "help", usage = "Display available commands")
    @Override
    public void printListCommand(Object... args) {
        Arrays.stream(CommandFactoryImpl.class.getDeclaredMethods())
                .map(x -> x.getDeclaredAnnotation(Command.class))
                .filter(Objects::nonNull)
                .map(x -> String.format("%-35s - %-50s", x.name(), x.usage()))
                .forEach(x -> this.responder.collect(x));
    }

    @Command(name = "info", usage = "Display information about the collection (type, initialization date, number of elements, etc.) " +
            "in the standard output stream")
    @Override
    public void getGeneralInformation(Object... args) {
        this.responder.collect( "Type of collection: Priority Queue");
        this.responder.collect( "Size of collection: " + spaceMarines.size());
    }

    @Command(name = "show", usage = "Output to the standard output stream all the elements of the collection " +
            "in a string representation")
    @Override
    public void getDetailsInformation(Object... args) {
        this.responder.collect(Arrays.toString(spaceMarines.toArray()));
    }

    @Command(name = "helper_method", usage = "Load object from database to collection.")
    @Override
    public void add(SpaceMarine sm) {
        this.spaceMarines.add(sm);
    }

    @Command(name = "add", usage = "Add new object to collection", param = 1)
    @Override
    public void addObject(Object... args) {
        SpaceMarine spaceMarine = (SpaceMarine)args[0];

        try {
            spaceMarine.setId(repo.add(spaceMarine));
        } catch (SQLException e) {
            this.repo.handleSQLException(e);
        }

        this.spaceMarines.add(spaceMarine);

        this.responder.collect( "New Space Marine has been added");
    }

    @Command(name = "update", usage = "Update the value of a collection element whose id is equal to the specified", param = 2)
    @Override
    public void update(Object... args) {
        int id = (Integer)args[0];

        SpaceMarine sm = (SpaceMarine)args[1];

        sm.setId(id);

        this.spaceMarines.removeIf(x -> x.getId().equals(id));

        try {
            int success = repo.update(sm);

            if (success == -1) {
                this.responder.collect("There is no id satisfying. " +
                        "Please use the command \"show\" to see the id of objects that you have.");
            } else {
                this.spaceMarines.add(sm);
                this.responder.collect( "Updated collection.");
            }
        } catch (SQLException e) {
            this.repo.handleSQLException(e);
        }
    }

    @Command(name = "remove_by_id", usage = "Remove an item from the collection by its id", param = 1)
    @Override
    public void removeById(Object... args) {
        try {
            this.repo.removeById((Integer) args[0]);
        } catch (SQLException e) {
            repo.handleSQLException(e);

            this.responder.collect("There is no id satisfying. " +
                    "Please use the command \"show\" to see the id of objects that you have.");

            return;
        }

        this.spaceMarines.removeIf(sm -> sm.getId().equals(args[0]));

        this.responder.collect( "Removed element.");
    }

    @Command(name = "clear", usage = "Clear collection")
    @Override
    public void clear(Object... args) {
        this.spaceMarines.clear();

        this.responder.collect( "Cleared collection");
    }

    @Command(name = "save", usage = "Save collection to file")
    @Override
    public void save(Object... args) {
        this.responder.collect( "Saved collection to file.");
    }

    @Command(name = "execute_script", usage = "Read and execute the script from the specified file." +
            "The script contains commands in the same form in which they are entered by the user interactively.")
    @Override
    public void executeFile(Object... args) throws IllegalArgumentException { }

    @Command(name = "exit", usage = "Terminate the program (without saving to a file)")
    @Override
    public void exitProgram(Object... args) {
        System.exit(0);
    }

    @Command(name = "add_if_max", usage = "Add a new element to the collection if its value exceeds " +
            "the value of the largest element in this collection", param = 1)
    @Override
    public void addIfMax(Object... args) throws NullPointerException {
        SpaceMarine sm = (SpaceMarine)args[0];

        Object[] spaceMarines = this.spaceMarines.toArray();

        if (this.spaceMarines.isEmpty() || sm.compareTo((SpaceMarine)spaceMarines[spaceMarines.length - 1]) < 0) {

            this.spaceMarines.add(sm);

            this.responder.collect( "New object was added into the collection");
        } else {
            this.responder.collect( "New object is not greater than the current maximum object in the collection");
        }
    }

    @Command(name = "add_if_min", usage = "Add a new element to the collection if its value is less " +
            "than that of the smallest element in this collection", param = 1)
    @Override
    public void addIfMin(Object... args) throws NullPointerException {
        SpaceMarine sm = (SpaceMarine)args[0];
        if (this.spaceMarines.isEmpty() || sm.compareTo(this.spaceMarines.peek()) > 0) {

            this.spaceMarines.add(sm);

            this.responder.collect( "New object was added into the collection");
        } else {
            this.responder.collect( "New object is not less then the current minimum object in the collection");
        }
    }

    @Command(name = "history", usage = "Print the last 6 commands (without their arguments)")
    @Override
    public void showHistory(Object... args) {
        this.responder.collect(Arrays.toString(commandLog.toArray()));
    }

    @Command(name = "remove_any_by_category", usage = "Remove one item from the collection whose " +
            "category field value is equivalent to the specified", param = 1)
    @Override
    public void removeByCategory(Object... args) {
        this.spaceMarines.removeIf(x -> x.getCategory().equals(AstartesCategory.valueOf((String)args[0])));

        this.responder.collect( "Removed items whose category field value is equal to the specified");
    }

    @Command(name = "count_greater_than_category", usage = "Display the number of elements whose category " +
            "field value is greater than the specified", param = 1)
    @Override
    public void countGreaterThanCategory(Object... args) {
        this.responder.collect( "The number of elements is: " + spaceMarines.stream()
                        .filter(x -> x.getCategory() != null
                                && x.getCategory().ordinal() > AstartesCategory.valueOf((String) args[0]).ordinal())
                        .count());
    }

    @Command(name = "filter_greater_than_melee_weapon", usage = "Display elements whose meleeWeapon " +
            "field value is greater than the specified", param = 1)
    @Override
    public void filterGreaterThanMeleeWeapon(Object... args) {
        String meleeWeapon = (String) args[0];

        this.responder.collect(spaceMarines.stream()
                        .filter(x -> x.getMeleeWeapon() != null
                                && x.getMeleeWeapon().ordinal() > MeleeWeapon.valueOf(meleeWeapon).ordinal())
                        .map(SpaceMarine::toString)
                        .reduce("", (a, b) -> a + b));
    }

    @Command(name = "sign_up", usage = "Sign up a new account", param = 1)
    @Override
    public void signUp(Object... args) {
        User user = (User) args[0];

        try {
            this.responder.collect(UserController.signUp(user)
                    ? "Sign up success"
                    : "The username has been taken. Please sign up by another name.");
        } catch (SQLException e) { repo.handleSQLException(e); }
    }

    @Command(name = "log_in", usage = "Log in to the database", param = 1)
    @Override
    public void logIn(Object... args) {
        User user = (User) args[0];

        try {
            this.responder.collect(UserController.logIn(user)
                    ? "Logged in successfully."
                    : "The username or password is incorrect.");

            this.authorized = true;

            this.repo.register(user);

            this.repo.loadDatabase(this);
        } catch (SQLException e) { this.repo.handleSQLException(e); }
    }

    @Command(name = "log_out", usage = "Log out of the database")
    @Override
    public void logOut(Object... args) {
        save(args);

        init();

        this.responder.collect("The user has logged out of the server.");
    }


    // ------------------------ private method --------------------
    private void init() {
        this.spaceMarines = new PriorityQueue<>();

        this.commandLog = new ArrayList<>();

        this.authorized = false;

        Connection con = null;
        try {
            con = DriverManager.getConnection(
                    System.getenv("url"),
                    System.getenv("host"),
                    System.getenv("password"));
        } catch (SQLException e) { e.printStackTrace(); }

        this.repo = new SpaceMarineRepositoryImpl(con);
    }

    private Optional<Method> getMethod(String methodName, int argsLength) {
        List<Method> methods = Arrays.stream(CommandFactoryImpl.class.getDeclaredMethods())
                .filter(x -> {
                    Command ano = x.getDeclaredAnnotation(Command.class);
                    return ano != null && ano.name().equals(methodName) && ano.param() == argsLength; })
                .collect(Collectors.toList());

        return methods.size() == 0
                ? Optional.empty()
                : Optional.of(methods.get(0));
    }
}
