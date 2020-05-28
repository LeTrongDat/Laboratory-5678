package com.company.server.thread;

import com.company.server.communication.ServerInputProcessor;
import com.company.server.communication.ServerOutputProcessor;
import com.company.server.processor.ServerCommandsManager;
import com.company.server.service.SocketService;
import com.company.server.service.impl.SocketServiceImpl;

import java.net.Socket;
import java.util.concurrent.*;

public class SocketSession implements Runnable {
    private Socket socket;

    public SocketSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        SocketServiceImpl.cachedThreadPool.submit(new SocketReader(socket));

        SocketServiceImpl.fixedThreadPool.submit(new ServerCommandsManager());

        new Thread(new SocketSender(socket)).start();
    }
}
