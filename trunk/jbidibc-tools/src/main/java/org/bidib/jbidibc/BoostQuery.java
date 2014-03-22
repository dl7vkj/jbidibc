package org.bidib.jbidibc;

import java.util.Collection;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.CommandStationProgState;
import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.enumeration.LcOutputType;
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
            openPort(getPortName());

            Node node = findNode();

            if (node != null) {

                final BoosterStateHelper boosterStateHelper = new BoosterStateHelper();
                Bidib.getInstance().getMessageReceiver().addMessageListener(new MessageListener() {

                    @Override
                    public void speed(byte[] address, AddressData addressData, int speed) {
                    }

                    @Override
                    public void occupied(byte[] address, int detectorNumber) {
                    }

                    @Override
                    public void key(byte[] address, int keyNumber, int keyState) {
                    }

                    @Override
                    public void identity(byte[] address, IdentifyState identifyState) {
                    }

                    @Override
                    public void free(byte[] address, int detectorNumber) {
                    }

                    @Override
                    public void error(byte[] address, int errorCode) {
                    }

                    @Override
                    public void confidence(byte[] address, int valid, int freeze, int signal) {
                    }

                    @Override
                    public void boosterVoltage(byte[] address, int voltage) {
                    }

                    @Override
                    public void boosterTemperature(byte[] address, int temperature) {
                    }

                    @Override
                    public void boosterState(byte[] address, BoosterState state) {
                        LOGGER.info("Received current booster state: {}", state);
                        boosterStateHelper.setBoosterState(state);
                        synchronized (boosterStateHelper) {
                            boosterStateHelper.notifyAll();
                        }
                    }

                    @Override
                    public void boosterCurrent(byte[] address, int current) {
                    }

                    @Override
                    public void address(byte[] address, int detectorNumber, Collection<AddressData> addressData) {
                    }

                    @Override
                    public void accessoryState(byte[] address, AccessoryState accessoryState) {
                    }

                    @Override
                    public void lcStat(byte[] address, LcOutputType portType, int portNumber, int portStatus) {
                    }

                    @Override
                    public void feedbackAccessory(byte[] address, int detectorNumber, int accessoryAddress) {
                    }

                    @Override
                    public void feedbackCv(byte[] address, int decoderAddress, int cvNumber, int dat) {
                    }

                    @Override
                    public void dynState(byte[] address, int detectorNumber, int dynNumber, int dynValue) {
                    }

                    @Override
                    public void csProgState(
                        byte[] address, CommandStationProgState commandStationProgState, int remainingTime,
                        int cvNumber, int cvData) {
                    }

                    @Override
                    public void csState(byte[] address, CommandStationState commandStationState) {
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
