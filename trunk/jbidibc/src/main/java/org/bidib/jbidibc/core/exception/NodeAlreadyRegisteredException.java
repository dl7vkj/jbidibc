package org.bidib.jbidibc.core.exception;

/**
 * This exception is thrown if a new node is already registered in the nodes list.
 * 
 */
public class NodeAlreadyRegisteredException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String description;

    public NodeAlreadyRegisteredException() {
        super();
    }

    public NodeAlreadyRegisteredException(String message) {
        super(message);
    }

    public NodeAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

    public NodeAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
