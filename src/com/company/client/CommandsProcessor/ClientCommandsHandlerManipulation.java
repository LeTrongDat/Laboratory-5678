package com.company.client.CommandsProcessor;

import com.company.shared.entity.CommandData;
import com.company.shared.annotations.Command;

import java.lang.reflect.InvocationTargetException;

public interface ClientCommandsHandlerManipulation {
    @Command(name = "help", usage = "Display available commands")
    CommandData printListCommand(Object... args);

    @Command(name = "info", usage = "Display information about the collection (type, initialization date, number of elements, etc.) " +
            "in the standard output stream")
    CommandData getGeneralInformation(Object... args);

    @Command(name = "show", usage = "Output to the standard output stream all the elements of the collection " +
            "in a string representation")
    CommandData getDetailsInformation(Object... args);

    @Command(name = "add", usage = "Add new object to collection")
    CommandData addObject(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException;

    @Command(name = "update", usage = "Update the value of a collection element whose id is equal to the specified", param = 1)
    CommandData update(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException;

    @Command(name = "remove_by_id", usage = "Remove an item from the collection by its id", param = 1)
    CommandData removeById(Object... args);

    @Command(name = "clear", usage = "Clear collection")
    CommandData clear(Object... args);

    @Deprecated
    @Command(name = "save", usage = "Save collection to file")
    CommandData save(Object... args);

    @Command(name = "execute_script", usage = "Read and execute the script from the specified file." +
            "The script contains commands in the same form in which they are entered by the user interactively.",
            param = 1)
    CommandData executeFile(Object... args);

    @Command(name = "add_if_max", usage = "Add a new element to the collection if its value exceeds " +
            "the value of the largest element in this collection")
    CommandData addIfMax(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException;

    @Command(name = "add_if_min", usage = "Add a new element to the collection if its value is less " +
            "than that of the smallest element in this collection")
    CommandData addIfMin(Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException;

    @Command(name = "history", usage = "Print the last 6 commands (without their arguments)")
    CommandData showHistory(Object... args);

    @Command(name = "remove_any_by_category", usage = "Remove one item from the collection whose " +
            "category field value is equivalent to the specified", param = 1)
    CommandData removeByCategory(Object... args);

    @Command(name = "count_greater_than_category", usage = "Display the number of elements whose category " +
            "field value is greater than the specified", param = 1)
    CommandData countGreaterThanCategory(Object... args);

    @Command(name = "filter_greater_than_melee_weapon", usage = "Display elements whose meleeWeapon " +
            "field value is greater than the specified", param = 1)
    CommandData filterGreaterThanMeleeWeapon(Object... args);

    @Command(name = "exit", usage = "Terminate the program (without saving to a file)")
    void exitProgram(Object... args);

}
