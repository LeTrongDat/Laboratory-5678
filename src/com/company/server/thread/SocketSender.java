package com.company.server.thread;

import com.company.server.communication.ServerOutputProcessor;
import com.company.server.io.Sendable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SocketSender implements Runnable, Sendable<String> {
    private Socket socket;
    private BlockingQueue<String> messageQueue;

    public SocketSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) send(messageQueue.take());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(message);
    }
}
