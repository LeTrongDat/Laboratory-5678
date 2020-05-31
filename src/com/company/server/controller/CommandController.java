package com.company.server.controller;

import com.company.server.factory.impl.CommandFactoryImpl;
import com.company.server.io.Collector;
import com.company.server.io.impl.MessageCollector;
import com.company.shared.entity.CommandData;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;

/**
 * A class for managing all the commands.
 * @author Le Trong Dat
 */
public class CommandController implements Runnable {
    private CommandFactoryImpl commandFactoryImpl = new CommandFactoryImpl();
    private BlockingQueue<CommandData> commandQueue;
    private BlockingQueue<String> messageQueue;

    public CommandController(BlockingQueue<CommandData> commandQueue, BlockingQueue<String> messageQueue) {
        this.commandQueue = commandQueue;
        this.messageQueue = messageQueue;
    }

    public void execute() throws InterruptedException, InvocationTargetException, IllegalAccessException {
        Collector<String> messageCollector = new MessageCollector();
        commandFactoryImpl.setMessageCollector(messageCollector);

        System.out.println(1);
        commandFactoryImpl.processCommand(commandQueue.take());
        String message = messageCollector.getCollection();
        messageQueue.put(message);
    }

    @Override
    public void run() {
        try {
            while (true) execute();
        } catch (IllegalAccessException | InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}