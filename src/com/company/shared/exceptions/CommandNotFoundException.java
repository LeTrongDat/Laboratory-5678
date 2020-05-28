package com.company.shared.exceptions;

/**
 * Exception for all invalid commands from users.
 * @author Le Trong Dat
 */
public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(){
        super();
    }
    public CommandNotFoundException(String mess, Throwable e) {
        super(mess, e);
    }
}
