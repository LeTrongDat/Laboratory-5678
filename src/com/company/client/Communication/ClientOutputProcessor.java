package com.company.client.Communication;

import com.company.shared.CommandData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientOutputProcessor {
    private Socket socket;
    public ClientOutputProcessor(Socket socket) {
        this.socket = socket;
    }
    public void serializeServerInput(CommandData commandData) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(commandData);
    }
}
