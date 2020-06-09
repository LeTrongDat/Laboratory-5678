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
    private CommandFactoryImpl commandFactoryImpl;

    private final BlockingQueue<CommandData> commandQueue;

    private final BlockingQueue<String> messageQueue;

    {
        commandFactoryImpl = new CommandFactoryImpl();
    }

    public CommandController(BlockingQueue<CommandData> commandQueue, BlockingQueue<String> messageQueue) {
        this.commandQueue = commandQueue;

        this.messageQueue = messageQueue;
    }

    public void execute() throws InterruptedException, InvocationTargetException, IllegalAccessException {

        Collector<String> responder = new MessageCollector();

        commandFactoryImpl.setResponder(responder);

        commandFactoryImpl.processCommand(commandQueue.take());

        String message = responder.getCollection();

        messageQueue.put(message);
    }

    @Override
    public void run() {
        try { while (true) execute(); }
        catch (Exception e) { e.printStackTrace(); }
    }
}