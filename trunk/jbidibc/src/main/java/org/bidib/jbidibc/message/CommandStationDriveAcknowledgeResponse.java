package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.DriveAcknowledge;
import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the drive state
 */
public class CommandStationDriveAcknowledgeResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_DRIVE_ACK;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveAcknowledgeResponse.class);

    CommandStationDriveAcknowledgeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no command station drive acknowledge received");
        }

        LOGGER.debug("Received response, status: {}", getState());
    }

    public CommandStationDriveAcknowledgeResponse(byte[] addr, int num, AddressData decoderAddress, byte acknowledge)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_CS_DRIVE_ACK, prepareData(decoderAddress, acknowledge));
    }

    public String getName() {
        return "MSG_CS_DRIVE_ACK";
    }

    private static byte[] prepareData(AddressData decoderAddress, byte acknowledge) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // write decoder address
        decoderAddress.writeToStream(out);
        // data
        out.write(acknowledge);

        return out.toByteArray();
    }

    public int getAddress() {
        byte[] data = getData();

        return (data[0] & 0xFF) + ((data[1] & 0xFF) << 8);
    }

    public DriveAcknowledge getState() {
        return DriveAcknowledge.valueOf(getData()[2]);
    }
}
