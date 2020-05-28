package com.company.server.thread;

import com.company.server.processor.ServerCommandsManager;
import com.company.server.service.impl.SocketServiceImpl;
import com.company.shared.CommandData;

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

        SocketServiceImpl.cachedThreadPool.submit(new SocketReader(socket, commandQueue));

        SocketServiceImpl.fixedThreadPool.submit(new ServerCommandsManager(commandQueue, messageQueue));

        new Thread(new SocketSender(socket, messageQueue)).start();
    }
}
