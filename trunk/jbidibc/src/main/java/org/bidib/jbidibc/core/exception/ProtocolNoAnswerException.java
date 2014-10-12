package org.bidib.jbidibc.core.exception;

public class ProtocolNoAnswerException extends ProtocolException {
    private static final long serialVersionUID = 1L;

    public ProtocolNoAnswerException() {
        super();
    }

    public ProtocolNoAnswerException(String message) {
        super(message);
    }

    public ProtocolNoAnswerException(Throwable cause) {
        super(cause);
    }

    public ProtocolNoAnswerException(String message, Throwable cause) {
        super(message, cause);
    }
}
