package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
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

        LOGGER.info("Number of nodes in system: {}", getCount());
    }

    public int getCount() {
        return getData()[0];
    }
}
