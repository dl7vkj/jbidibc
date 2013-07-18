/**
 * 
 */
package org.bidib.jbidibc.utils;

import java.math.BigInteger;

/**
 * This class contains utility functions for byte conversations.
 */
public final class ByteUtils {
    private ByteUtils() {
    }

    public static int getWord(byte lowByte, byte highByte) {
        return ((highByte & 0x3F) << 8) + (lowByte & 0xFF);
    }

    /**
     * Get the (unsigned) int value from the low and high byte value.
     * @param lowByte the low byte
     * @param highByte the high byte
     * @return the combined int value
     */
    public static int getInt(byte lowByte, byte highByte) {
        return ((highByte & 0xFF) << 8) + (lowByte & 0xFF);
    }

    /**
     * Get the (unsigned) int value from the byte value.
     * @param byteValue the byte value
     * @return the int value
     */
    public static int getInt(byte byteValue) {
        return (byteValue & 0xFF);
    }

    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];

        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static byte[] convertLongToUniqueId(long uniqueId) {
        return BigInteger.valueOf(uniqueId).toByteArray();
    }

    public static long convertUniqueIdToLong(byte[] uniqueId) {
        return new BigInteger(uniqueId).longValue();
    }

    public static String toString(byte[] bytes) {
        StringBuffer result = new StringBuffer("[");

        if (bytes != null) {
            for (byte b : bytes) {
                if (result.length() > 1) {
                    result.append(", ");
                }
                result.append(b & 0xFF);
            }
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Compare two byte arrays.
     * @param a1 the first byte array
     * @param a2 the second byte array
     * @return true if both arrays are equal
     */
    public static boolean arrayEquals(byte[] a1, byte[] a2) {
        if (a1.length != a2.length) {
            return false;
        }
        for (int index = 0; index < a1.length; index++) {
            if (a1[index] != a2[index]) {
                return false;
            }
        }
        return true;
    }
}
