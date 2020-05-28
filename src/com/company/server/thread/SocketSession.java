package com.company.server.thread;

import com.company.server.controller.CommandController;
import com.company.server.factory.impl.SocketFactoryImpl;
import com.company.server.io.impl.SocketReader;
import com.company.server.io.impl.SocketSender;
import com.company.shared.entity.CommandData;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class SocketSession implements Runnable {
    private Socket socket;

    public SocketSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BlockingQueue<CommandData> commandQueue = new SynchronousQueue<>();
        BlockingQueue<String> messageQueue = new SynchronousQueue<>();

        SocketFactoryImpl.cachedThreadPool.submit(new SocketReader(socket, commandQueue));

        SocketFactoryImpl.fixedThreadPool.submit(new CommandController(commandQueue, messageQueue));

        new Thread(new SocketSender(socket, messageQueue)).start();
    }
}
