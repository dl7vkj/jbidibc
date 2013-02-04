package org.bidib.jbidibc.message;

import org.bidib.jbidibc.enumeration.DriveAcknowledge;
import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Response from command station with the drive state
 */
public class CommandStationDriveAcknowledgeResponse extends BidibMessage {
    CommandStationDriveAcknowledgeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no command station drive acknowledge received");
        }
    }

    public int getAddress() {
        byte[] data = getData();

        return (data[0] & 0xFF) + ((data[1] & 0xFF) << 8);
    }

    public DriveAcknowledge getState() {
        return DriveAcknowledge.valueOf(getData()[2]);
    }
}
