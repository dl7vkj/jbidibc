package org.bidib.jbidibc.exception;

/**
 * This exception is thrown if no answer was received from communication partner.
 *
 */
public class NoAnswerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

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
}
