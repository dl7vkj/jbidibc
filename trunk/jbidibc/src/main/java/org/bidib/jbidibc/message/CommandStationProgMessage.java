package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.CommandStationPt;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command to send programming commands for 'programming track'
 */
public class CommandStationProgMessage extends BidibCommandMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationProgMessage.class);

    /**
     * Create the PT command for a loco decoder.
     * 
     * @param opCode
     *            the operation code
     * @param cvNumber
     *            the CV number
     * @param data
     *            the data to write
     */
    public CommandStationProgMessage(CommandStationPt opCode, int cvNumber, byte data) {
        super(0, BidibLibrary.MSG_CS_PROG, prepareData(opCode, cvNumber, data));
    }

    public CommandStationProgMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    private static byte[] prepareData(CommandStationPt opCode, int cvNumber, byte data) {

        if (cvNumber < 1 || cvNumber > 1024) {
            throw new IllegalArgumentException("CV number is out of allowed range (1..1024).");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // op code
        out.write(opCode.getType());
        // CV number
        out.write(ByteUtils.getLowByte(cvNumber - 1));
        out.write(ByteUtils.getHighByte(cvNumber - 1));
        // data
        out.write(data);

        LOGGER.info("Send data to interface: {}", ByteUtils.bytesToHex(out.toByteArray()));

        return out.toByteArray();
    }

    public CommandStationPt getOpCode() {
        byte opCode = getData()[0];

        CommandStationPt commandStationPt = CommandStationPt.valueOf(opCode);
        return commandStationPt;
    }

    public int getCvNumber() {
        int index = 1;
        byte lowByte = getData()[index++];
        byte highByte = getData()[index++];
        int cvNumber = ByteUtils.getWord(lowByte, highByte);
        return cvNumber;
    }

    public int getCvData() {
        if (getData().length > 3) {
            byte cvData = getData()[3];

            return ByteUtils.getInt(cvData);
        }
        return -1;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
