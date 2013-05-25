package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibMessage.class);
    // The address field is only valid for a response message!
    private byte[] addr;

    private int num;

    private byte type;

    private byte[] data;

    BidibMessage(byte[] addr, int num, int type, byte... data) {
        this.addr = addr != null ? addr.clone() : null;
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
     *            array of bytes, containing the leading magic byte, but wthout
     *            the trailing magic byte
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
                    LOGGER.warn("Invalid message: {}", message);
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
        }
        else {
            throw new ProtocolException("message too short");
        }
    }

    public byte[] getAddr() {
        return addr;
    }

    public int getNum() {
        return num;
    }

    public void setSendMsgNum(int sendMsgNum) {
        this.num = sendMsgNum;
    }

    public byte getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + (addr != null ? ByteUtils.toString(addr) + "," : "") + "num="
            + (num & 0xFF) + ",type=" + (type & 0xFF) + ",data=" + ByteUtils.toString(data) + "]";
    }
}
