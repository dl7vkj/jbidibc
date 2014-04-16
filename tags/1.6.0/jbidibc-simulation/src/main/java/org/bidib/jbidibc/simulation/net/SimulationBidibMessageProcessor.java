package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.exception.ProtocolException;

public interface SimulationBidibMessageProcessor extends BidibMessageProcessor {

    /**
     * Publish the response in the provided byte array output stream.
     * 
     * @param output
     *            the output stream that contains the responses
     * @throws ProtocolException
     */
    void publishResponse(final ByteArrayOutputStream output) throws ProtocolException;
}
