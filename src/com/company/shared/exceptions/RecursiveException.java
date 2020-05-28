package com.company.shared.exceptions;

public class RecursiveException extends Exception {
    public RecursiveException() {
        super();
    }
    public RecursiveException(String mess, Throwable e) {
        super(mess, e);
    }
}
