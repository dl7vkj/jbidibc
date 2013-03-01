package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class BoostDiagnosticResponse extends BidibMessage {
    BoostDiagnosticResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("no booster diagnostic");
        }
    }

    public static int convertCurrent(int current) {
        if (current >= 1 && current <= 15) {
        }
        else if (current >= 16 && current <= 63) {
            current = (current - 12) * 4;
        }
        else if (current >= 64 && current <= 127) {
            current = (current - 51) * 16;
        }
        else if (current >= 128 && current <= 191) {
            current = (current - 108) * 64;
        }
        else if (current >= 192 && current <= 250) {
            current = (current - 171) * 256;
        }
        else {
            current = 0;
        }
        return current;
    }

    private int convertValue(int value) {
        int result = 0;

        if (value >= 1 && value <= 250) {
            result = value;
        }
        return result;
    }

    public int getCurrent() {
        return convertCurrent(getValue(BidibLibrary.BIDIB_BST_DIAG_I) & 0xFF);
    }

    public int getTemperature() {
        return convertValue(getValue(BidibLibrary.BIDIB_BST_DIAG_T) & 0xFF);
    }

    private byte getValue(int type) {
        byte result = 0;
        byte[] data = getData();

        for (int index = 0; index < data.length - 1; index += 2) {
            if (data[index] == type) {
                result = data[index + 1];
                break;
            }
        }
        return result;
    }

    public int getVoltage() {
        return convertValue(getValue(BidibLibrary.BIDIB_BST_DIAG_V) & 0xFF);
    }
}
