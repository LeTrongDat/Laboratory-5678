package com.company.server.factory;

import java.io.IOException;
import java.net.Socket;

public interface SocketFactory {

    /**
     * Run service for new client.
     */
    void run();


    /**
     * Set up new end point between new client and server.
     * @return
     * @throws IOException when can not set up the end point.
     */
    Socket connect() throws IOException;
}
