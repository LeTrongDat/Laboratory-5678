package com.company.server.thread;

import com.company.client.CommandsProcessor.ClientCommandsManager;
import com.company.server.processor.ServerCommandsManager;

public class Worker implements Runnable {

    @Override
    public void run() {
        System.out.print("> ");
        ClientCommandsManager clientCommandsManager = new ClientCommandsManager();
        ServerCommandsManager serverCommandsManager = new ServerCommandsManager();
        while (true) {
            try {
                serverCommandsManager.execute(clientCommandsManager.execute());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
