package org.bidib.jbidibc.message;

import java.util.BitSet;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.SpeedSteps;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandStationDriveMessage extends BidibMessage {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveMessage.class);
	
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
        	LOGGER.debug("Prepared bits: {}", bits);

        	// TODO this prepared the first byte correct but didn't put on the lights
//        	while (bitIndex < bits.length()) {
//        		if (bitIndex < 8) {
//        			byte current = result[0];
//        			for (;bitIndex < 8; bitIndex++ ) {
//        				current |= (convert(bits.get(bitIndex)) << bitIndex);
//        				
//        				LOGGER.debug("Prepared current: {}", current);
//        			}
//        			result[0] = current;
//        		}
//        		bitIndex++;
//        	}

        	// TODO this cause an AIOOBEx
        	while (bitIndex < bits.length()) {
                if (bitIndex == 0) {
                	boolean bit = bits.get(bitIndex);
                    result[4] = convert(bit/*bits.get(bitIndex)*/);
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
