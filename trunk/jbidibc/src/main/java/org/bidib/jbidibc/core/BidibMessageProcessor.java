package org.bidib.jbidibc.core;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.exception.ProtocolException;

public interface BidibMessageProcessor {

    /**
     * Process the messages in the provided byte array output stream.
     * 
     * @param context
     *            the context
     * @param output
     *            the output stream that contains the messages
     * @throws ProtocolException
     */
    void processMessages(final Context context, final ByteArrayOutputStream output) throws ProtocolException;
}
