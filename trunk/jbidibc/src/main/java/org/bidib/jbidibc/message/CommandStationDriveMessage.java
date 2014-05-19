package org.bidib.jbidibc.message;

import java.util.BitSet;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.enumeration.DirectionEnum;
import org.bidib.jbidibc.enumeration.SpeedStepsEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandStationDriveMessage extends BidibCommandMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveMessage.class);

    public CommandStationDriveMessage(int address, SpeedStepsEnum speedSteps, Integer speed, DirectionEnum direction,
        BitSet activeFunctions, BitSet functions) {
        super(0, BidibLibrary.MSG_CS_DRIVE, ByteUtils.concat(new byte[] { (byte) (address & 0xFF),
            (byte) ((address & 0xFF00) >> 8), speedSteps.getType(), getActiveBits(speed, activeFunctions),
            convertSpeed(speed, direction) }, convertFunctions(functions)));
    }

    public CommandStationDriveMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    private static byte convert(boolean value) {
        return value ? (byte) 1 : (byte) 0;
    }

    private static byte[] convertFunctions(BitSet bits) {
        byte[] result = new byte[4];

        if (bits != null) {
            for (int bitIndex = 0; bitIndex < bits.length(); bitIndex++) {
                if (bitIndex == 0) {
                    result[0] |= (convert(bits.get(bitIndex)) << 4);
                }
                else if (bitIndex < 5) {
                    result[0] |= (convert(bits.get(bitIndex)) << (bitIndex - 1));
                }
                else if (bitIndex < 13) {
                    result[1] |= (convert(bits.get(bitIndex)) << (bitIndex - 5));
                }
                else if (bitIndex < 21) {
                    result[2] |= (convert(bits.get(bitIndex)) << (bitIndex - 13));
                }
                else {
                    result[3] |= (convert(bits.get(bitIndex)) << (bitIndex - 21));
                }
            }
        }
        return result;
    }

    private static byte convertSpeed(Integer speed, DirectionEnum direction) {
        byte result = 0;

        if (speed != null) {
            result = speed.byteValue();
        }
        if (direction == DirectionEnum.FORWARD) {
            result |= 0x80;
        }
        return result;
    }

    private static byte getActiveBits(Integer speed, BitSet activeFunctions) {
        byte result = 0;

        if (speed != null) {
            result |= 1;
        }
        if (activeFunctions != null) {
            // F0 .. F4
            if (activeFunctions.get(0)) {
                result |= 2;
            }
            // F5 .. F12
            if (activeFunctions.get(1)) {
                result |= 4;
            }
            // F13 .. F20
            if (activeFunctions.get(2)) {
                result |= 8;
            }
            // F21 .. F28
            if (activeFunctions.get(3)) {
                result |= 16;
            }
        }
        return result;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { CommandStationDriveAcknowledgeResponse.TYPE };
    }

    public AddressData getDecoderAddress() {
        int index = 0;
        byte lowByte = getData()[index++];
        byte highByte = getData()[index++];
        int address = ByteUtils.getWord(lowByte, highByte);

        index += 3;

        byte data2 = getData()[index];
        AddressTypeEnum addressTypeEnum = AddressTypeEnum.LOCOMOTIVE_BACKWARD;
        if ((data2 & 0x80) == 0x80) {
            addressTypeEnum = AddressTypeEnum.LOCOMOTIVE_FORWARD;
        }
        AddressData addressData = new AddressData(address, addressTypeEnum);
        LOGGER.debug("Prepared address data: {}", addressData);
        return addressData;
    }

}
