package org.bidib.jbidibc.core.exception;

/**
 * This exception is thrown if no answer was received from communication partner.
 * 
 */
public class NoAnswerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String description;

    public NoAnswerException() {
        super();
    }

    public NoAnswerException(String message) {
        super(message);
    }

    public NoAnswerException(Throwable cause) {
        super(cause);
    }

    public NoAnswerException(String message, Throwable cause) {
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
