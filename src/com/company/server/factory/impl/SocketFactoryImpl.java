package com.company.server.factory.impl;

import com.company.server.io.impl.Log;
import com.company.server.factory.SocketFactory;
import com.company.server.thread.SocketSession;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketFactoryImpl implements SocketFactory {
    private ServerSocket serverSocket;

    private static final int nThread;

    public static final ExecutorService cachedThreadPool;

    public static final ExecutorService fixedThreadPool;

    static {
        nThread = 20;

        cachedThreadPool = Executors.newCachedThreadPool();

        fixedThreadPool = Executors.newFixedThreadPool(nThread);
    }

    public SocketFactoryImpl(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        while (true) {
            try {
                Socket socket = connect();

                new Thread(new SocketSession(socket)).start();
            } catch (IOException e) {
                Log.logback("The server socket is closed by host or runs out of resources.");
            }
        }
    }

    public Socket connect() throws IOException {
        Socket socket = this.serverSocket.accept();

        Log.logback("Successful connect with the user at port: " + socket.getPort());

        return socket;
    }
}
