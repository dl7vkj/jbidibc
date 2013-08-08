package org.bidib.jbidibc.node;

import java.util.Calendar;

import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.NodeChangedAckMessage;
import org.bidib.jbidibc.message.SysClockMessage;

/**
 * This class represents the interface node in the BiDiB system.
 *
 */
public class RootNode extends BidibNode {
    
    public static final byte[] ROOTNODE_ADDR = new byte[]{0,0,0,0};

    RootNode(MessageReceiver messageReceiver) {
        super(new byte[] { 0 }, messageReceiver);
    }

    /**
     * Send the node changed acknowledge message.
     * @param versionNumber the version number of the node table
     * @throws ProtocolException
     */
    public void acknowledgeNodeChanged(int versionNumber) throws ProtocolException {
        sendNoWait(new NodeChangedAckMessage(versionNumber));
    }

    /**
     * Send the sys clock message.
     * @param date the current date
     * @param factor the time factor
     * @throws ProtocolException
     */
    public void clock(Calendar date, int factor) throws ProtocolException {
        sendNoWait(new SysClockMessage(date, factor));
    }

    /**
     * Send the system reset message to the node.
     * @throws ProtocolException
     */
    public void reset() throws ProtocolException {
        super.reset();
        // TODO reset the counters

    }

}
