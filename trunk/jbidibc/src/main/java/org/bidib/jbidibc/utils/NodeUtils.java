package org.bidib.jbidibc.utils;

/**
 * 
 * Unique ID:
 *  1. Byte: Class ID
 *    Bit 7: interface node (has subnodes)
 *    Bit 6: feedback functions
 *    Bit 5: 
 *    Bit 4: command station functions
 *    Bit 3: command station programming functions
 *    Bit 2: accessory functions
 *    Bit 1: booster functions
 *    Bit 0: switch functions
 *
 */
public class NodeUtils {
    // the interface address is always 0
    public static final int INTERFACE_ADDRESS = 0;

    /**
     * Convert a node address into an integer value.
     * All byte parts are converted into an int value by shifting the parts to their position
     * in the int value.
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
     * @param uniqueId the unique id of the node
     * @param pidBits the relevant PID bits
     * @return returns the uniqueId of the node as formatted hex string
     */
    public static String[] getPidAndSerialNumberAsString(long uniqueId, int pidBits) {
        // TODO add support for the pidBits ...
        
        return new String[]{"PID: ???", "Serial: ???"};
    }

    /**
     * @param uniqueId the unique id of the node
     * @return returns the uniqueId of the node as formatted hex string
     */
    public static String getUniqueIdAsString(long uniqueId) {
        return String.format("VID %02X PID %08X", (uniqueId >> 32) & 0xff, uniqueId & 0xffffffffL);
    }
}
