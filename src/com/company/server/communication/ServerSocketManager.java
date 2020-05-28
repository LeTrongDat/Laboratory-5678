package com.company.server.communication;

import com.company.server.processor.ServerCommandsManager;
import com.company.server.io.Logback;
import com.company.shared.CommandData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketManager {
    private ServerSocket serverSocket;
    private ServerInputProcessor serverInputProcessor;
    private ServerOutputProcessor serverOutputProcessor;

    public ServerSocketManager(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void run(ServerCommandsManager serverCommandsManager) throws Exception {
        try {
            Socket socket = connect();
            try {
                while (true) {
                    String messages = serverCommandsManager.execute(receiveData()).getResult();
                    sendData(messages);
                }
            } catch (IOException e) {
                serverCommandsManager.execute(new CommandData("save", new Object[]{}));
                Logback.logback("The client has disconnected. This socket will be closed. Please re-connect again to communicate with server.");
                socket.close();
            }
        } catch (IOException e) {
            Logback.logback("The server socket is closed by host or runs out of resources.");
        }
    }

    public Socket connect() throws IOException {
        Socket socket = serverSocket.accept();
        Logback.logback("Successful connect.");
        serverInputProcessor = new ServerInputProcessor(socket);
        serverOutputProcessor = new ServerOutputProcessor(socket);
        return socket;
    }

    public CommandData receiveData() throws IOException {
        return serverInputProcessor.deserializeClientOutput();
    }
    public void sendData(String messages) throws IOException {
        serverOutputProcessor.serializeClientInput(messages);
    }
}
