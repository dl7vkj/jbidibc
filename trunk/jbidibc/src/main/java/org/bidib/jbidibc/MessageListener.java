package org.bidib.jbidibc;

import java.util.Collection;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.IdentifyState;

/**
 * The message Listener interface.
 *
 */
public interface MessageListener {
    void address(byte[] address, int detectorNumber, Collection<AddressData> addressData);

    void boosterCurrent(byte[] address, int current);

    void boosterState(byte[] address, BoosterState state);

    void boosterTemperature(byte[] address, int temperature);

    void boosterVoltage(byte[] address, int voltage);

    void confidence(byte[] address, int valid, int freeze, int signal);

    void free(byte[] address, int detectorNumber);

    void identity(byte[] address, IdentifyState identifyState);

    void key(byte[] address, int keyNumber, int keyState);

    /**
     * Signals that a node was lost in the system.
     * @param node the lost node
     * 
     * <p>TODO refactor this into a separate interface</p>
     */
    void nodeLost(Node node);

    /**
     * Signals that a new node was found in the system.
     * @param node the new node
     * 
     * <p>TODO refactor this into a separate interface</p>
     */
    void nodeNew(Node node);

    void occupied(byte[] address, int detectorNumber);

    void speed(byte[] address, AddressData addressData, int speed);

    /**
     * Signals that an error message was received. 
     * @param address the address
     * @param errorCode the error code
     */
    void error(byte[] address, int errorCode);

    /**
     * Signals that an accessory state message was received. 
     * @param address the address
     * @param accessoryState the accessory state
     */
    void accessoryState(byte[] address, AccessoryState accessoryState);
}
