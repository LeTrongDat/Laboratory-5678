package com.company.client.Communication;

public class ClientQueryPrinter implements ClientPrinter {
    private boolean executeFileMode;

    public ClientQueryPrinter() {
        executeFileMode = false;
    }

    @Override
    public boolean isExecuteFileMode() {
        return executeFileMode;
    }

    @Override
    public void setMode(boolean executeFileMode) {
        this.executeFileMode = executeFileMode;
    }

    @Override
    public void print(String str, Object... objs) {
        if (!isExecuteFileMode()) {
            if (str.equals("print")) printObj(objs);
                else rawPrint(objs);
        }
    }
}
