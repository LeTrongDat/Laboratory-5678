package com.company.server.io.impl;

import com.company.server.io.Sendable;
import com.company.server.observer.Observer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SocketSender implements Runnable, Sendable<String> {
    private Socket socket;
    private BlockingQueue<String> messageQueue;

    public SocketSender(Socket socket, BlockingQueue<String> messageQueue) {
        this.socket = socket;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        try {
            while (true) send(messageQueue.take());
        } catch (InterruptedException | IOException e) {
            Observer.unsubscribe(messageQueue);
        }
    }

    @Override
    public void send(String message) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(message);
    }
}
