package org.inu.universe.exception;

public class AccountException extends RuntimeException {
    public AccountException() {
    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(Throwable cause) {
        super(cause);
    }
}
