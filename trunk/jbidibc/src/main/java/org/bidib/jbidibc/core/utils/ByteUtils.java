/**
 * 
 */
package org.bidib.jbidibc.core.utils;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * This class contains utility functions for byte conversations.
 */
public final class ByteUtils {
    private ByteUtils() {
    }

    public static byte[] toDWORD(int value) {
        byte[] dword = new byte[4];

        for (int index = 0; index < 4; index++) {
            byte val = (byte) ((value >> (8 * index)) & 0xFF);
            dword[index] = val;
        }
        return dword;
    }

    public static int getDWORD(byte[] value) {
        int dword = 0;
        for (int index = 3; index > -1; index--) {
            dword = (dword << 8) | (value[index] & 0xFF);
        }
        return dword;
    }

    public static byte[] toWORD(int value) {
        byte[] word = new byte[2];

        for (int index = 0; index < 2; index++) {
            byte val = (byte) ((value >> (8 * index)) & 0xFF);
            word[index] = val;
        }
        return word;
    }

    public static int getWORD(byte[] value) {
        int word = 0;
        for (int index = 1; index > -1; index--) {
            word = (word << 8) | (value[index] & 0xFF);
        }
        return word;
    }

    public static int getWord(byte lowByte, byte highByte) {
        return ((highByte & 0x3F) << 8) + (lowByte & 0xFF);
    }

    /**
     * Get the (unsigned) int value from the low and high byte value.
     * 
     * @param lowByte
     *            the low byte
     * @param highByte
     *            the high byte
     * @return the combined int value
     */
    public static int getInt(byte lowByte, byte highByte) {
        return ((highByte & 0xFF) << 8) + (lowByte & 0xFF);
    }

    /**
     * Get the (unsigned) integer value from the low and high byte value.
     * 
     * @param lowByte
     *            the low byte
     * @param highByte
     *            the high byte
     * @return the combined int value or null if both values are null
     */
    public static Integer getInteger(Byte lowByte, Byte highByte) {
        if (lowByte != null && highByte != null) {
            return ((highByte & 0xFF) << 8) + (lowByte & 0xFF);
        }
        return null;
    }

    /**
     * Get the (unsigned) int value from the byte value.
     * 
     * @param byteValue
     *            the byte value
     * @return the int value
     */
    public static int getInt(byte byteValue) {
        return (byteValue & 0xFF);
    }

    /**
     * Get the (unsigned) int value from the byte value.
     * 
     * @param byteValue
     *            the byte value
     * @return the int value
     */
    public static int getInt(byte byteValue, int mask) {
        return (byteValue & mask);
    }

    /**
     * Concat a byte array.
     * 
     * @param array1
     *            the first array
     * @param array2
     *            the second array
     * @return the concatenated array
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];

        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static byte[] convertLongToUniqueId(long uniqueId) {
        byte[] value = BigInteger.valueOf(uniqueId).toByteArray();
        int size = (value.length == 8 ? 1 : 0);
        return Arrays.copyOfRange(value, size, 7 + size);
    }

    public static byte[] getVidPidFromUniqueId(long uniqueId) {
        byte[] value = BigInteger.valueOf(uniqueId).toByteArray();
        int size = (value.length == 8 ? 1 : 0);
        return Arrays.copyOfRange(value, size, 7 + size);
    }

    public static int getClassIdFromUniqueId(long uniqueId) {
        byte[] value = BigInteger.valueOf(uniqueId).abs().toByteArray();
        int index = (value.length == 8 ? 1 : 0);
        return ByteUtils.getInt(value[index]);
    }

    public static long convertUniqueIdToLong(byte[] uniqueId) {
        long result = 0;

        for (int i = 0; i < uniqueId.length; i++) {
            result = (result << 8) + (uniqueId[i] & 0xFF);
        }
        return result;
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
     * 
     * @param a1
     *            the first byte array
     * @param a2
     *            the second byte array
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
     * 
     * @param value
     *            the value
     * @return the low byte
     */
    public static byte getLowByte(int value) {
        byte lowByte = getLowByte(value, 0xFF);
        return lowByte;
    }

    /**
     * Returns the low byte of an int value.
     * 
     * @param value
     *            the value
     * @param mask
     *            the mask
     * @return the low byte
     */
    public static byte getLowByte(int value, int mask) {
        byte lowByte = (byte) (value & mask);
        return lowByte;
    }

    /**
     * Returns the low byte of an int value.
     * 
     * @param value
     *            the value
     * @return the low byte
     */
    public static Byte getLowByte(Integer value) {
        if (value != null) {
            byte lowByte = (byte) (value.intValue() & 0xFF);
            return lowByte;
        }
        return null;
    }

    /**
     * Returns the high byte of an int value.
     * 
     * @param value
     *            the value
     * @return the high byte
     */
    public static byte getHighByte(int value) {
        byte penultimateByte = (byte) ((value >> 8) & 0xFF);
        return penultimateByte;
    }

