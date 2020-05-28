package com.company.server.io;

import java.io.IOException;

public interface Readable<T> {
    T read() throws IOException;
}
