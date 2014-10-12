package org.bidib.jbidibc.core.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.CommandStationProgState;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the current state
 */
public class CommandStationProgStateResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationProgStateResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_CS_PROG_STATE;

    CommandStationProgStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 4) {
            throw new ProtocolException("no command station prog state received");
        }
    }

    public CommandStationProgStateResponse(byte[] addr, int num, CommandStationProgState state, int remainingTime,
        int cvNumber, int cvData) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_CS_PROG_STATE, prepareData(state, remainingTime, cvNumber, cvData));
    }

    public String getName() {
        return "MSG_CS_PROG_STATE";
    }

    private static byte[] prepareData(CommandStationProgState state, int remainingTime, int cvNumber, int cvData) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // state
        out.write(state.getType());
        // time
        out.write(ByteUtils.getLowByte(remainingTime));
        // no CV_L and CV_H
        out.write(ByteUtils.getLowByte(cvNumber));
        out.write(ByteUtils.getHighByte(cvNumber));
        // data
        out.write(ByteUtils.getLowByte(cvData));

        return out.toByteArray();
    }

    public CommandStationProgState getState() {
        return CommandStationProgState.valueOf(getData()[0]);
    }

    public int getRemainingTime() {
        return ByteUtils.getInt(getData()[1]);
    }

    public int getCvNumber() {
        int index = 2;
        byte lowByte = getData()[index++];
        byte highByte = getData()[index++];
        int cvNumber = ByteUtils.getWord(lowByte, highByte);

        cvNumber++; // 0 -> CV 1

        return cvNumber;
    }

    public int getCvData() {
        if (getData().length > 4) {
            byte cvData = getData()[4];

            return ByteUtils.getInt(cvData);
        }
        LOGGER.warn("Invalid CV data received!");
        return -1;
    }
}
