package org.inu.universe.exception;

public class EmailException extends RuntimeException {
    public EmailException() {
    }

    public EmailException(String message) {
        super(message);
    }

    public EmailException(Throwable cause) {
        super(cause);
    }
}
