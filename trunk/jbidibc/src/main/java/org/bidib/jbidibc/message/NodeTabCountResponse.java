package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class NodeTabCountResponse extends BidibMessage {
	public static final int TYPE = BidibLibrary.MSG_NODETAB_COUNT;
	
    NodeTabCountResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no node tab count received");
        }
    }

    public int getCount() {
        return getData()[0];
    }
}
