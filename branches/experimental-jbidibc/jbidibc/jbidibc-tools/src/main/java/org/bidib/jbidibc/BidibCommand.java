package org.bidib.jbidibc;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.node.BidibNode;

public class BidibCommand {
    protected static Node findNode(long uniqueId) throws IOException, ProtocolException, InterruptedException {
        Node result = null;
        BidibNode rootNode = Bidib.getRootNode();
        int count = rootNode.getNodeCount();

        for (int index = 1; index <= count; index++) {
            Node node = rootNode.getNextNode();

            if ((node.getUniqueId() & 0xffffffffffffffL) == uniqueId) {
                Bidib.getNode(node).getMagic();
                result = node;
                break;
            }
        }
        return result;
    }

    protected static byte[] getUniqueId(long uniqueId) {
        byte[] result = new byte[7];
        ByteBuffer bb = ByteBuffer.allocate(8);

        bb.putLong(uniqueId);
        System.arraycopy(bb.array(), 1, result, 0, result.length);
        return result;
    }
}
