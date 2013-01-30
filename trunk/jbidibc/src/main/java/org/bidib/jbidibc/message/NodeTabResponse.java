package org.bidib.jbidibc.message;

import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class NodeTabResponse extends BidibMessage {
    NodeTabResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 9) {
            throw new ProtocolException("no node tab received");
        }
    }

    public Node getNode(byte[] parentAddress) {
        byte[] data = getData();
        byte[] addr = new byte[parentAddress.length + 1];
        byte[] uniqueId = new byte[7];

        System.arraycopy(parentAddress, 0, addr, 1, parentAddress.length);
        addr[0] = data[1];
        System.arraycopy(data, 2, uniqueId, 0, uniqueId.length);
        return new Node(data[0], addr, ByteUtils.convertUniqueIdToLong(uniqueId));
    }
}
