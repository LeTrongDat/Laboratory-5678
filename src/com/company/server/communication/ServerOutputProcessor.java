package com.company.server.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerOutputProcessor {
    private Socket socket;
    public ServerOutputProcessor(Socket socket) {
        this.socket = socket;
    }
    public void serializeClientInput(String messages) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(messages);
    }
}
