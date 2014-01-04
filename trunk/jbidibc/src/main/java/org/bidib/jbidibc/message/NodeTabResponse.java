package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeTabResponse extends BidibMessage {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static final Integer TYPE = BidibLibrary.MSG_NODETAB;

    NodeTabResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 9) {
            throw new ProtocolException("No valid node tab received");
        }
    }

    public NodeTabResponse(byte[] addr, int num, byte version, byte localAddr, long uniqueId) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_NODETAB, ByteUtils.concat(new byte[] { version, localAddr }, ByteUtils
            .getVidPidFromUniqueId(uniqueId)));
    }

    /**
     * Create a new node based on the parent address and the received data.
     * @param parentAddress the parent address
     * @return the node
     */
    public Node getNode(byte[] parentAddress) {
        //        LOGGER.debug("Create new node with parent address: {}", parentAddress);
        byte[] data = getData();
        byte[] addr = new byte[parentAddress.length + 1];
        byte[] uniqueId = new byte[7];

        LOGGER.info("Create new node with parent address: {}, current local address: {}", parentAddress, data[1]);

        if (parentAddress.length == 1 && parentAddress[0] == 0) {
            // the parent is the interface node
            addr = new byte[1];
            addr[0] = data[1];
        }
        else if (data[1] == 0) {
            // we have the node itself
            addr = new byte[parentAddress.length];
            System.arraycopy(parentAddress, 0, addr, 0, parentAddress.length);
        }
        else {
            // add the local address of the subnode to the parent address
            System.arraycopy(parentAddress, 0, addr, 0, parentAddress.length);
            addr[parentAddress.length] = data[1];
        }
        System.arraycopy(data, 2, uniqueId, 0, uniqueId.length);

        // create the new node with the received data
        Node node = new Node(ByteUtils.getInt(data[0]), addr, ByteUtils.convertUniqueIdToLong(uniqueId));
        LOGGER.info("Created node from received data: {}", node);
        return node;
    }
}
