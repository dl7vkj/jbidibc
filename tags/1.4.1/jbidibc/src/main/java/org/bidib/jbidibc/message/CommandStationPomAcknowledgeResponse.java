package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.PomAcknowledge;
import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the pom acknowledge state
 */
public class CommandStationPomAcknowledgeResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_DRIVE_ACK;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationPomAcknowledgeResponse.class);

    CommandStationPomAcknowledgeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("no command station accessory acknowledge received");
        }

        LOGGER.debug("Received response, acknowledge status: {}", getAcknState());
    }

    public int getAddress() {
        byte[] data = getData();

        return (data[0] & 0xFF) + ((data[1] & 0xFF) << 8);
    }

    public PomAcknowledge getAcknState() {
        return PomAcknowledge.valueOf(getData()[5]);
    }
}
