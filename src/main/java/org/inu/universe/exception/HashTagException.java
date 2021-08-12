package org.inu.universe.exception;

public class HashTagException extends RuntimeException {
    public HashTagException() {
    }

    public HashTagException(String message) {
        super(message);
    }

    public HashTagException(Throwable cause) {
        super(cause);
    }
}
