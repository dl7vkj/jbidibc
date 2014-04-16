package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddrChangeAckResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_NEW_DECODER;

    private static final Logger LOGGER = LoggerFactory.getLogger(AddrChangeAckResponse.class);

    AddrChangeAckResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 8) {
            throw new ProtocolException("invalid decoder data received");
        }
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
