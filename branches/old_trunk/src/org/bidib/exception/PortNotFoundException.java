package org.bidib.exception;

public class PortNotFoundException extends Exception {
    private static final long serialVersionUID = 1185914351410243364L;

    public PortNotFoundException() {
        super();
    }

    public PortNotFoundException(String message) {
        super(message);
    }

    public PortNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
