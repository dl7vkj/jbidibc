package org.bidib.jbidibc.node;

import java.util.Calendar;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.exception.NoAnswerException;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.SysClockMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the interface node in the BiDiB system.
 * 
 */
public class RootNode extends BidibNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(RootNode.class);

    public static final byte[] ROOTNODE_ADDR = new byte[] { 0 };

    RootNode(MessageReceiver messageReceiver, boolean ignoreWaitTimeout) {
        super(new byte[] { 0 }, messageReceiver, ignoreWaitTimeout);
    }

    /**
     * Send the sys clock message.
     * 
     * @param date
     *            the current date
     * @param factor
     *            the time factor
     * @throws ProtocolException
     */
    public void clock(Calendar date, int factor) throws ProtocolException {
        sendNoWait(new SysClockMessage(date, factor));
    }

    /**
     * Send the system reset message to the node.
     * 
     * @throws ProtocolException
     */
    public void reset() throws ProtocolException {
        super.reset();
        // TODO reset the counters

    }

    @Override
    public int getMagic() throws ProtocolException {
        LOGGER.trace("Get magic from root node!");
        int magic = super.getMagic();
        LOGGER.debug("Get magic from root node returns: {}", magic);
        if (BIDIB_MAGIC_UNKNOWN == magic || BidibLibrary.BIDIB_BOOT_MAGIC == magic) {
            LOGGER.warn("The interface did not respond the get magic request!");

            StringBuffer sb = new StringBuffer("The interface did not respond the get magic request!");

            String errorInfo = getMessageReceiver().getErrorInformation();
            if (errorInfo != null) {
                String buffer = new String(errorInfo);
                LOGGER.warn("Found received data that was not identifed as BiDiB messages: {}", buffer);

                sb.append("\r\n");
                sb.append(buffer);
            }

            NoAnswerException ex = new NoAnswerException("Establish communication with interface failed.");
            ex.setDescription(sb.toString());
            throw ex;
        }
        return magic;
    }

}
