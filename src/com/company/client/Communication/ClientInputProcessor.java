package com.company.client.Communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientInputProcessor {
    private Socket socket;
    public ClientInputProcessor(Socket socket) {
        this.socket = socket;
    }
    public String deserializeServerOutput() throws IOException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return (String) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("The format of object is not correct.");
            return null;
        }
    }
}
