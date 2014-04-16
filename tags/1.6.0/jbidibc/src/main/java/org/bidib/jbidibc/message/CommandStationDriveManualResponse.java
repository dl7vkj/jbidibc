package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.DriveState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the information of manual drive
 */
public class CommandStationDriveManualResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_DRIVE_MANUAL;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveManualResponse.class);

    CommandStationDriveManualResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 9) {
            throw new ProtocolException("no command station drive manual received");
        }

        LOGGER.debug("Received response, speed: {}", getSpeed());
    }

    public int getAddress() {
        byte[] data = getData();

        return (data[0] & 0xFF) + ((data[1] & 0xFF) << 8);
    }

    public byte getFormat() {
        return getData()[2];
    }

    public byte getOutputActive() {
        return getData()[3];
    }

    public int getSpeed() {
        return ByteUtils.getInt(getData()[4]);
    }

    public byte getLights() {
        return (byte) (getData()[5] & 0x10);
    }

    public DriveState getDriveState() {
        DriveState driveState =
            new DriveState(getAddress(), ByteUtils.getInt(getData()[2]), ByteUtils.getInt(getData()[3]), getSpeed(),
                ByteUtils.getInt(getLights()));
        return driveState;
    }
}
