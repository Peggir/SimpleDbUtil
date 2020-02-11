package com.peggir.SimpleDbUtil.exceptions;

/**
 * Generic exception thrown when unable to query a database.
 */
public class DbCallException extends Exception {

    public static final String DEFAULT_ERROR_MSG = "Error during database operation";

    /**
     * Thrown when unable to query a database.
     *
     * @param message Explanation of the exception
     * @param cause   Cause of the exception
     */
    public DbCallException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
