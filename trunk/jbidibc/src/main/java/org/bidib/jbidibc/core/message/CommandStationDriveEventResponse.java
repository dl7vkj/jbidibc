package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the drive event
 */
public class CommandStationDriveEventResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_DRIVE_EVENT;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveEventResponse.class);

    CommandStationDriveEventResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no command station drive event received");
        }

        LOGGER.debug("Received response, event: {}", getEvent());
    }

    public String getName() {
        return "MSG_CS_DRIVE_EVENT";
    }

    public int getAddress() {
        byte[] data = getData();

        return (data[0] & 0xFF) + ((data[1] & 0xFF) << 8);
    }

    public byte getEvent() {
        return getData()[2];
    }
}
