package org.inu.universe.exception;

public class ProfileException extends RuntimeException{
    public ProfileException() {
    }

    public ProfileException(String message) {
        super(message);
    }

    public ProfileException(Throwable cause) {
        super(cause);
    }
}
