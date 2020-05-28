package com.company.server;

import com.company.server.account.Account;
import com.company.server.communication.ServerInputProcessor;
import com.company.server.communication.ServerOutputProcessor;
import com.company.server.io.Logback;
import com.company.server.processor.ServerCommandsHandler;
import com.company.server.processor.ServerCommandsManager;
import com.company.shared.CommandData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Session implements Runnable {
    private final Account account;
    private final Socket socket;
    private final ServerCommandsManager serverCommandsManager;
    private ServerInputProcessor serverInputProcessor;
    private ServerOutputProcessor serverOutputProcessor;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


    public Session(Socket socket, Account account, ServerCommandsManager serverCommandsManager) {
        this.socket = socket;
        this.account = account;
        this.serverCommandsManager = serverCommandsManager;
        this.serverInputProcessor = new ServerInputProcessor(socket);
        this.serverOutputProcessor = new ServerOutputProcessor(socket);
    }

    @Override
    public void run() {
        process();
    }
    private void process() {
        try {
            while (true) {
                String messages = serverCommandsManager.execute(receiveData()).getResult();
                sendData(messages);
            }
        } catch (IOException e) {
            try {
                serverCommandsManager.execute(new CommandData("save", new Object[]{}));
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            Logback.logback("The client has disconnected. This socket will be closed. Please re-connect again to communicate with server.");
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public CommandData receiveData() throws IOException {
        return serverInputProcessor.deserializeClientOutput();
    }
    public void sendData(String messages) throws IOException {
        serverOutputProcessor.serializeClientInput(messages);
    }
}
