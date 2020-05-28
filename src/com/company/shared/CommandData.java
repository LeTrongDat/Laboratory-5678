package com.company.shared;

import java.io.Serializable;
import java.util.Objects;

public class CommandData implements Serializable {
    private String commandName;
    private Object[] commandArguments;
    public CommandData(String commandName, Object... commandArguments) {
        this.commandArguments = commandArguments;
        this.commandName = commandName;
    }
    public String getCommandName() {
        return this.commandName;
    }
    public Object[] getCommandArguments() {
        return this.commandArguments;
    }
}