package com.company.server;

import com.company.server.service.impl.SocketServiceImpl;
import com.company.server.io.Logback;
import com.company.server.thread.Worker;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Le Trong Dat
 */
public class Main  {
    public static void main(String... args) {
        new  Thread(new Worker()).start();

        try {
            int port = args.length < 1 ? 1270 : Integer.parseInt(args[0]);
            ServerSocket serverSocket = new ServerSocket(port);
            Logback.logback("Server is listening on port " + port);
            Logback.logback("Waiting for client");
            new SocketServiceImpl(serverSocket).run();
        } catch (IOException e) {
            Logback.logback("This port is unavailable now.");
        }
    }
}
