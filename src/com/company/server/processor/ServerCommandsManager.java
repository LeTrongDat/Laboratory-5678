package com.company.server.processor;

import com.company.server.dao.reader.csv.OpenCSVReader;
import com.company.server.io.ServerPrinter;
import com.company.server.io.ServerResultPrinter;
import com.company.shared.CommandData;

import java.lang.reflect.InvocationTargetException;

/**
 * A class for managing all the commands.
 * @author Le Trong Dat
 */
public class ServerCommandsManager {
    private ServerCommandsHandler serverCommandsHandler = new ServerCommandsHandler();

    public ServerCommandsManager() {
        OpenCSVReader.getInput(serverCommandsHandler);
        ServerPrinter resultServerPrinter = new ServerResultPrinter();
        serverCommandsHandler.setResultServerPrinter(resultServerPrinter);
    }
    public ServerPrinter execute(CommandData commandData) throws InvocationTargetException, IllegalAccessException {
        if (commandData == null) return new ServerResultPrinter();
        ServerPrinter serverResultPrinter = new ServerResultPrinter();
        serverCommandsHandler.setResultServerPrinter(serverResultPrinter);
        serverCommandsHandler.processCommand(commandData);
        return serverResultPrinter;
    }
}