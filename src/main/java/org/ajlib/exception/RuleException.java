package org.ajlib.exception;

public class RuleException extends RuntimeException {
    public RuleException() {
    }

    public RuleException(String message) {
        super(message);
    }

    public RuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
