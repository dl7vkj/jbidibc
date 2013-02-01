package org.bidib.jbidibc.node;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactory.class);

    private static final int ROOT_ADDRESS = 0;

    private final Map<Integer, BidibNode> nodes = Collections.synchronizedMap(new HashMap<Integer, BidibNode>());

    public NodeFactory() {
        MessageReceiver.addMessageListener(new MessageListener() {
            @Override
            public void address(byte[] address, int detectorNumber, Collection<AddressData> addressData) {
            }

            @Override
            public void boosterCurrent(byte[] address, int current) {
            	LOGGER.info("booster current has changed: {}, address: {}", current, address);
            }

            @Override
            public void boosterState(byte[] address, BoosterState state) {
            }

            @Override
            public void confidence(byte[] address, int valid, int freeze, int signal) {
            }

            @Override
            public void free(byte[] address, int detectorNumber) {
            }

            @Override
            public void key(byte[] address, int keyNumber, int keyState) {
            }

            @Override
            public void nodeLost(Node node) {
                removeNode(node);
            }

            @Override
            public void nodeNew(Node node) {
                removeNode(node);
            }

            @Override
            public void occupied(byte[] address, int detectorNumber) {
            }

            @Override
            public void speed(byte[] address, AddressData addressData, int speed) {
            }
        });
    }

    /**
     * Convert a node address into an integer value.
     * 
     * @param address
     *            node address
     * 
     * @return integer value for that address
     */
    public static int convert(byte[] address) {
        int result = 0;

        if (address != null) {
            for (int index = 0; index < address.length; index++) {
                result += address[index] << index;
            }
        }
        return result;
    }

    public AccessoryNode getAccessoryNode(Node node) {
        BidibNode result = getNode(node);

        if (result instanceof AccessoryNode) {
            return (AccessoryNode) result;
        }
        LOGGER.debug("The requested node is not an AccessoryNode.");
        return null;
    }

    public CommandStationNode getCommandStationNode(Node node) {
        BidibNode result = getNode(node);

        if (result instanceof CommandStationNode) {
            return (CommandStationNode) result;
        } 
        LOGGER.debug("The requested node is not an CommandStationNode.");
        return null;
    }

    public BidibNode getNode(Node node) {
        int address = convert(node.getAddr());
        BidibNode result = nodes.get(address);

        if (result == null) {
            int classId = ByteUtils.convertLongToUniqueId(node.getUniqueId())[0];

            // create the new node based on the class id
            if ((classId & 0x01) == 1) {
                result = new AccessoryNode(node.getAddr());
            } 
            else if ((classId & 0x08) == 1) {
                result = new CommandStationNode(node.getAddr());
            } 
            else {
                result = new BidibNode(node.getAddr());
            }
        	LOGGER.info("Create new node: {}, address: {}", result, address);
        	
            nodes.put(address, result);
        }
        return result;
    }

    /**
     * @return returns the root node
     */
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
