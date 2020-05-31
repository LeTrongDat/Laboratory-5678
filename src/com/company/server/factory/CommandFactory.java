package com.company.server.factory;

import com.company.shared.annotations.CommandAnnotation;
import com.company.shared.entity.SpaceMarine;

import java.io.IOException;

public interface CommandFactory {
    @CommandAnnotation(name = "help", usage = "Display available commands")
    void printListCommand(Object... args);

    @CommandAnnotation(name = "info", usage = "Display information about the collection (type, initialization date, number of elements, etc.) " +
            "in the standard output stream")
    void getGeneralInformation(Object... args);

    @CommandAnnotation(name = "show", usage = "Output to the standard output stream all the elements of the collection " +
            "in a string representation")
    void getDetailsInformation(Object... args);

    @CommandAnnotation(name = "add", usage = "Add new object to collection", param = 1)
    void addObject(Object... args) throws NullPointerException;

    @CommandAnnotation(name = "update", usage = "Update the value of a collection element whose id is equal to the specified", param = 2)
    void update(Object... args) throws NullPointerException;

    @CommandAnnotation(name = "remove_by_id", usage = "Remove an item from the collection by its id", param = 1)
    void removeById(Object... args);

    @CommandAnnotation(name = "clear", usage = "Clear collection")
    void clear(Object... args);

    @CommandAnnotation(name = "save", usage = "Save collection to file")
    void save(Object... args) throws IOException;

    @CommandAnnotation(name = "execute_script", usage = "Read and execute the script from the specified file." +
            "The script contains commands in the same form in which they are entered by the user interactively.")
    void executeFile(Object... args);

    @CommandAnnotation(name = "add_if_max", usage = "Add a new element to the collection if its value exceeds " +
            "the value of the largest element in this collection", param = 1)
    void addIfMax(Object... args) throws NullPointerException;

    @CommandAnnotation(name = "add_if_min", usage = "Add a new element to the collection if its value is less " +
            "than that of the smallest element in this collection", param = 1)
    void addIfMin(Object... args) throws NullPointerException;

    @CommandAnnotation(name = "history", usage = "Print the last 6 commands (without their arguments)")
    void showHistory(Object... args);

    @CommandAnnotation(name = "remove_any_by_category", usage = "Remove one item from the collection whose " +
            "category field value is equivalent to the specified", param = 1)
    void removeByCategory(Object... args);

    @CommandAnnotation(name = "count_greater_than_category", usage = "Display the number of elements whose category " +
            "field value is greater than the specified", param = 1)
    void countGreaterThanCategory(Object... args);

    @CommandAnnotation(name = "filter_greater_than_melee_weapon", usage = "Display elements whose meleeWeapon " +
            "field value is greater than the specified", param = 1)
    void filterGreaterThanMeleeWeapon(Object... args);

    @CommandAnnotation(name = "exit", usage = "Terminate the program (without saving to a file)")
    void exitProgram(Object... args);

    @CommandAnnotation(name = "sign_up", usage = "Sign up a new account", param = 1)
    void signUp(Object... args);

    @CommandAnnotation(name = "log_in", usage = "Log in to the database", param = 1)
    void logIn(Object... args);

    @CommandAnnotation(name = "log_out", usage = "Log out of the database")
    void logOut(Object... args);

    @CommandAnnotation(name = "helper_method", usage = "add object from database to collection")
    void add(SpaceMarine sm);
}
