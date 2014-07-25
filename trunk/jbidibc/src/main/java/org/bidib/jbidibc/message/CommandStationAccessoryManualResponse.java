package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the information of manual accessory change
 */
public class CommandStationAccessoryManualResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_ACCESSORY_MANUAL;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationAccessoryManualResponse.class);

    CommandStationAccessoryManualResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no command station accessory manual received");
        }

        LOGGER.debug("Received manual accessory, aspect: {}", getAspect());
    }

    public String getName() {
        return "MSG_CS_ACCESSORY_MANUAL";
    }

    public int getAddress() {
        byte[] data = getData();

        return ByteUtils.getInt(data[0], data[1]);
    }

    public int getAspect() {
        return ByteUtils.getInt(getData()[2]);
    }
}
