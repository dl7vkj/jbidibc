package org.bidib.jbidibc.core;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.NodeListener;
import org.bidib.jbidibc.exception.ProtocolException;

public interface BidibMessageProcessor {

    /**
     * Add a message listener.
     * 
     * @param messageListener
     *            the message listener to add
     */
    void addMessageListener(MessageListener messageListener);

    /**
     * Remove a message listener.
     * 
     * @param messageListener
     *            the message listener to remove
     */
    void removeMessageListener(MessageListener messageListener);

    /**
     * Add a node listener.
     * 
     * @param nodeListener
     *            the node listener to add
     */
    void addNodeListener(NodeListener nodeListener);

    /**
     * Remove a node listener.
     * 
     * @param nodeListener
     *            the node listener to remove
     */
    void removeNodeListener(NodeListener nodeListener);

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

    /**
     * @param ignoreWrongMessageNumber
     *            set the flag to ignore wrong message numbers
     */
    void setIgnoreWrongMessageNumber(boolean ignoreWrongMessageNumber);
}
