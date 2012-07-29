package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class NodeLostResponse extends NodeTabResponse {
    NodeLostResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
    }
}
