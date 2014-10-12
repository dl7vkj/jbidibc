package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VendorEnableMessage extends BidibCommandMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorEnableMessage.class);

    public VendorEnableMessage(long uniqueId) {
        super(0, BidibLibrary.MSG_VENDOR_ENABLE, NodeUtils.getUniqueId(uniqueId));
    }

    public VendorEnableMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_VENDOR_ENABLE";
    }

    public long getUniqueId() {
        byte[] uniqueId = new byte[7];
        System.arraycopy(getData(), 0, uniqueId, 0, uniqueId.length);

        long uniqueIdentifier = ByteUtils.convertUniqueIdToLong(uniqueId);
        LOGGER.info("Return the uniqueId: {}", uniqueIdentifier);
        return uniqueIdentifier;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { VendorAckResponse.TYPE };
    }
}
