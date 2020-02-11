package com.peggir.SimpleDbUtil.exceptions;

/**
 * Exception thrown when unable to map a result set to an object.
 */
public class DbCallResultSetMapperException extends Exception {

    public static final String DEFAULT_ERROR_MSG = "Unable to map result set to object";

    /**
     * Thrown when unable to map a result set to an object.
     *
     * @param message Explanation of the exception
     * @param cause   Cause of the exception
     */
    public DbCallResultSetMapperException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
