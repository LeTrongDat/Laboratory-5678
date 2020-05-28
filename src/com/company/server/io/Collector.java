package com.company.server.io;

public interface Collector<T> {
    void collect(T... items);
    T getCollection();
}
