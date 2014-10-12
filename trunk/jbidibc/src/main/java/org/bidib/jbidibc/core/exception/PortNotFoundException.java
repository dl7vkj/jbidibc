package org.bidib.jbidibc.core.exception;

public class PortNotFoundException extends Exception implements ReasonAware {
    private static final long serialVersionUID = 1L;

    public static final String PORT_NOT_FOUND = "portNotFound";

    public PortNotFoundException() {
        super();
    }

    public PortNotFoundException(String message) {
        super(message);
    }

    public PortNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return the reason
     */
    @Override
    public String getReason() {
        return PORT_NOT_FOUND;
    }
}
