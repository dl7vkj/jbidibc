package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

/**
 * This response is sent as direct response to a request that takes some time to execute. The
 * final execution state will be signaled by a LcStatResponse message.
 *
 */
public class LcWaitResponse extends BidibMessage {
    LcWaitResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no lc wait received");
        }
    }

    public int getPortType() {
        int result = getData()[0];
        return result;
    }

    public int getPortNumber() {
        int result = getData()[1];
        return result;
    }

    public int getTimeout() {
        int result = getData()[2] & 0xFF;

        return result > 127 ? result * 1000 : result * 100;
    }
}
