/**
 * 
 */
package org.bidib.jbidibc.utils;

import java.math.BigInteger;

/**
 * This class contains utility functions for byte conversations.
 */
public class ByteUtils {

    public static int getWord(byte lowByte, byte highByte) {
        return ((highByte & 0x3F) << 8) + (lowByte & 0xFF);
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
    
}
