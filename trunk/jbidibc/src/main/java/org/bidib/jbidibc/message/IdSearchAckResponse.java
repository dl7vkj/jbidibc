package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdSearchAckResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_ID_SEARCH_ACK;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdSearchAckResponse.class);

    IdSearchAckResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 11) {
            throw new ProtocolException("invalid decoder data received");
        }
    }

    public String getName() {
        return "MSG_ID_SEARCH_ACK";
    }

    public int getLocalDetectorAddress() {
        byte[] data = getData();

        return ByteUtils.getInt(data[0]);
    }

    public int getSearchVendorId() {
        byte[] data = getData();

        return ByteUtils.getInt(data[1]);
    }

    public int getSearchSerialId() {
        byte[] data = getData();

        int serialId = ByteUtils.convertSerial(data, 2);
        LOGGER.debug("Converted serial id of search: {}", serialId);
        return serialId;
    }

    public int getDecoderVendorId() {
        byte[] data = getData();

        return ByteUtils.getInt(data[6]);
    }

    public int getDecoderSerialId() {
        byte[] data = getData();

        int serialId = ByteUtils.convertSerial(data, 7);
        LOGGER.debug("Converted serial id of decoder: {}", serialId);
        return serialId;
    }
}
