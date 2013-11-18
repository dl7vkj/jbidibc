package org.bidib.jbidibc;

import java.util.Collection;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.enumeration.LcOutputType;

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

    void lcStat(byte[] address, LcOutputType portType, int portNumber, int portStatus);
}
