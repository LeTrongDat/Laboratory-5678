package com.company.server.processor;

import com.company.server.dao.reader.csv.OpenCSVReader;
import com.company.server.io.ServerPrinter;
import com.company.server.io.ServerResultPrinter;
import com.company.shared.CommandData;
import jdk.nashorn.internal.ir.Block;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * A class for managing all the commands.
 * @author Le Trong Dat
 */
public class ServerCommandsManager implements Runnable {
    private BlockingQueue<CommandData> commandDataBlockingQueue;
    private BlockingQueue<String> stringBlockingQueue;
    private ServerCommandsHandler serverCommandsHandler = new ServerCommandsHandler();

    public void setCommandDataBlockingQueue(BlockingQueue<CommandData> commandDataBlockingQueue) {
        this.commandDataBlockingQueue = commandDataBlockingQueue;
    }

    public void setStringBlockingQueue(BlockingQueue<String> stringBlockingQueue) {
        this.stringBlockingQueue = stringBlockingQueue;
    }

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

    @Override
    public void run() {
        try {
            while (true) {
                String message = execute(commandDataBlockingQueue.take()).getResult();
                stringBlockingQueue.put(message);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}