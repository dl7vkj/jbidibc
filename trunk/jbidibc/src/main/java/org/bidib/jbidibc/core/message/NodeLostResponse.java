package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class NodeLostResponse extends NodeTabResponse {
    public static final Integer TYPE = BidibLibrary.MSG_NODE_LOST;

    NodeLostResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
    }

    public NodeLostResponse(byte[] addr, int num, byte version, byte localAddr, long uniqueId) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_NODE_LOST, ByteUtils.concat(new byte[] { version, localAddr },
            ByteUtils.getVidPidFromUniqueId(uniqueId)));
    }

    public String getName() {
        return "MSG_NODE_LOST";
    }
}
