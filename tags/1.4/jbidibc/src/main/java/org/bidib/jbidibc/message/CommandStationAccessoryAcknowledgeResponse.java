package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.AccessoryAcknowledge;
import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the accessory acknowledge state
 */
public class CommandStationAccessoryAcknowledgeResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_DRIVE_ACK;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationAccessoryAcknowledgeResponse.class);

    CommandStationAccessoryAcknowledgeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no command station accessory acknowledge received");
        }

        LOGGER.debug("Received response, acknowledge status: {}", getAcknState());
    }

    public int getAddress() {
        byte[] data = getData();

        return (data[0] & 0xFF) + ((data[1] & 0xFF) << 8);
    }

    public AccessoryAcknowledge getAcknState() {
        return AccessoryAcknowledge.valueOf(getData()[2]);
    }
}
