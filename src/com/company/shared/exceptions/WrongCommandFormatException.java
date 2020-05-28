package com.company.shared.exceptions;

public class WrongCommandFormatException extends Exception {
    public WrongCommandFormatException(){
        super();
    }
    public WrongCommandFormatException(String mess, Throwable e) {
        super(mess, e);
    }
}
