package com.company.server.thread;

import com.company.server.io.Logback;
import com.company.server.io.Readable;
import com.company.shared.CommandData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SocketReader implements Runnable, Readable<CommandData> {
    private Socket socket;
    private BlockingQueue<CommandData> blockingQueue;

    public SocketReader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) blockingQueue.put(read());
        } catch (IOException | InterruptedException e) {
            Logback.logback("The client has disconnected. This socket will be closed. Please re-connect again to communicate with server.");
        }
    }

    @Override
    public CommandData read() throws IOException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return (CommandData) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            Logback.logback("The format of data is not correct.");
            return null;
        }
    }
}
