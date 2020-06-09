package com.company.server.thread;

import com.company.client.CommandsProcessor.ClientCommandsManager;
import com.company.server.io.impl.MessageCollector;
import com.company.server.factory.impl.CommandFactoryImpl;

public class Worker implements Runnable {
    @Override
    public void run() {
        System.out.print("> ");

        ClientCommandsManager clientCommandsManager = new ClientCommandsManager();

        while (true) {
            try {
                CommandFactoryImpl commandFactoryImpl = new CommandFactoryImpl();

                commandFactoryImpl.setResponder(new MessageCollector());

                commandFactoryImpl.processCommand(clientCommandsManager.execute());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
