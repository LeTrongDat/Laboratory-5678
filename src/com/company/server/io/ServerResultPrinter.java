package com.company.server.io;

public class ServerResultPrinter implements ServerPrinter {
    private String result;

    public ServerResultPrinter() {
        result = "";
    }

    public String getResult() {
        return result;
    }

    @Override
    public void print(String str, Object... objs) {
        if (str.equals("print")) result += printObj(objs);
        else {
            result += rawPrint(objs);
        }
    }
}
