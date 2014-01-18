package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.DecoderIdAddressData;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.enumeration.CommandStationPom;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

/**
 * Command to send programming commands for 'programming on mains'
 */
public class CommandStationPomMessage extends BidibMessage {
    /**
     * Create the command for a loco decoder.
     * @param decoderAddress the decoder address
     */
    public CommandStationPomMessage(AddressData decoderAddress, CommandStationPom opCode, int cvNumber, byte data) {
        super(0, BidibLibrary.MSG_CS_POM, prepareData(decoderAddress, opCode, cvNumber, data));
    }

    /**
     * Create the command for a loco decoder.
     * @param decoderAddress the decoder address
     * @param addressDID the DID address
     */
    public CommandStationPomMessage(DecoderIdAddressData addressDID, CommandStationPom opCode, int cvNumber, byte data) {
        super(0, BidibLibrary.MSG_CS_POM, prepareData(addressDID, opCode, cvNumber, data));
    }

    public CommandStationPomMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    private static byte[] prepareData(AddressData decoderAddress, CommandStationPom opCode, int cvNumber, byte data) {

        if (cvNumber < 1 || cvNumber > 1024) {
            throw new IllegalArgumentException("CV number is out of allowed range (1..1024).");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // write decoder address
        decoderAddress.writeToStream(out);
        // no ADDR_XL and ADDR_XH
        out.write((byte) 0);
        out.write((byte) 0);
        // no MID
        out.write((byte) 0);
        // op code
        out.write(opCode.getType());
        // CV number
        out.write(ByteUtils.getLowByte(cvNumber - 1));
        out.write(ByteUtils.getHighByte(cvNumber - 1));
        // no XPOM
        out.write((byte) 0);
        // data
        out.write(data);

        return out.toByteArray();
    }

    private static byte[] prepareData(DecoderIdAddressData addressDID, CommandStationPom opCode, int cvNumber, byte data) {

        if (cvNumber < 1 || cvNumber > 1024) {
            throw new IllegalArgumentException("CV number is out of allowed range (1..1024).");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // write did address
        addressDID.writeToStream(out);
        // no MID
        out.write(addressDID.getManufacturedId());
        // op code
        out.write(opCode.getType());
        // CV number
        out.write(ByteUtils.getLowByte(cvNumber - 1));
        out.write(ByteUtils.getHighByte(cvNumber - 1));
        // no XPOM
        out.write((byte) 0);
        // data
        out.write(data);

        return out.toByteArray();
    }

    public AddressData getDecoderAddress() {
        int index = 0;
        byte lowByte = getData()[index++];
        byte highByte = getData()[index++];
        int address = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));

        AddressData addressData = new AddressData(address, AddressTypeEnum.valueOf((byte) ((highByte & 0xC0) >> 6)));
        return addressData;
    }
}
