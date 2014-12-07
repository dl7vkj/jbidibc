package org.bidib.jbidibc.core.utils;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.CRC8;
import org.bidib.jbidibc.core.LcConfigX;
import org.bidib.jbidibc.core.LcMacro;
import org.bidib.jbidibc.core.enumeration.AccessoryOkayEnum;
import org.bidib.jbidibc.core.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.core.enumeration.BacklightPortEnum;
import org.bidib.jbidibc.core.enumeration.BidibEnum;
import org.bidib.jbidibc.core.enumeration.FlagEnum;
import org.bidib.jbidibc.core.enumeration.InputPortEnum;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.enumeration.LightPortEnum;
import org.bidib.jbidibc.core.enumeration.MacroEnum;
import org.bidib.jbidibc.core.enumeration.MotorPortEnum;
import org.bidib.jbidibc.core.enumeration.ServoPortEnum;
import org.bidib.jbidibc.core.enumeration.SoundPortEnum;
import org.bidib.jbidibc.core.enumeration.SwitchPortEnum;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);

    /**
     * Convert the value of an output type to the corresponding BidibEnum.
     * 
     * @param outputType
     *            the output type
     * @param value
     *            the value.
     * @return the BidibEnum value
     */
    public static BidibEnum toPortStatus(LcOutputType outputType, byte value) {
        BidibEnum result = null;

        switch (outputType) {
            case ANALOGPORT:
                result = AnalogPortEnum.valueOf(value);
                break;
            case BACKLIGHTPORT:
                // the pseudo status is always start, because the value is delivered
                result = BacklightPortEnum.START;
                break;
            case LIGHTPORT:
                result = LightPortEnum.valueOf(value);
                break;
            case MOTORPORT:
                result = MotorPortEnum.valueOf(value);
                break;
            case SERVOPORT:
                // the pseudo status is always start, because the value is delivered
                result = ServoPortEnum.START;
                break;
            case SOUNDPORT:
                result = SoundPortEnum.valueOf(value);
                break;
            case SWITCHPORT:
                result = SwitchPortEnum.valueOf(value);
                break;
            case INPUTPORT:
                result = InputPortEnum.valueOf(value);
                break;
            case END_OF_MACRO:
            case ACCESSORY_OKAY_INPUTQUERY0:
            case ACCESSORY_OKAY_INPUTQUERY1:
            case ACCESSORY_OKAY_NF:
            case BEGIN_CRITICAL:
            case END_CRITICAL:
            case FLAG_CLEAR:
            case FLAG_QUERY:
            case FLAG_QUERY0:
            case FLAG_QUERY1:
            case FLAG_SET:
            case INPUT_QUERY0:
            case INPUT_QUERY1:
            case DELAY:
            case RANDOM_DELAY:
            case SERVOMOVE_QUERY:
            case START_MACRO:
            case STOP_MACRO:
                break;
            default:
                LOGGER.warn("No port status available for output type: {}", outputType);
                throw new RuntimeException("No port status available for output type: " + outputType);

        }

        // if (outputType == LcOutputType.ANALOGPORT) {
        // result = AnalogPortEnum.valueOf(value);
        // }
        // else if (outputType == LcOutputType.BACKLIGHTPORT) {
        // // the pseudo status is always start, because the value is delivered
        // result = BacklightPortEnum.START;
        // }
        // else if (outputType == LcOutputType.LIGHTPORT) {
        // result = LightPortEnum.valueOf(value);
        // }
        // else if (outputType == LcOutputType.MOTORPORT) {
        // result = MotorPortEnum.valueOf(value);
        // }
        // else if (outputType == LcOutputType.SERVOPORT) {
        // // the pseudo status is always start, because the value is delivered
        // result = ServoPortEnum.START;
        // }
        // else if (outputType == LcOutputType.SOUNDPORT) {
        // result = SoundPortEnum.valueOf(value);
        // }
        // else if (outputType == LcOutputType.SWITCHPORT) {
        // result = SwitchPortEnum.valueOf(value);
        // }
        // else if (outputType != null && outputType.hasPortStatus()) {
        // LOGGER.warn("No port status available for output type: {}", outputType);
        // throw new RuntimeException("No port status available for output type: " + outputType);
        // }

        return result;
    }

    /**
     * Get the port status value from the provided macro step.
     * 
     * @param macro
     *            the macro step
     * @return the port status
     */
    public static byte getPortStatus(LcMacro macro) {
        byte result = 0;
        BidibEnum status = macro.getStatus();

        if (status != null) {
            if (status instanceof AnalogPortEnum) {
                result = ((AnalogPortEnum) status).getType();
            }
            else if (status instanceof BacklightPortEnum) {
                result = macro.getValue();
            }
            else if (status instanceof FlagEnum) {
                result = ((FlagEnum) status).getType();
            }
            else if (status instanceof LightPortEnum) {
                result = ((LightPortEnum) status).getType();
            }
            else if (status instanceof MotorPortEnum) {
                result = ((MotorPortEnum) status).getType();
            }
            else if (status instanceof ServoPortEnum) {
                result = macro.getValue();
            }
            else if (status instanceof SoundPortEnum) {
                result = ((SoundPortEnum) status).getType();
            }
            else if (status instanceof SwitchPortEnum) {
                result = ((SwitchPortEnum) status).getType();
            }
            else if (status instanceof AccessoryOkayEnum) {
                result = ((AccessoryOkayEnum) status).getType();
            }
            else if (status instanceof MacroEnum) {
                result = ((MacroEnum) status).getType();
            }
            else {
                LOGGER.warn("Unsupported macro status detected: {}", status);
            }
        }
        return result;
    }

    // the value is only available for servoport and backlightport
    public static byte getPortValue(LcOutputType outputType, byte value) {
        byte result = 0;

        switch (outputType) {
            case BACKLIGHTPORT:
            case SERVOPORT:
                result = value;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * Split the byte array into separate messages. The CRC value at the end is calculated over the whole array.
     * 
     * @param output
     *            array containing at least one message
     * 
     * @return list of the separated messages
     * 
     * @throws ProtocolException
     *             Thrown if the CRC failed.
     */
    public static Collection<byte[]> splitBidibMessages(byte[] output) throws ProtocolException {
        Collection<byte[]> result = new LinkedList<byte[]>();
        int index = 0;

        LOGGER.trace("splitMessages: {}", output);

        while (index < output.length) {
            int size = output[index] + 1;

            if (size <= 0) {
                throw new ProtocolException("cannot split messages, array size is " + size);
            }

            byte[] message = new byte[size];

            try {
                System.arraycopy(output, index, message, 0, message.length);
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                LOGGER.warn("Failed to copy, msg.len: " + message.length + ", size: " + size + ", output.len: "
                    + output.length, ex);
                throw ex;
            }
            result.add(message);
            index += size;

            // CRC
            if (index == output.length - 1) {
                int crc = 0;

                for (index = 0; index < output.length - 1; index++) {
                    crc = CRC8.getCrcValue((output[index] ^ crc) & 0xFF);
                }
                if (crc != (output[index] & 0xFF)) {
                    throw new ProtocolException("CRC failed: should be " + crc + " but was " + (output[index] & 0xFF));
                }
                break;
            }
        }
        return result;
    }

    public static int convertCurrent(int current) {
        if (current > 15) {
            if (current >= 16 && current <= 63) {
                current = (current - 12) * 4;
            }
            else if (current >= 64 && current <= 127) {
                current = (current - 51) * 16;
            }
            else if (current >= 128 && current <= 191) {
                current = (current - 108) * 64;
            }
            else if (current >= 192 && current <= 250) {
                current = (current - 171) * 256;
            }
            else {
                current = 0;
            }
        }
        return current;
    }

    public static int convertVoltageValue(int value) {
        int result = 0;

        if (value >= 1 && value <= 250) {
            result = value;
        }
        return result;
    }

    public static int convertTemperatureValue(int value) {
        int result = 0;

        if (value >= 1 && value <= 250) {
            result = value;
        }
        return result;
    }

    public static LcConfigX getLcConfigX(byte[] data) {

        byte outputType = data[0];
        int portNumber = ByteUtils.getInt(data[1], 0x7F);

        Map<Byte, Number> values = new LinkedHashMap<>();
        // get the values
        if (data.length > 2) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(data);

                bais.skip(2);

                while (bais.available() > 0) {
                    byte pEnum = ByteUtils.getLowByte(bais.read());
                    int bytesRead = 0;
                    if ((pEnum & 0x80) == 0x80) {
                        if (BidibLibrary.BIDIB_PCFG_CONTINUE == pEnum) {
                            LOGGER.info("Continue detected, more config willbe received.");
                            values.put(pEnum, null);
                        }
                        else if (BidibLibrary.BIDIB_PCFG_RGB == pEnum) {
                            // RGB
                            byte[] rgbValue = new byte[3];
                            bytesRead = bais.read(rgbValue);
                            LOGGER.info("Read a RGB value: {}, bytesRead: {}, pEnum: {}", ByteUtils.toString(rgbValue),
                                bytesRead, ByteUtils.getInt(pEnum));

                            Integer integerValue = ByteUtils.getRGB(rgbValue);
                            values.put(pEnum, integerValue);
                        }
                        else {
                            // int32
                            byte[] intValue = new byte[4];
                            bytesRead = bais.read(intValue);
                            LOGGER.info("Read a int32 value: {}, bytesRead: {}, pEnum: {}",
                                ByteUtils.toString(intValue), bytesRead, ByteUtils.getInt(pEnum));

                            Integer integerValue = ByteUtils.getDWORD(intValue);
                            values.put(pEnum, integerValue);
                        }
                    }
                    else if ((pEnum & 0x40) == 0x40) {
                        // int16
                        byte[] intValue = new byte[2];
                        bytesRead = bais.read(intValue);
                        LOGGER.info("Read a int16 value: {}, bytesRead: {}, pEnum: {}", ByteUtils.toString(intValue),
                            bytesRead, ByteUtils.getInt(pEnum));

                        Integer integerValue = ByteUtils.getWORD(intValue);
                        values.put(pEnum, integerValue);
                    }
                    else { // byte
                        byte[] byteVal = new byte[1];
                        bytesRead = bais.read(byteVal);
                        LOGGER.info("Read a byte value: {}, bytesRead: {}, pEnum: {}", ByteUtils.toString(byteVal),
                            bytesRead, ByteUtils.getInt(pEnum));
                        Byte byteValue = new Byte(byteVal[0]);
                        values.put(pEnum, byteValue);
                    }
                }
            }
            catch (Exception ex) {
                LOGGER.warn("Read content of message failed.", ex);
            }
        }

        return new LcConfigX(LcOutputType.valueOf(outputType), portNumber, values);
    }
}
