package com.company.shared.exceptions;

/**
 * Exception for all invalid fields typed from users.
 * @author Le Trong Dat
 */
public class InvalidValueException extends Exception {
    public InvalidValueException(){
        super();
    }
    public InvalidValueException(String mess, Throwable e) {
        super(mess, e);
    }

}
