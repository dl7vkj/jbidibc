package org.bidib.jbidibc.node;

import java.io.IOException;

import org.bidib.jbidibc.MessageReceiver;
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
     * 
     * @throws IOException
     * @throws ProtocolException
     * @throws InterruptedException
     */
    public void boosterOn() throws IOException, ProtocolException, InterruptedException {
        LOGGER.debug("Switch booster on.");
        delegate.send(new BoostOnMessage(), false, null);
    }

    /**
     * Switch off track signal on the booster.
     * 
     * @throws IOException
     * @throws ProtocolException
     * @throws InterruptedException
     */
    public void boosterOff() throws IOException, ProtocolException, InterruptedException {
        LOGGER.debug("Switch booster off.");
        delegate.send(new BoostOffMessage(), false, null);
    }

    /**
     * Query the booster state. We don't wait for the response because the {@link MessageReceiver} fires the booster
     * status callback on receipt.
     * 
     * @throws IOException
     * @throws ProtocolException
     * @throws InterruptedException
     */
    public void boosterQuery() throws IOException, ProtocolException, InterruptedException {
        delegate.send(new BoostQueryMessage(), false, null);
    }

}
