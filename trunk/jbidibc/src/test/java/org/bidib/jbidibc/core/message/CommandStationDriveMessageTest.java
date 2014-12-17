package org.bidib.jbidibc.core.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.BitSet;

import org.bidib.jbidibc.core.AddressData;
import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.CRC8;
import org.bidib.jbidibc.core.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.core.enumeration.DirectionEnum;
import org.bidib.jbidibc.core.enumeration.SpeedStepsEnum;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommandStationDriveMessageTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveMessageTest.class);

    @Test
    public void prepareSpeedMessageBackward() {
        int address = 3;
        SpeedStepsEnum speedSteps = SpeedStepsEnum.DCC128;
        Integer speed = 51;
        DirectionEnum direction = DirectionEnum.BACKWARD;
        BitSet activeFunctions = new BitSet(8);
        activeFunctions.set(0, true);
        activeFunctions.set(1, false);
        // activeFunctions.set(1, true);
        BitSet functions = new BitSet(32);
        functions.set(4, true); // FL
        functions.set(3, false); // F4 - off
        functions.set(2, true); // F3
        functions.set(1, true); // F2
        functions.set(0, true); // F1
        CommandStationDriveMessage message =
            new CommandStationDriveMessage(address, speedSteps, speed, direction, activeFunctions, functions);

        LOGGER.debug("Created message: {}", message);
        Assert.assertNotNull(message);

        Assert.assertEquals(message.getData()[2], (byte) 0x03); // DCC128
        Assert.assertEquals(message.getData()[3], (byte) 0x03); // active

        // direction and speed
        Assert.assertEquals(message.getData()[4], (byte) 51); // speed

        // functions:
        // speed and
        // light
        // (F1...F4)
        Assert.assertEquals(message.getData()[5], (byte) 27); // FL,F3,F2,F1
    }

    @Test
    public void prepareSpeedMessageForward() {
        int address = 3;
        SpeedStepsEnum speedSteps = SpeedStepsEnum.DCC128;
        Integer speed = 51;
        DirectionEnum direction = DirectionEnum.FORWARD;
        BitSet activeFunctions = new BitSet(8);
        activeFunctions.set(0, true);
        activeFunctions.set(1, false);
        // activeFunctions.set(1, true);
        BitSet functions = new BitSet(32);
        functions.set(4, true); // FL
        functions.set(3, false); // F4 - off
        functions.set(2, true); // F3
        functions.set(1, true); // F2
        functions.set(0, true); // F1
        CommandStationDriveMessage message =
            new CommandStationDriveMessage(address, speedSteps, speed, direction, activeFunctions, functions);

        LOGGER.debug("Created message: {}", message);
        Assert.assertNotNull(message);

        Assert.assertEquals(message.getData()[2], (byte) 0x03); // DCC128
        Assert.assertEquals(message.getData()[3], (byte) 0x03); // active

        // direction and speed
        Assert.assertEquals(message.getData()[4], (byte) (51 | 0x80)); // speed

        // functions:
        // speed and
        // light
        // (F1...F4)
        Assert.assertEquals(message.getData()[5], (byte) 27); // FL,F3,F2,F1
    }

    @Test
    public void prepareCommandStationDriveMessage() throws ProtocolException {
        // FE 0B 00 39 64 03 00 01 84 00 00 00 00 DC FE
        byte[] message = new byte[] { 0x0B, 0x00, 0x39, 0x64, 0x03, 0x00, 0x01, (byte) 0x84, 0x00, 0x00, 0x00, 0x00 };
        CommandStationDriveMessage driveMessage = new CommandStationDriveMessage(message);
        Assert.assertNotNull(driveMessage);
        LOGGER.info("Prepare driveMessage: {}", driveMessage);

        AddressData locoAddress = new AddressData(3, AddressTypeEnum.LOCOMOTIVE_BACKWARD);

        Assert.assertEquals(driveMessage.getDecoderAddress(), locoAddress);
        Assert.assertEquals(driveMessage.getSpeed(), 0x00);
    }

    @Test
    public void prepareCommandStationDriveMessageWithFunction9On() throws ProtocolException {
        // FE 0C 00 2F 64 03 00 03 04 80 00 10 00 00 92 FE
        byte[] message =
            new byte[] { 0x0C, 0x00, 0x2F, 0x64, 0x03, 0x00, 0x03, 0x04, (byte) 0x80, 0x00, 0x10, 0x00, 0x00 };

        CommandStationDriveMessage driveMessage = new CommandStationDriveMessage(message);
        Assert.assertNotNull(driveMessage);
        LOGGER.info("Prepare driveMessage: {}", driveMessage);

        AddressData locoAddress = new AddressData(3, AddressTypeEnum.LOCOMOTIVE_BACKWARD);

        Assert.assertEquals(driveMessage.getDecoderAddress(), locoAddress);
        Assert.assertEquals(driveMessage.getSpeed(), 0x00);

        Assert.assertEquals(driveMessage.getData()[2 + 1], (byte) 0x04); // output F9..F12
        Assert.assertEquals(driveMessage.getData()[2 + 4], (byte) 0x10); // F9
    }

    @Test
    public void prepareCommandStationDriveMessageWithFunctionF9Off() throws ProtocolException {
        // FE 0C 00 40 64 03 00 03 04 80 00 00 00 00 53 FE
        byte[] message =
            new byte[] { 0x0C, 0x00, 0x40, 0x64, 0x03, 0x00, 0x03, 0x04, (byte) 0x80, 0x00, 0x00, 0x00, 0x00 };

        CommandStationDriveMessage driveMessage = new CommandStationDriveMessage(message);
        Assert.assertNotNull(driveMessage);
        LOGGER.info("Prepare driveMessage: {}", driveMessage);

        AddressData locoAddress = new AddressData(3, AddressTypeEnum.LOCOMOTIVE_BACKWARD);

        Assert.assertEquals(driveMessage.getDecoderAddress(), locoAddress);
        Assert.assertEquals(driveMessage.getSpeed(), 0x00);

        Assert.assertEquals(driveMessage.getData()[2 + 1], (byte) 0x04); // output F9..F12
        Assert.assertEquals(driveMessage.getData()[2 + 4], (byte) 0x00); // F9 off
    }

    @Test
    public void someTest() throws ProtocolException, IOException {
        byte[] message =
            { 0x0C, 0x00, (byte) 0xB9, 0x64, 0x62, 0x02, 0x03, 0x03, (byte) 0x80, 0x10, 0x00, 0x00, 0x00, (byte) 0xBE };

        BidibMessage bidibMessage = new BidibMessage(message);
        LOGGER.info("message: {}", message);

        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_CS_DRIVE);

        CommandStationDriveMessage driveMessage = new CommandStationDriveMessage(message);
        Assert.assertNotNull(driveMessage);
        LOGGER.info("Prepare driveMessage: {}", driveMessage);

        AddressData locoAddress = new AddressData(610, AddressTypeEnum.LOCOMOTIVE_BACKWARD);

        Assert.assertEquals(driveMessage.getDecoderAddress(), locoAddress);
        Assert.assertEquals(driveMessage.getSpeed(), 0x00);

        EncodedMessage encodedMessage = encodeMessage(driveMessage, new byte[] { 0 });

        ByteArrayOutputStream output = transferMessage(encodedMessage);
        LOGGER.info("TRX: {}", ByteUtils.bytesToHex(output.toByteArray()));
    }

    private int getNextSendMsgNum() {
        return 0xB9;
    }

    private EncodedMessage encodeMessage(BidibCommand message, byte[] addr) {
        int num = getNextSendMsgNum();
        message.setSendMsgNum(num);
        // logRecord.append("send ").append(message).append(" to ").append(this);

        byte type = message.getType();
        byte[] data = message.getData();
        byte[] bytes = null;
        int index = 0;

        LOGGER.trace("Current node addr: {}", addr);

        if (addr != null && addr.length != 0 && addr[0] != 0) {
            bytes = new byte[1 + (addr.length + 1) + 2 + (data != null ? data.length : 0)];
            bytes[index++] = (byte) (bytes.length - 1);
            for (int addrIndex = 0; addrIndex < addr.length; addrIndex++) {
                bytes[index++] = addr[addrIndex];
            }
        }
        else {
            LOGGER.trace("Current address is the root node.");
            bytes = new byte[1 + (addr.length /* len of root node */) + 2 + (data != null ? data.length : 0)];
            bytes[index++] = (byte) (bytes.length - 1);
        }
        bytes[index++] = 0; // 'terminating zero' of the address

        bytes[index++] = (byte) num;
        bytes[index++] = type;
        if (data != null) {
            // LOGGER.debug("Add data: {}", ByteUtils.bytesToHex(data));
            for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
                bytes[index++] = data[dataIndex];
            }
        }
        EncodedMessage encodedMessage = new EncodedMessage(bytes);
        return encodedMessage;
    }

    public static class EncodedMessage {
        private byte[] message;

        public EncodedMessage(byte[] message) {
            this.message = message;
        }

        public byte[] getMessage() {
            return message;
        }
    }

    private ByteArrayOutputStream transferMessage(EncodedMessage encodedMessage) throws IOException {

        byte[] message = encodedMessage.getMessage();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Send the message: {}", ByteUtils.bytesToHex(message));
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        sendDelimiter(output);

        byte length = message[0];

        escape(output, length);

        int txCrc = CRC8.getCrcValue(length);

        for (int i = 1; i <= length; i++) {
            escape(output, message[i]);
            txCrc = CRC8.getCrcValue((message[i] ^ txCrc) & 0xFF);
        }
        escape(output, (byte) txCrc);
        sendDelimiter(output);

        return output;
    }

    private void sendDelimiter(ByteArrayOutputStream output) {
        output.write((byte) BidibLibrary.BIDIB_PKT_MAGIC);
    }

    private void escape(ByteArrayOutputStream output, byte c) throws IOException {
        if ((c == (byte) BidibLibrary.BIDIB_PKT_MAGIC) || (c == (byte) BidibLibrary.BIDIB_PKT_ESCAPE)) {
            output.write((byte) BidibLibrary.BIDIB_PKT_ESCAPE);
            c = (byte) (c ^ 0x20);
        }
        output.write(c);
    }

}
