package org.bidib.node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bidib.MessageListener;
import org.bidib.MessageReceiver;
import org.bidib.Node;
import org.bidib.message.BidibMessage;


public class NodeFactory {
    private static final int ROOT_ADDRESS = 0;

    private final Map<Integer, BidibNode> nodes = Collections.synchronizedMap(new HashMap<Integer, BidibNode>());

    public NodeFactory() {
        MessageReceiver.addMessageListener(new MessageListener() {
            @Override
            public void nodeLost(Node node) {
                removeNode(node);
            }

            @Override
            public void nodeNew(Node node) {
                removeNode(node);
            }
        });
    }

    private static int convert(byte[] bytes) {
        int result = 0;

        if (bytes != null) {
            for (int index = 0; index < bytes.length; index++) {
                result += bytes[index] << index;
            }
        }
        return result;
    }

    public AccessoryNode getAccessoryNode(Node node) {
        BidibNode result = getNode(node);

        if (result instanceof AccessoryNode) {
            return (AccessoryNode) result;
        } else {
            return null;
        }
    }

    public BidibNode getNode(Node node) {
        int address = convert(node.getAddr());
        BidibNode result = nodes.get(address);

        if (result == null) {
            int classId = BidibMessage.convertLongToUniqueId(node.getUniqueId())[0];

            if ((classId & 0x01) == 1) {
                result = new AccessoryNode(node.getAddr());
            } else {
                result = new BidibNode(node.getAddr());
            }
            nodes.put(address, result);
        }
        return result;
    }

    public RootNode getRootNode() {
        RootNode result = (RootNode) nodes.get(ROOT_ADDRESS);

        if (result == null) {
            result = new RootNode();
            nodes.put(ROOT_ADDRESS, result);
        }
        return result;
    }

    private void removeNode(Node node) {
        nodes.remove(convert(node.getAddr()));
    }
}
