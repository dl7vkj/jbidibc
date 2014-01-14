package org.bidib.jbidibc.exception;

public class PortNotOpenedException extends Exception implements ReasonAware {
    private static final long serialVersionUID = 1L;

    public static final String PORT_IN_USE = "portInUse";

    public static final String UNKNOWN = "unknown";

    private String reason;

    public PortNotOpenedException() {
        super();
    }

    public PortNotOpenedException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    public PortNotOpenedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return the reason
     */
    @Override
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}
