package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddrChangeAckResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_ADDR_CHANGE_ACK;

    private static final Logger LOGGER = LoggerFactory.getLogger(AddrChangeAckResponse.class);

    AddrChangeAckResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 8) {
            throw new ProtocolException("invalid decoder data received");
        }
    }

    public String getName() {
        return "MSG_ADDR_CHANGE_ACK";
    }

    public int getLocalDetectorAddress() {
        byte[] data = getData();

        return ByteUtils.getInt(data[0]);
    }

    public int getDecoderVendorId() {
        byte[] data = getData();

        return ByteUtils.getInt(data[1]);
    }

    public int getDecoderSerialId() {
        byte[] data = getData();

        int serialId = ByteUtils.convertSerial(data, 2);
        LOGGER.debug("Converted serial id of decoder: {}", serialId);
        return serialId;
    }

    public int getNewAddress() {

        int newAddress = ByteUtils.getInt(getData()[6], getData()[7]);
        LOGGER.debug("The new address is: {}", newAddress);
        return newAddress;
    }
}
