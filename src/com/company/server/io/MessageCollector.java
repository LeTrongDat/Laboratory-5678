package com.company.server.io;

import java.util.Arrays;

public class MessageCollector implements Collector<String> {
    private String message;

    public MessageCollector() {
        message = "";
    }

    @Override
    public void collect(String... items) {
        message += Arrays.toString(items).replaceAll("[\\[\\]]", "") + "\n> ";
    }

    @Override
    public String getCollection() {
        return message;
    }
}
