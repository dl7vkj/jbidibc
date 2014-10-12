package org.bidib.jbidibc.core;

import java.util.Collection;

import org.bidib.jbidibc.core.enumeration.AccessoryAcknowledge;
import org.bidib.jbidibc.core.enumeration.ActivateCoilEnum;
import org.bidib.jbidibc.core.enumeration.BoosterState;
import org.bidib.jbidibc.core.enumeration.CommandStationProgState;
import org.bidib.jbidibc.core.enumeration.CommandStationState;
import org.bidib.jbidibc.core.enumeration.IdentifyState;
import org.bidib.jbidibc.core.enumeration.LcOutputType;

/**
 * The message Listener interface.
 * 
 */
public interface MessageListener {
    void address(byte[] address, int detectorNumber, Collection<AddressData> addressData);

    /**
     * Accessory address was signaled from feedback device.
     * 
     * @param address
     *            the node address
     * @param detectorNumber
     *            the local detector number.
     * @param accessoryAddress
     *            the accessory address
     */
    void feedbackAccessory(byte[] address, int detectorNumber, int accessoryAddress);

    /**
     * CV was signaled from feedback device.
     * 
     * @param address
     *            the node address
     * @param decoderAddress
     *            the decoder address
     * @param cvNumber
     *            the CV number
     * @param dat
     *            the date
     */
    void feedbackCv(byte[] address, AddressData decoderAddress, int cvNumber, int dat);

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
     * 
     * @param address
     *            the address
     * @param errorCode
     *            the error code
     */
    void error(byte[] address, int errorCode, byte[] reasonData);

    /**
     * Signals that an accessory state message was received.
     * 
     * @param address
     *            the address
     * @param accessoryState
     *            the accessory state
     */
    void accessoryState(byte[] address, AccessoryState accessoryState);

    void lcStat(byte[] address, LcOutputType portType, int portNumber, int portStatus);

    void lcWait(byte[] address, LcOutputType portType, int portNumber, int predRotationTime);

    void dynState(byte[] address, int detectorNumber, int dynNumber, int dynValue);

    void csProgState(
        byte[] address, CommandStationProgState commandStationProgState, int remainingTime, int cvNumber, int cvData);

    void csState(byte[] address, CommandStationState commandStationState);

    void csDriveManual(byte[] address, DriveState driveState);

    void csAccessoryManual(byte[] address, AddressData decoderAddress, ActivateCoilEnum activate, int aspect);

    void csAccessoryAcknowledge(byte[] address, int decoderAddress, AccessoryAcknowledge acknowledge);
}
