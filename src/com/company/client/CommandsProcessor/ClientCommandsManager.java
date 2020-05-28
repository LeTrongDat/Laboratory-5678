package com.company.client.CommandsProcessor;

import com.company.client.Communication.ClientConsoleInputProcessor;
import com.company.client.Communication.ClientPrinter;
import com.company.client.Communication.ClientQueryPrinter;
import com.company.client.Communication.ClientResultPrinter;
import com.company.shared.entity.CommandData;

import java.util.Arrays;

/**
 * A class for managing all the commands.
 * @author Le Trong Dat
 */
public class ClientCommandsManager {
    private ClientCommandsHandler clientCommandsHandler = new ClientCommandsHandler();
    public ClientCommandsManager() {
        ClientConsoleInputProcessor clientConsoleInputProcessor = new ClientConsoleInputProcessor();
        ClientPrinter resultClientPrinter = new ClientResultPrinter();
        ClientPrinter queryClientPrinter = new ClientQueryPrinter();

        clientConsoleInputProcessor.setResultClientPrinter(resultClientPrinter);
        clientConsoleInputProcessor.setQueryClientPrinter(queryClientPrinter);

        clientCommandsHandler.setQueryClientPrinter(queryClientPrinter);
        clientCommandsHandler.setResultClientPrinter(resultClientPrinter);
        clientCommandsHandler.addClientConsoleInputProcessor(clientConsoleInputProcessor);
    }
    public CommandData execute() throws Exception {
        String string = clientCommandsHandler.getClientConsoleInputProcessor().getString(0);

        if (string == null) {
            clientCommandsHandler.addClientConsoleInputProcessor(null);
            if (clientCommandsHandler.getClientConsoleInputProcessor().getNameOfFileStream() == null) {
                clientCommandsHandler.getQueryClientPrinter().setMode(false);
                clientCommandsHandler.getResultClientPrinter().print("rawPrint", "Please enter the next command: ");
            }
            return null;
        }
        String[] strings = string.split(" ");
        return clientCommandsHandler.processCommand(strings[0], Arrays.copyOfRange(strings, 1, strings.length));
    }
}