package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

/**
 * This response is sent as direct response to a request that takes some time to execute. The final execution state will
 * be signaled by a LcStatResponse message.
 * 
 */
public class LcWaitResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_LC_WAIT;

    LcWaitResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no lc wait received");
        }
    }

    public String getName() {
        return "MSG_LC_WAIT";
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public int getPortNumber() {
        int result = ByteUtils.getInt(getData()[1]);
        return result;
    }

    public int getPredictedRotationTime() {
        int result = ByteUtils.getInt(getData()[2]);

        return result > 127 ? result * 1000 : result * 100;
    }
}
