package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeTabCountResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeTabCountResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_NODETAB_COUNT;

    NodeTabCountResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no node tab count received");
        }

        LOGGER.debug("Number of nodes in system: {}", getCount());
    }

    public NodeTabCountResponse(byte[] addr, int num, byte tabCount) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_NODETAB_COUNT, tabCount);
    }

    public String getName() {
        return "MSG_NODETAB_COUNT";
    }

    public int getCount() {
        return ByteUtils.getInt(getData()[0]);
    }
}
