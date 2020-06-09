package com.company.server.io.impl;

import com.company.server.io.Readable;
import com.company.shared.entity.CommandData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class SocketReader implements Runnable, Readable<CommandData> {
    private Socket socket;

    private BlockingQueue<CommandData> commandQueue;

    public SocketReader(Socket socket, BlockingQueue<CommandData> commandQueue) {
        this.socket = socket;

        this.commandQueue = commandQueue;
    }

    @Override
    public void run() {
        try {
            while (true) this.commandQueue.put(read());
        } catch (IOException | InterruptedException e) {
            Log.logback("The client at port " + socket.getPort() + " has disconnected.");
        }
    }

    @Override
    public CommandData read() throws IOException {
        CommandData commandData = null;

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            commandData = (CommandData) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            Log.logback("The format of data is not correct.");
        }

        return commandData;
    }
}
