package com.company.server.io;

import java.io.IOException;

public interface Sendable<T> {
    void send(T foo) throws IOException;
}
