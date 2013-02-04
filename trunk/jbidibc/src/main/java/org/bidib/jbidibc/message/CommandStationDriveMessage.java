package org.bidib.jbidibc.message;

import java.util.BitSet;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.SpeedSteps;
import org.bidib.jbidibc.utils.ByteUtils;

public class CommandStationDriveMessage extends BidibMessage {
    public CommandStationDriveMessage(int address, SpeedSteps speedSteps, Integer speed, BitSet activeFunctions,
            BitSet functions) {
        super(0, BidibLibrary.MSG_CS_DRIVE, ByteUtils.concat(new byte[] { (byte) (address & 0xFF),
                (byte) ((address & 0xFF00) >> 8), speedSteps.getType(), getActiveBits(speed, activeFunctions),
                speed != null ? speed.byteValue() : 0 }, convertFunctions(functions)));
    }

    private static byte getActiveBits(Integer speed, BitSet activeFunctions) {
        byte result = 0;

        if (speed != null) {
            result |= 1;
        }
        if (activeFunctions != null) {
            if (activeFunctions.get(0)) {
                result |= 2;
            }
            if (activeFunctions.get(1)) {
                result |= 4;
            }
            if (activeFunctions.get(2)) {
                result |= 8;
            }
            if (activeFunctions.get(3)) {
                result |= 16;
            }
        }
        return result;
    }

    private static byte[] convertFunctions(BitSet bits) {
        byte[] result = new byte[4];
        int bitIndex = 0;

        if (bits != null) {
            while (bitIndex < bits.length()) {
                if (bitIndex == 0) {
                    result[4] = convert(bits.get(bitIndex));
                } else if (bitIndex < 5) {
                    result[bitIndex - 1] = convert(bits.get(bitIndex));
                } else {
                    result[bitIndex + 3] = convert(bits.get(bitIndex));
                }
            }
        }
        return result;
    }

    private static byte convert(boolean value) {
        return value ? (byte) 1 : (byte) 0;
    }
}
