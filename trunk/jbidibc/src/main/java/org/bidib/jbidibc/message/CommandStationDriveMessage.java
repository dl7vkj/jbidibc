package org.bidib.jbidibc.message;

import java.util.BitSet;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.SpeedStepsEnum;
import org.bidib.jbidibc.utils.ByteUtils;

public class CommandStationDriveMessage extends BidibMessage {
    public CommandStationDriveMessage(int address, SpeedStepsEnum speedSteps, Integer speed, BitSet activeFunctions,
        BitSet functions) {
        super(0, BidibLibrary.MSG_CS_DRIVE, ByteUtils.concat(new byte[] { (byte) (address & 0xFF),
            (byte) ((address & 0xFF00) >> 8), speedSteps.getType(), getActiveBits(speed, activeFunctions),
            getSpeed(speed) }, convertFunctions(functions)));
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

    private static byte convert(boolean value) {
        return value ? (byte) 1 : (byte) 0;
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

    private static byte getSpeed(Integer speed) {
        byte result = 0;

        if (speed != null) {
            if (speed > 0) {
                result = speed.byteValue();
            }
            else {
                result = (byte) (Math.abs(speed) | 0x80);
            }
        }
        return result;
    }
}
