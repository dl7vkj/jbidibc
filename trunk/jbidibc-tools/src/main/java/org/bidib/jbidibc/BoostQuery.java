package org.bidib.jbidibc;

import org.bidib.jbidibc.core.DefaultMessageListener;
import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BoosterNode;
import org.bidib.jbidibc.serial.Bidib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameters;

/**
 * This commands reads the value of the specified CV from the specified node.
 * 
 */
@Parameters(separators = "=")
public class BoostQuery extends BidibNodeCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoostQuery.class);

    public static void main(String[] args) {
        run(new BoostQuery(), args);
    }

    public int execute() {
        int result = 20;

        try {
            openPort(getPortName(), null);

            Node node = findNode();

            if (node != null) {

                final BoosterStateHelper boosterStateHelper = new BoosterStateHelper();
                Bidib.getInstance().getMessageReceiver().addMessageListener(new DefaultMessageListener() {

                    @Override
                    public void boosterState(byte[] address, BoosterState state) {
                        LOGGER.info("Received current booster state: {}", state);
                        boosterStateHelper.setBoosterState(state);
                        synchronized (boosterStateHelper) {
                            boosterStateHelper.notifyAll();
                        }
                    }
                });

                // verify that the booster query returns the booster state ...
                BoosterNode boosterNode = Bidib.getInstance().getBoosterNode(node);
                boosterNode.boosterQuery();

                // wait for repsonse
                synchronized (boosterStateHelper) {
                    LOGGER.info("Wait for response.");
                    if (boosterStateHelper.getBoosterState() == null) {
                        boosterStateHelper.wait(3000L);
                    }
                }

                LOGGER.info("Current booster state: {}", boosterStateHelper.getBoosterState());

                result = 0;
            }
            else {
                LOGGER.warn("node with unique id \"" + getNodeIdentifier() + "\" not found");
            }

            Bidib.getInstance().close();

        }
        catch (PortNotFoundException ex) {
            LOGGER.error("The provided port was not found: " + ex.getMessage()
                + ". Verify that the BiDiB device is connected.", ex);
        }
        catch (Exception ex) {
            LOGGER.error("Execute command failed.", ex);
        }
        return result;
    }

    private static final class BoosterStateHelper {
        private BoosterState boosterState;

        private Object lock = new Object();

        public void setBoosterState(BoosterState boosterState) {
            synchronized (lock) {
                this.boosterState = boosterState;
            }
        }

        public BoosterState getBoosterState() {
            synchronized (lock) {
                return boosterState;
            }
        }
    }
}
