package com.company.server;

import com.company.client.CommandsProcessor.ClientCommandsManager;
import com.company.server.processor.ServerCommandsManager;
import com.company.server.communication.ServerSocketManager;
import com.company.server.io.Logback;
import sun.nio.ch.ThreadPool;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Le Trong Dat
 */
public class Main  {
    public static void main(String... args) throws InvocationTargetException, IllegalAccessException {

        System.out.print("> ");
        ServerCommandsManager serverCommandsManager = new ServerCommandsManager();
        new Thread(() -> {
            ClientCommandsManager clientCommandsManager = new ClientCommandsManager();
            while (true) {
                try {
                    serverCommandsManager.execute(clientCommandsManager.execute());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            int port = args.length < 1 ? 1270 : Integer.parseInt(args[0]);
            ServerSocket serverSocket = new ServerSocket(port);
            Logback.logback("Server is listening on port " + port);
            Logback.logback("Waiting for client");
            while (true) { new ServerSocketManager(serverSocket).run(serverCommandsManager); }
        } catch (IOException e) {
            Logback.logback("This port is unavailable now.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
