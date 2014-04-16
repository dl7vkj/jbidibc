package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BidibCommandMessage extends BidibMessage implements BidibCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidibCommandMessage.class);

    // set a default size of 5
    private int answerSize = 5;

    BidibCommandMessage(int num, int type, byte... data) {
        super(num, type, data);
    }

    /**
     * Create a BidibCommandMessage from an array of bytes.
     * 
     * @param message
     *            array of bytes, containing the leading magic byte, but without the trailing magic byte
     * 
     * @throws ProtocolException
     *             Thrown if the leading magic byte was missing.
     */
    BidibCommandMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public int getAnswerSize() {
        return answerSize;
    }

    @Override
    public void setAnswerSize(int answerSize) {
        LOGGER.debug("Set the answer size: {}", answerSize);
        this.answerSize = answerSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BidibCommandMessage) {
            BidibCommandMessage other = (BidibCommandMessage) obj;
            if (other.getType() == getType() && other.getNum() == getNum()
                && ByteUtils.arrayEquals(other.getAddr(), getAddr())
                && ByteUtils.arrayEquals(other.getData(), getData())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
