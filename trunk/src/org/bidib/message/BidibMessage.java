package org.bidib.message;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import org.bidib.exception.ProtocolException;

public class BidibMessage {
    // The address field is only valid for a response message!
    private byte[] addr = null;
    private int num = 0;
    private byte type = 0;
    private byte[] data = null;

    BidibMessage(byte[] addr, int num, int type, byte... data) {
        this.addr = addr;
        this.num = num & 0xFF;
        this.type = (byte) type;
        this.data = data;
    }

    BidibMessage(int num, int type, byte... data) {
        this.num = num & 0xFF;
        this.type = (byte) type;
        this.data = data;
    }

    /**
     * Create a BidibMessage from an array of bytes.
     * 
     * @param message
     *            array of bytes, containing the leading magic byte, but wthout the trailing magic byte
     * 
     * @throws ProtocolException
     *             Thrown if the leading magic byte was missing.
     */
    BidibMessage(byte[] message) throws ProtocolException {
        if (message != null && message.length > 1) {
            int index = 0;
            int length = message[index++];

            // address
            ByteArrayOutputStream addrBytes = new ByteArrayOutputStream();

            while (message[index] != 0) {
                addrBytes.write(message[index++]);
                if (index >= message.length) {
                    throw new ProtocolException("address too long");
                }
            }
            if (addrBytes.size() < 4) {
                addrBytes.write(0);
            }
            addr = addrBytes.toByteArray();
            index++;

            num = message[index++];
            type = message[index++];

            // data
            ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();

            while (index <= length) {
                dataBytes.write(message[index++]);
            }
            data = dataBytes.toByteArray();
        } else {
            throw new ProtocolException("message too short");
        }
    }

    protected static byte[] concat(byte[] array1, byte[] array2) {
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

    public byte[] getAddr() {
        return addr;
    }

    public int getNum() {
        return num;
    }

    public byte getType() {
        return type;
    }

    public byte[] getData() {
        return data;
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

    public String toString() {
        return getClass().getSimpleName() + "[" + (addr != null ? toString(addr) + "," : "") + "num=" + (num & 0xFF)
                + ",type=" + (type & 0xFF) + ",data=" + toString(data) + "]";
    }
}
