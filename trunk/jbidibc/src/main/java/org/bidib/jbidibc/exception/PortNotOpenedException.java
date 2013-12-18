package org.bidib.jbidibc.exception;

public class PortNotOpenedException extends Exception {
    private static final long serialVersionUID = 1L;

    public PortNotOpenedException() {
        super();
    }

    public PortNotOpenedException(String message) {
        super(message);
    }

    public PortNotOpenedException(String message, Throwable cause) {
        super(message, cause);
    }
}
