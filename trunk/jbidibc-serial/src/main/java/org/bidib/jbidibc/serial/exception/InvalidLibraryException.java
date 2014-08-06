package org.bidib.jbidibc.serial.exception;

public class InvalidLibraryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidLibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
