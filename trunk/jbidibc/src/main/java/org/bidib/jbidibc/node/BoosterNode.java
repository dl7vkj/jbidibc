package org.bidib.jbidibc.node;

import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.core.DefaultMessageListener;
import org.bidib.jbidibc.enumeration.BoosterState;
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
     * @param broadcast
     *            broadcast message mode (see {@link BoostOnMessage} )
     * @throws ProtocolException
     */
    public void boosterOn(byte broadcast) throws ProtocolException {
        LOGGER.info("Switch booster on, broadcast: {}", broadcast);
        delegate.sendNoWait(new BoostOnMessage(broadcast));
    }

    /**
     * Switch off track signal on the booster.
     * 
     * @param broadcast
     *            broadcast message mode (see {@link BoostOffMessage} )
     * @throws ProtocolException
     */
    public void boosterOff(byte broadcast) throws ProtocolException {
        LOGGER.info("Switch booster off, broadcast: {}", broadcast);
        delegate.sendNoWait(new BoostOffMessage(broadcast));
    }

    /**
     * Query the booster state. We don't wait for the response because the MessageReceiver fires the booster status
     * callback on receipt.
     * 
     * @throws ProtocolException
     */
    public void boosterQuery() throws ProtocolException {
        delegate.sendNoWait(new BoostQueryMessage());
    }

    /**
     * Query the booster state.
     * 
     * @return the current booster state
     * @throws ProtocolException
     */
    public BoosterState queryState() throws ProtocolException {

        LOGGER.debug("Query the state of the booster.");

        BoosterState boosterState = BoosterState.OFF;

        final Object boosterStateFeedbackLock = new Object();
        final BoosterState[] resultHolder = new BoosterState[1];

        // create a temporary message listener
        MessageListener messageListener = new DefaultMessageListener() {
            @Override
            public void boosterState(byte[] address, BoosterState state) {
                LOGGER.info("+++ Booster state was signalled: {}", state);

                resultHolder[0] = state;

                synchronized (boosterStateFeedbackLock) {
                    boosterStateFeedbackLock.notify();
                }
            }
        };

        try {
            // add the message listener to the node
            addMessageListener(messageListener);

            synchronized (boosterStateFeedbackLock) {
                // send the query booster state command
                delegate.sendNoWait(new BoostQueryMessage());

                LOGGER.info("+++ The boost query message was sent, wait for response.");
                // wait for the response
                try {
                    boosterStateFeedbackLock.wait(2000L);
                }
                catch (InterruptedException ie) {
                    LOGGER.warn("Wait for booster state was interrupted.", ie);
                }

                LOGGER.info("+++ After wait for response.");
            }

            boosterState = resultHolder[0];
            LOGGER.info("+++ Return the current booster state: {}", boosterState);
        }
        finally {
            // remove the temporary message listener
            removeMessageListener(messageListener);
        }
        return boosterState;
    }

    private void addMessageListener(final MessageListener messageListener) {
        delegate.getMessageReceiver().addMessageListener(messageListener);
    }

    private void removeMessageListener(final MessageListener messageListener) {
        delegate.getMessageReceiver().removeMessageListener(messageListener);
    }
}
