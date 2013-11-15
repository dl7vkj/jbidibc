/**
 * 
 */
package org.bidib.jbidibc.utils;

import java.math.BigInteger;

/**
 * This class contains utility functions for byte conversations.
 */
public final class ByteUtils {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

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

    /**
     * Returns the low byte of an int value.
     * @param value the value
     * @return the low byte
     */
    public static byte getLowByte(int value) {
        byte lowByte = (byte) (value & 0xFF);
        return lowByte;
    }

    /**
     * Returns the high byte of an int value.
     * @param value the value
     * @return the high byte
     */
    public static byte getHighByte(int value) {
        byte penultimateByte = (byte) ((value >> 8) & 0xFF);
        return penultimateByte;
    }

    /**
     * Converts the serial number to an int value.
     * @param serialData the serial number
     * @param offset the offset to read the data
     * @return the serial number
     */
    public static int convertSerial(byte[] serialData, int offset) {
        int result = 0;

        if (serialData != null) {
            for (int index = 0; index < 4; index++) {
                result += (serialData[index + offset] & 0xFF) << (index * 8);
            }
        }
        return result;
    }

    /**
     * Convert a byte array to a hex string.
     * Original source: http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
     * @param bytes the byte array
     * @return the formatted hex string
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static String byteToHex(int byteValue) {
        char[] hexChars = new char[2];
        hexChars[0] = hexArray[byteValue >>> 4];
        hexChars[1] = hexArray[byteValue & 0x0F];
        return new String(hexChars);
    }

    public static String byteToHex(byte byteValue) {
        char[] hexChars = new char[2];
        int v = byteValue & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
        return new String(hexChars);
    }
}
