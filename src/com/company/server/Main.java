package com.company.server;

import com.company.server.factory.impl.SocketFactoryImpl;
import com.company.server.io.impl.Log;
import com.company.server.thread.Worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

/**
 * @author Le Trong Dat
 */
public class Main  {
    public static void main(String... args) {
        new  Thread(new Worker()).start();
        try {
            new Resource().run();
            System.out.print("> ");
        } catch (SQLException e) {
            Log.logback("Message: " + e.getMessage());
            Log.logback("SQL State: " + e.getSQLState());
        }
        try {
            int port = args.length < 1 ? 1270 : Integer.parseInt(args[0]);
            ServerSocket serverSocket = new ServerSocket(port);
            Log.logback("Server is listening on port " + port);
            Log.logback("Waiting for client");
            new SocketFactoryImpl(serverSocket).run();
        } catch (IOException e) {
            Log.logback("This port is unavailable now.");
        }
    }
}
