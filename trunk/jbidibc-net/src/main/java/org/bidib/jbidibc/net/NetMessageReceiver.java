package org.bidib.jbidibc.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.MessageReceiver;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.node.NodeFactory;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetMessageReceiver extends MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetMessageReceiver.class);

    private ByteArrayOutputStream output;

    protected NetMessageReceiver(NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    @Override
    public synchronized void processMessages(final ByteArrayOutputStream unescapedInput) throws ProtocolException {

        try {
            output = new ByteArrayOutputStream();
            InputStream input = null;

            if (unescapedInput != null) {
                input = new ByteArrayInputStream(unescapedInput.toByteArray());
            }
            if (input != null) {
                int data = 0;
                boolean escapeHot = false;
                StringBuilder logRecord = new StringBuilder();

                // read the values from in the port
                while (running.get() && (data = input.read()) != -1) {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("received data: {}", ByteUtils.byteToHex(data));
                    }
                    // append data to log record
                    logRecord.append(ByteUtils.byteToHex(data)).append(" ");

                    // check if the current is the end of a packet
                    if (data == BidibLibrary.BIDIB_PKT_MAGIC && output.size() > 0) {

                        LOGGER.debug("Received raw message: {}", logRecord);
                        if (MSG_RAW_LOGGER.isInfoEnabled()) {
                            MSG_RAW_LOGGER.info("<< {}", logRecord);
                        }
                        logRecord.setLength(0);

                        // if a CRC error is detected in splitMessages the reading loop will terminate ...
                        super.processMessages(output);
                    }
                    else {
                        if (data == BidibLibrary.BIDIB_PKT_ESCAPE) {
                            escapeHot = true;
                        }
                        else if (data != BidibLibrary.BIDIB_PKT_MAGIC) {
                            if (escapeHot) {
                                data ^= 0x20;
                                escapeHot = false;
                            }
                            output.write((byte) data);
                        }
                    }
                }
                LOGGER.debug("Leaving receive loop, RUNNING: {}", running);

                if (output != null && output.size() > 0) {
                    byte[] remaining = output.toByteArray();
                    String remainingString = ByteUtils.bytesToHex(remaining);
                    LOGGER.warn("Data remaining in output: {}", remainingString);
                }
                if (logRecord != null && logRecord.length() > 0) {
                    LOGGER.warn("Data remaining in logRecord: {}", logRecord);
                }

            }
            else {
                LOGGER.error("No input available.");
            }

        }
        catch (Exception e) {
            LOGGER.warn("Exception detected in message receiver!", e);
            throw new RuntimeException(e);
        }

    }
}
