package com.guvi.mindfulness.exception;

/**
 * Wraps checked SQLExceptions so that service and servlet layers can report user friendly messages.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}

