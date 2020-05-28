package com.company.server.processor;

import com.company.server.io.Collector;
import com.company.server.io.MessageCollector;
import com.company.shared.CommandData;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;

/**
 * A class for managing all the commands.
 * @author Le Trong Dat
 */
public class ServerCommandsManager implements Runnable {
    private ServerCommandsHandler serverCommandsHandler = new ServerCommandsHandler();
    private BlockingQueue<CommandData> commandQueue;
    private BlockingQueue<String> messageQueue;

    public ServerCommandsManager(BlockingQueue<CommandData> commandQueue, BlockingQueue<String> messageQueue) {
        this.commandQueue = commandQueue;
        this.messageQueue = messageQueue;
    }

    public void execute() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        Collector<String> messageCollector = new MessageCollector();
        serverCommandsHandler.setMessageCollector(messageCollector);
        serverCommandsHandler.processCommand(commandQueue.take());
        String message = messageCollector.getCollection();
        messageQueue.put(message);
    }

    @Override
    public void run() {
        try {
            while (true) execute();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}