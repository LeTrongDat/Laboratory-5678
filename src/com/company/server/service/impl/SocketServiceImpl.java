package com.company.server.service.impl;

import com.company.server.io.Logback;
import com.company.server.service.SocketService;
import com.company.server.thread.SocketSession;
import sun.nio.ch.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServiceImpl implements SocketService {
    private ServerSocket serverSocket;
    private static final int nThread = 20;

    public static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    public static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(nThread);


    public SocketServiceImpl(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        while (true) {
            try {
                Socket socket = connect();
                new Thread(new SocketSession(socket)).start();
            } catch (IOException e) {
                Logback.logback("The server socket is closed by host or runs out of resources.");
            }
        }
    }

    public Socket connect() throws IOException {
        Socket socket = serverSocket.accept();
        Logback.logback("Successful connect with the user at port: " + socket.getPort());
        return socket;
    }
}
