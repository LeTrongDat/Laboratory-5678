package com.company.server.thread;

import com.company.client.CommandsProcessor.ClientCommandsManager;
import com.company.server.io.MessageCollector;
import com.company.server.processor.ServerCommandsHandler;

public class Worker implements Runnable {
    @Override
    public void run() {
        System.out.print("> ");
        ClientCommandsManager clientCommandsManager = new ClientCommandsManager();
        while (true) {
            try {
                ServerCommandsHandler serverCommandsHandler = new ServerCommandsHandler();
                serverCommandsHandler.setMessageCollector(new MessageCollector());
                serverCommandsHandler.processCommand(clientCommandsManager.execute());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
