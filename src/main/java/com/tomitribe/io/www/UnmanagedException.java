package com.tomitribe.io.www;

/**
 * A wrapper for unmanaged checked exceptions.
 */
public class UnmanagedException extends RuntimeException {
    public UnmanagedException(Throwable cause) {
        super(cause);
    }
}
