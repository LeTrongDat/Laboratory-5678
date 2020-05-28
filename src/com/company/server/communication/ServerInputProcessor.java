package com.company.server.communication;

import com.company.server.io.Logback;
import com.company.shared.CommandData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerInputProcessor {
    private Socket socket;
    public ServerInputProcessor(Socket socket) {
        this.socket = socket;
    }
    public CommandData deserializeClientOutput() throws IOException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return (CommandData) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            Logback.logback("The format of data is not correct.");
            return null;
        }
    }
}
