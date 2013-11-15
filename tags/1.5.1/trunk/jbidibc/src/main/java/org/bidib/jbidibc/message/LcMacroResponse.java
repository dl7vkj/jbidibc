package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.MessageUtils;

public class LcMacroResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_LC_MACRO;

    LcMacroResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("no macro received");
        }
    }

    /**
     * @return returns the initialized macro point
     */
    public LcMacro getMacro() {
        byte[] data = getData();
        LcOutputType outputType = LcOutputType.valueOf(data[3]);

        return new LcMacro(data[0], data[1], data[2], outputType, data[4], MessageUtils.toPortStatus(outputType,
            data[5]), MessageUtils.getPortValue(outputType, data[5]));
    }

}