    /**
     * Returns the high byte of an int value.
     * 
     * @param value
     *            the value
     * @return the high byte
     */
    public static Byte getHighByte(Integer value) {
        if (value != null) {
            byte penultimateByte = (byte) ((value >> 8) & 0xFF);
            return penultimateByte;
        }
        return null;
    }

    /**
     * Returns the low byte value of an int value as int.
     * 
     * @param value
     *            the value
     * @return the low byte value as int
     */
    public static int getIntLowByteValue(int value) {
        return (value & 0xFF);
    }

    /**
     * Returns the high byte value of an int value as int.
     * 
     * @param value
     *            the value
     * @return the low byte value as int
     */
    public static int getIntHighByteValue(int value) {
        return (value & 0xFF00) >> 8;
    }

    /**
     * Converts the serial number to an int value.
     * 
     * @param serialData
     *            the serial number
     * @param offset
     *            the offset to read the data
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

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Convert a byte array to a hex string with spaces delimited. Original source: DatatypeConverterImpl.printHexBinary
     * 
     * @param bytes
     *            the byte array
     * @return the formatted hex string
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder r = new StringBuilder(bytes.length * 3);
        for (byte b : bytes) {
            r.append(hexArray[(b >> 4) & 0xF]);
            r.append(hexArray[(b & 0xF)]);
            r.append(' ');
        }
        return r.toString();
    }

    /**
     * Convert a byte array to a hex string with spaces delimited. Original source: DatatypeConverterImpl.printHexBinary
     * 
     * @param bytes
     *            the byte array
     * @param length
     *            the number of bytes to append
     * @return the formatted hex string
     */
    public static String bytesToHex(byte[] bytes, int length) {
        StringBuilder r = new StringBuilder(length * 3);
        for (int index = 0; index < length; index++) {
            byte b = bytes[index];
            r.append(hexArray[(b >> 4) & 0xF]);
            r.append(hexArray[(b & 0xF)]);
            r.append(' ');
        }
        return r.toString();
    }

    public static String byteToHex(int byteValue) {
        char[] hexChars = new char[2];
        hexChars[0] = hexArray[(byteValue & 0xF0) >>> 4];
        hexChars[1] = hexArray[byteValue & 0x0F];
        return new String(hexChars);
    }

    public static String magicToHex(int magic) {
        char[] hexChars = new char[4];
        hexChars[0] = hexArray[(magic & 0xF000) >>> 12];
        hexChars[1] = hexArray[(magic & 0x0F00) >>> 8];
        hexChars[2] = hexArray[(magic & 0xF0) >>> 4];
        hexChars[3] = hexArray[magic & 0x0F];
        return new String(hexChars);
    }

    public static String byteToHex(byte byteValue) {
        char[] hexChars = new char[2];
        int v = byteValue & 0xFF;
        hexChars[0] = hexArray[v >>> 4];
        hexChars[1] = hexArray[v & 0x0F];
        return new String(hexChars);
    }

    public static byte setBit(byte byteValue, boolean bit, int bitpos) {
        Integer newValue = null;
        int intValue = (byteValue & 0xFF);
        if (bit) {
            newValue = (intValue | (1 << bitpos));
        }
        else {
            newValue = (intValue & ~(1 << bitpos));
        }

        return ByteUtils.getLowByte(newValue);
    }

    public static int getBit(byte byteValue, int bitpos) {

        int intValue = (byteValue & 0xFF);
        return ((intValue >>> bitpos) & 1);
    }

    public static int getBit(int intValue, int bitpos) {

        return ((intValue >>> bitpos) & 1);
    }

    public static boolean isBitSetEqual(byte byteValue, int bit, int bitpos) {

        int intValue = (byteValue & 0xFF);
        if (((intValue >>> bitpos) & 1) == bit) {
            return true;
        }
        return false;
    }

    public static boolean isBitSetEqual(int intValue, int bit, int bitpos) {

        if (((intValue >>> bitpos) & 1) == bit) {
            return true;
        }
        return false;
    }

    public static byte[] bstr(String value) {
        byte[] result = new byte[value.length() + 1 /* terminating zero */];

        result[0] = ByteUtils.getLowByte(value.length());
        System.arraycopy(value.getBytes(), 0, result, 1, value.length());
        return result;
    }

    public static byte[] bstr(String name, String value) {
        byte[] result = new byte[name.length() + value.length() + 2 /* 2x terminating zero */];

        result[0] = ByteUtils.getLowByte(name.length());
        System.arraycopy(name.getBytes(), 0, result, 1, name.length());
        result[name.length() + 1] = ByteUtils.getLowByte(value.length());
        System.arraycopy(value.getBytes(), 0, result, name.length() + 2, value.length());
        return result;
    }

    public static String cstr(byte[] bstr, int offset) {
        int length = bstr[offset];
        byte[] cstr = new byte[length];

        System.arraycopy(bstr, offset + 1, cstr, 0, length);
        return new String(cstr);
    }
}
