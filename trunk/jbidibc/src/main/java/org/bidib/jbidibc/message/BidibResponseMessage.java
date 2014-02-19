package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class BidibResponseMessage extends BidibMessage {

    BidibResponseMessage(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
    }

    /**
     * Create a BidibResponseMessage from an array of bytes.
     * 
     * @param message
     *            array of bytes, containing the leading magic byte, but without
     *            the trailing magic byte
     * 
     * @throws ProtocolException
     *             Thrown if the leading magic byte was missing.
     */
    BidibResponseMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BidibResponseMessage) {
            BidibResponseMessage other = (BidibResponseMessage) obj;
            if (other.getType() == getType() && other.getNum() == getNum()
                && ByteUtils.arrayEquals(other.getAddr(), getAddr())
                && ByteUtils.arrayEquals(other.getData(), getData())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
