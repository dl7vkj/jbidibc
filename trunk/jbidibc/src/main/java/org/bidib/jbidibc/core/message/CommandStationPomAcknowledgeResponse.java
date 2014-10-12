package org.bidib.jbidibc.core.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.core.AddressData;
import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.core.enumeration.PomAcknowledge;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the pom acknowledge state
 */
public class CommandStationPomAcknowledgeResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_POM_ACK;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationPomAcknowledgeResponse.class);

    CommandStationPomAcknowledgeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("no command station pom acknowledge received");
        }

        LOGGER.debug("Received response, acknowledge status: {}", getAcknState());
    }

    public CommandStationPomAcknowledgeResponse(byte[] addr, int num, AddressData decoderAddress, byte acknowledge)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_CS_POM_ACK, prepareData(decoderAddress, acknowledge));
    }

    public String getName() {
        return "MSG_CS_POM_ACK";
    }

    private static byte[] prepareData(AddressData decoderAddress, byte acknowledge) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // write decoder address
        decoderAddress.writeToStream(out);
        // no ADDR_XL and ADDR_XH
        out.write((byte) 0);
        out.write((byte) 0);
        // no MID
        out.write((byte) 0);
        // data
        out.write(acknowledge);

        return out.toByteArray();
    }

    public AddressData getAddress() {
        int index = 0;
        byte lowByte = getData()[index++];
        byte highByte = getData()[index++];
        int address = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));

        AddressData addressData = new AddressData(address, AddressTypeEnum.valueOf((byte) ((highByte & 0xC0) >> 6)));
        return addressData;
    }

    public int getAddressX() {
        byte[] data = getData();

        return ByteUtils.getInt(data[2], data[3]);
    }

    public int getMID() {
        return ByteUtils.getInt(getData()[4]);
    }

    public PomAcknowledge getAcknState() {
        return PomAcknowledge.valueOf(getData()[5]);
    }
}
