package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcMacroResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcMacroResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_LC_MACRO;

    LcMacroResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("no macro received");
        }
    }

    public LcMacroResponse(byte[] addr, int num, byte macroNum, byte step, LcMacro lcMacro) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_MACRO, new byte[] { macroNum, step, lcMacro.getDelay(),
            lcMacro.getOutputType() != null ? lcMacro.getOutputType().getType() : 0, lcMacro.getOutputNumber(),
            lcMacro.getStatus() != null ? lcMacro.getStatus().getType() : 0 });
    }

    public String getName() {
        return "MSG_LC_MACRO";
    }

    /**
     * @return returns the initialized macro point
     */
    public LcMacro getMacro() {
        byte[] data = getData();

        LOGGER.info("Returned macro data: {}", ByteUtils.bytesToHex(data));

        // TODO change this behavior to propagate the problem
        try {
            LcOutputType outputType = LcOutputType.valueOf(data[3]);

            return new LcMacro(data[0], data[1], data[2], outputType, data[4], MessageUtils.toPortStatus(outputType,
                data[5]), MessageUtils.getPortValue(outputType, data[5]));
        }
        catch (Exception ex) {
            LOGGER.warn("Convert macro data to LcMacro failed.", ex);
            return null;
        }
    }

}
