package com.example.miniproyecto3.exceptions;

public class DoubleShootException extends RuntimeException {

    public DoubleShootException(){
        super();
    }

    public DoubleShootException(String message) {
        super(message);
    }

    public DoubleShootException(String message, Throwable cause) {
        super(message, cause);
    }

    public DoubleShootException(Throwable cause) {
        super(cause);
    }
}
