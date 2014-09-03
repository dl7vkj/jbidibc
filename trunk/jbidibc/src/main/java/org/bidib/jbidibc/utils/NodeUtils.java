package org.bidib.jbidibc.utils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Predicate;
import org.bidib.jbidibc.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Unique ID: 1. Byte: Class ID Bit 7: interface node (has subnodes) Bit 6: feedback functions Bit 5: Bit 4: command
 * station functions Bit 3: command station programming functions Bit 2: accessory functions Bit 1: booster functions
 * Bit 0: switch functions
 * 
 */
public class NodeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeUtils.class);

    // the interface address is always 0
    public static final int INTERFACE_ADDRESS = 0;

    /**
     * Convert a node address into an integer value. All byte parts are converted into an int value by shifting the
     * parts to their position in the int value.
     * 
     * @param address
     *            node address
     * 
     * @return integer value for that address
     */
    public static int convertAddress(byte[] address) {
        int result = 0;

        if (address != null) {
            for (int index = 0; index < address.length; index++) {
                result += address[index] << (index * 8);
            }
        }
        return result;
    }

    public static String formatAddress(byte[] address) {
        StringBuffer sb = new StringBuffer();
        for (int index = 0; index < address.length; index++) {
            sb.append(Byte.toString(address[index]));
            if (index < address.length - 1) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * @return returns if node supports accessory functions
     */
    public static boolean hasAccessoryFunctions(long uniqueId) {
        return ((uniqueId >> 48) & 4) == 4;
    }

    /**
     * @return returns if node supports booster functions
     */
    public static boolean hasBoosterFunctions(long uniqueId) {
        return ((uniqueId >> 48) & 2) == 2;
    }

    /**
     * @return returns if node supports feedback functions
     */
    public static boolean hasFeedbackFunctions(long uniqueId) {
        return ((uniqueId >> 48) & 64) == 64;
    }

    /**
     * @return returns if node supports subnodes functions
     */
    public static boolean hasSubNodesFunctions(long uniqueId) {
        return ((uniqueId >> 48) & 128) == 128;
    }

    /**
     * @return returns if node supports switch functions
     */
    public static boolean hasSwitchFunctions(long uniqueId) {
        return ((uniqueId >> 48) & 1) == 1;
    }

    /**
     * @return returns if node supports command station programming functions
     */
    public static boolean hasCommandStationProgrammingFunctions(long uniqueId) {
        return ((uniqueId >> 48) & 8) == 8;
    }

    /**
     * @return returns if node supports command station functions
     */
    public static boolean hasCommandStationFunctions(long uniqueId) {
        return ((uniqueId >> 48) & 16) == 16;
    }

    /**
     * @param uniqueId
     *            the unique id of the node
     * @param pidBits
     *            the relevant PID bits
     * @return returns the uniqueId of the node as formatted hex string
     */
    public static String[] getPidAndSerialNumberAsString(long uniqueId, int pidBits) {
        // TODO add support for the pidBits ...

        return new String[] { "PID: ???", "Serial: ???" };
    }

    /**
     * Return the unique id formatted with the pattern 'VID %02X PID %08X'.
     * 
     * @param uniqueId
     *            the unique id of the node
     * @return returns the uniqueId of the node as formatted hex string
     */
    public static String getUniqueIdAsString(long uniqueId) {
        return String.format("VID %02X PID %08X", (uniqueId >> 32) & 0xff, uniqueId & 0xffffffffL);
    }

    /**
     * @param uniqueId
     *            the unique id of the node
     * @return returns the uniqueId of the node as byte array
     */
    public static byte[] getUniqueId(long uniqueId) {
        byte[] result = new byte[7];
        ByteBuffer bb = ByteBuffer.allocate(8);

        bb.putLong(uniqueId);
        System.arraycopy(bb.array(), 1, result, 0, result.length);
        return result;
    }

    /**
     * @param uniqueId
     *            the unique id of the node as byte array
     * @return returns the uniqueId of the node as long
     */
    public static long getUniqueId(byte[] uniqueId) {
        long result = 0;

        for (int i = 0; i < uniqueId.length; i++) {
            result = (result << 8) + (uniqueId[i] & 0xFF);
        }
        return result;
    }

    /**
     * Get the vendor id from the unique id.
     * 
     * @param uniqueId
     *            the unique id
     * @return the vendor id
     */
    public static long getVendorId(long uniqueId) {
        return (uniqueId >> 32) & 0xff;
    }

    /**
     * Get the product id from the unique id.
     * 
     * @param uniqueId
     *            the unique id
     * @return the product id
     */
    public static long getPid(long uniqueId) {
        return (uniqueId >> 24) & 0xffL;
    }

    /**
     * Find a node by its node address in the provided list of nodes.
     * 
     * @param nodes
     *            the list of nodes
     * @param address
     *            the address of the node to find
     * @return the found node or <code>null</code> if no node was found with the provided node address
     */
    public static Node findNodeByAddress(List<Node> nodes, final byte[] address) {
        Node topNode = org.apache.commons.collections4.CollectionUtils.find(nodes, new Predicate<Node>() {

            @Override
            public boolean evaluate(Node node) {
                if (Arrays.equals(node.getAddr(), address)) {
                    LOGGER.debug("Found node: {}", node);
                    return true;
                }
                return false;
            }
        });
        return topNode;
    }

    public static boolean isSubNode(Node parent, Node node) {
        byte[] parentAddress = parent.getAddr();
        byte[] nodeAddress = node.getAddr();

        // check if the nodes are equal
        if (Arrays.equals(parentAddress, nodeAddress)) {
            return false;
        }

        if (parentAddress.length == 1 && parentAddress[0] == 0) {
            // interface node needs special processing
            parentAddress = new byte[0];
        }

        if (nodeAddress.length == (parentAddress.length + 1)) {
            byte[] compareAddress = new byte[parentAddress.length];
            System.arraycopy(nodeAddress, 0, compareAddress, 0, parentAddress.length);

            if (Arrays.equals(parentAddress, compareAddress)) {
                LOGGER.debug("The current node is a subnode: {}, parent: {}", node, parent);
                return true;
            }
        }
        return false;
    }
}
