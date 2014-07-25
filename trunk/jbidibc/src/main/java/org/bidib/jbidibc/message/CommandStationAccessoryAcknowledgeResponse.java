package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.AccessoryAcknowledge;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the accessory acknowledge state
 */
public class CommandStationAccessoryAcknowledgeResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_ACCESSORY_ACK;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationAccessoryAcknowledgeResponse.class);

    CommandStationAccessoryAcknowledgeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no command station accessory acknowledge received");
        }

        LOGGER.debug("Received response, acknowledge status: {}", getAcknState());
    }

    public CommandStationAccessoryAcknowledgeResponse(byte[] addr, int num, AddressData decoderAddress, byte acknowledge)
        throws ProtocolException {
        this(addr, num, TYPE, prepareData(decoderAddress, acknowledge));
    }

    public String getName() {
        return "MSG_CS_ACCESSORY_ACK";
    }

    private static byte[] prepareData(AddressData decoderAddress, byte acknowledge) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // write decoder address
        out.write(ByteUtils.getLowByte(decoderAddress.getAddress()));
        out.write(ByteUtils.getHighByte(decoderAddress.getAddress()));
        // data
        out.write(acknowledge);

        return out.toByteArray();
    }

    public int getAddress() {
        byte[] data = getData();

        return ByteUtils.getInt(data[0], data[1]);
    }

    public AccessoryAcknowledge getAcknState() {
        return AccessoryAcknowledge.valueOf(getData()[2]);
    }
}
