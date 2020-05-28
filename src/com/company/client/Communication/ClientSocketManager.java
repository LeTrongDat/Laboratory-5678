package com.company.client.Communication;

import com.company.client.CommandsProcessor.ClientCommandsManager;
import com.company.shared.CommandData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocketManager {
    private ClientInputProcessor clientInputProcessor;
    private ClientOutputProcessor clientOutputProcessor;
    public void run(int port, ClientCommandsManager clientCommandsManager) {
        try {
            connect(port);
            try {
                while (true) {
                    sendData(clientCommandsManager.execute());
                    System.out.print(receiveData());
                }
            } catch (Exception e) {
                System.out.print("Reconnect to the server... \n> ");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.print("The host is unreachable\n> ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void connect(int port) throws IOException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        Socket socket = new Socket(inetAddress.getHostName(), port);
        System.out.print("Successful connected to the server\n> ");
        clientInputProcessor = new ClientInputProcessor(socket);
        clientOutputProcessor = new ClientOutputProcessor(socket);
    }
    public String receiveData() throws IOException {
        return clientInputProcessor.deserializeServerOutput();
    }
    public void sendData(CommandData commandData) throws IOException {
        clientOutputProcessor.serializeServerInput(commandData);
    }
}
