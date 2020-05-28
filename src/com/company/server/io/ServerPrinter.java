package com.company.server.io;

import java.util.Arrays;

public interface ServerPrinter {
    String getResult();
    void print(String type, Object... objs);
    default String printObj(Object... objs) {
        return Arrays.toString(objs).replaceAll("[\\[\\]]", "") + "\n> ";
    }
    default String rawPrint(Object... objs) {
        return Arrays.toString(objs).replaceAll("[\\[\\]]", "");
    }
}
