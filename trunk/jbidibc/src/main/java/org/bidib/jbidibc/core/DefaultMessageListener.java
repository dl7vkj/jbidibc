package org.bidib.jbidibc.core;

import java.util.Collection;

import org.bidib.jbidibc.AccessoryState;
import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.DriveState;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.enumeration.AccessoryAcknowledge;
import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.CommandStationProgState;
import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.enumeration.LcOutputType;

/**
 * Default MessageListener implementation with empty method body.
 */
public class DefaultMessageListener implements MessageListener {

    @Override
    public void address(byte[] address, int detectorNumber, Collection<AddressData> addressData) {
    }

    @Override
    public void boosterCurrent(byte[] address, int current) {
    }

    @Override
    public void boosterState(byte[] address, BoosterState state) {
    }

    @Override
    public void boosterTemperature(byte[] address, int temperature) {
    }

    @Override
    public void boosterVoltage(byte[] address, int voltage) {
    }

    @Override
    public void confidence(byte[] address, int valid, int freeze, int signal) {
    }

    @Override
    public void free(byte[] address, int detectorNumber) {
    }

    @Override
    public void identity(byte[] address, IdentifyState identifyState) {
    }

    @Override
    public void key(byte[] address, int keyNumber, int keyState) {
    }

    @Override
    public void occupied(byte[] address, int detectorNumber) {
    }

    @Override
    public void speed(byte[] address, AddressData addressData, int speed) {
    }

    @Override
    public void error(byte[] address, int errorCode) {
    }

    @Override
    public void accessoryState(byte[] address, AccessoryState accessoryState) {
    }

    @Override
    public void lcStat(byte[] address, LcOutputType portType, int portNumber, int portStatus) {
    }

    @Override
    public void lcWait(byte[] address, LcOutputType portType, int portNumber, int predRotationTime) {
    }

    @Override
    public void feedbackAccessory(byte[] address, int detectorNumber, int accessoryAddress) {
    }

    @Override
    public void feedbackCv(byte[] address, AddressData decoderAddress, int cvNumber, int dat) {
    }

    @Override
    public void dynState(byte[] address, int detectorNumber, int dynNumber, int dynValue) {
    }

    @Override
    public void csProgState(
        byte[] address, CommandStationProgState commandStationProgState, int remainingTime, int cvNumber, int cvData) {
    }

    @Override
    public void csState(byte[] address, CommandStationState commandStationState) {
    }

    @Override
    public void csDriveManual(byte[] address, DriveState driveState) {
    }

    @Override
    public void csAccessoryManual(byte[] address, int aspect) {
    }

    @Override
    public void csAccessoryAcknowledge(byte[] address, int decoderAddress, AccessoryAcknowledge acknowledge) {
    }
}
