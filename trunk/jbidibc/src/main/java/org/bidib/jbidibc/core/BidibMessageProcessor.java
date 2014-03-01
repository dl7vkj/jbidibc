package org.bidib.jbidibc.core;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.exception.ProtocolException;

public interface BidibMessageProcessor {

    /**
     * Process the messages in the provided byte array output stream.
     * 
     * @param output
     *            the output stream that contains the messages
     * @throws ProtocolException
     */
    void processMessages(final ByteArrayOutputStream output) throws ProtocolException;

    /**
     * @return returns device specific error information
     */
    String getErrorInformation();
}
