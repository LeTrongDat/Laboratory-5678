package com.company.server.factory;

import com.company.shared.entity.CommandData;
import com.company.shared.entity.SpaceMarine;

import java.lang.reflect.InvocationTargetException;

public interface CommandFactory {
    /**
     * Using annotation to loop through all available commands and compare with the name of present command.
     * @param commandData the name of the command.
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    void processCommand(CommandData commandData) throws IllegalAccessException, InvocationTargetException;

    /**
     * Display all available commands.
     * @param args
     */
    void printListCommand(Object... args);

    /**
     * Display information about the collection (type, initialization date, number of elements, etc.)
     * in the standard output stream.
     * @param args
     */
    void getGeneralInformation(Object... args);

    /**
     * Output to the standard output stream all the elements of the collection
     * in a string representation.
     * @param args
     */
    void getDetailsInformation(Object... args);

    /**
     * Add new object to collection.
     * @param args
     * @throws NullPointerException
     */
    void addObject(Object... args) throws NullPointerException;

    /**
     * Update the value of a collection element whose id is equal to the specified.
     * @param args
     * @throws NullPointerException
     */
    void update(Object... args) throws NullPointerException;

    /**
     * Remove an item from the collection by its id.
     * @param args
     */
    void removeById(Object... args);

    /**
     * Clear collection.
     * @param args
     */
    void clear(Object... args);

    /**
     * Save collection into database.
     * @param args
     */
    void save(Object... args) ;

    /**
     * Read and execute the script from the specified file.
     * The script contains commands in the same form in which they are entered by the user interactively.
     * @param args
     */
    void executeFile(Object... args);

    /**
     * Add a new element to the collection if its value exceeds
     * the value of the largest element in this collection.
     * @param args
     * @throws NullPointerException
     */
    void addIfMax(Object... args) throws NullPointerException;

    /**
     * Add a new element to the collection if its value is less
     * than that of the smallest element in this collection.
     * @param args
     * @throws NullPointerException
     */
    void addIfMin(Object... args) throws NullPointerException;

    /**
     * Print the last 6 commands (without their arguments).
     * @param args
     */
    void showHistory(Object... args);

    /**
     * Remove one item from the collection whose
     * category field value is equivalent to the specified.
     * @param args
     */
    void removeByCategory(Object... args);

    /**
     * Display the number of elements whose category
     * field value is greater than the specified
     * @param args
     */
    void countGreaterThanCategory(Object... args);

    /**
     * Display elements whose meleeWeapon
     * field value is greater than the specified.
     * @param args
     */
    void filterGreaterThanMeleeWeapon(Object... args);

    /**
     * Terminate the program (without saving to a file).
     * @param args
     */
    void exitProgram(Object... args);

    /**
     * Sign up a new account.
     * @param args
     */
    void signUp(Object... args);

    /**
     * Log in to the server.
     * @param args
     */
    void logIn(Object... args);

    /**
     * Log out of the server.
     * @param args
     */
    void logOut(Object... args);

    /**
     * Load object from database to collection.
     * @param sm
     */
    void add(SpaceMarine sm);

    /**
     * Show all objects of all users
     */
    void showAll(Object... args);
}
