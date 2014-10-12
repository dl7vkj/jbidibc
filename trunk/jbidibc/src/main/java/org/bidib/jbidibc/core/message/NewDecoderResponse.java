package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewDecoderResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_NEW_DECODER;

    private static final Logger LOGGER = LoggerFactory.getLogger(NewDecoderResponse.class);

    NewDecoderResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("invalid decoder data received");
        }
    }

    public String getName() {
        return "MSG_NEW_DECODER";
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
}
