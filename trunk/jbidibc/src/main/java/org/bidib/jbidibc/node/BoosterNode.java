package org.bidib.jbidibc.node;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BoostOffMessage;
import org.bidib.jbidibc.message.BoostOnMessage;
import org.bidib.jbidibc.message.BoostQueryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoosterNode {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoosterNode.class);

    private BidibNode delegate;

    BoosterNode(BidibNode delegate) {
        this.delegate = delegate;
    }

    /**
     * Switch on track signal on the booster.
     * @param broadcast broadcast message mode (see {@link BoostOnMessage} ) 
     * @throws ProtocolException
     */
    public void boosterOn(byte broadcast) throws ProtocolException {
        LOGGER.info("Switch booster on, broadcast: {}", broadcast);
        delegate.sendNoWait(new BoostOnMessage(broadcast));
    }

    /**
     * Switch off track signal on the booster.
     * @param broadcast broadcast message mode (see {@link BoostOffMessage} ) 
     * @throws ProtocolException
     */
    public void boosterOff(byte broadcast) throws ProtocolException {
        LOGGER.info("Switch booster off, broadcast: {}", broadcast);
        delegate.sendNoWait(new BoostOffMessage(broadcast));
    }

    /**
     * Query the booster state. We don't wait for the response because the MessageReceiver fires the booster
     * status callback on receipt.
     * 
     * @throws ProtocolException
     */
    public void boosterQuery() throws ProtocolException {
        delegate.sendNoWait(new BoostQueryMessage());
    }

}
