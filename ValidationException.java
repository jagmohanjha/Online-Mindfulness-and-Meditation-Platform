package com.guvi.mindfulness.exception;

/**
 * Thrown when incoming data from servlets fails business validation.
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}

