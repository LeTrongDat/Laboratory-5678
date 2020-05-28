package com.company.client.Communication;

import java.util.Arrays;

public interface ClientPrinter {
    public boolean isExecuteFileMode();
    public void setMode(boolean executeFileMode);
    public void print(String type, Object... objs);
    public default void printObj(Object... objs) {
        System.out.println(Arrays.toString(objs).replaceAll("[\\[\\]]", ""));
        System.out.print("> ");
    }
    public default void rawPrint(Object... objs) {
        System.out.print(Arrays.toString(objs).replaceAll("[\\[\\]]", ""));
    }
}
