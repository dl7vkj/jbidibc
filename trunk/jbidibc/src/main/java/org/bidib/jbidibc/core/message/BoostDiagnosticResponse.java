package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.MessageUtils;

public class BoostDiagnosticResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BOOST_DIAGNOSTIC;

    BoostDiagnosticResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("no booster diagnostic");
        }
    }

    public BoostDiagnosticResponse(byte[] addr, int num, int current, int voltage, int temperature)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BOOST_DIAGNOSTIC, new byte[] { BidibLibrary.BIDIB_BST_DIAG_I,
            ByteUtils.getLowByte(current), BidibLibrary.BIDIB_BST_DIAG_V, ByteUtils.getLowByte(voltage),
            BidibLibrary.BIDIB_BST_DIAG_T, ByteUtils.getLowByte(temperature) });
    }

    public String getName() {
        return "MSG_BOOST_DIAGNOSTIC";
    }

    private int convertValue(int value) {
        int result = 0;

        if (value >= 1 && value <= 250) {
            result = value;
        }
        return result;
    }

    public int getCurrent() {
        return MessageUtils.convertCurrent(getValue(BidibLibrary.BIDIB_BST_DIAG_I) & 0xFF);
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
