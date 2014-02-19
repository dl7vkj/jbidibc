package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.NodeUtils;
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
