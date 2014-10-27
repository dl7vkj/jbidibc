package org.bidib.jbidibc.core.message;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcConfigX;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcConfigXResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcConfigXResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_LC_CONFIGX;

    LcConfigXResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("No LcConfigX response received.");
        }
    }

    public LcConfigXResponse(byte[] addr, int num, byte portType, byte portNum, byte[] config) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_CONFIGX, ByteUtils.concat(new byte[] { portType, portNum }, config));
    }

    public String getName() {
        return "MSG_LC_CONFIGX";
    }

    public LcConfigX getLcConfigX() {
        byte[] data = getData();

        byte outputType = data[0];
        int portNumber = ByteUtils.getInt(data[1], 0x7F);

        Map<Byte, Number> values = new LinkedHashMap<>();
        // TODO get the values

        if (data.length > 2) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(data);

                bais.skip(2);

                while (bais.available() > 0) {
                    byte pEnum = ByteUtils.getLowByte(bais.read());
                    int bytesRead = 0;
                    switch (pEnum & 0x40) {
                        case 0x40: // int
                            byte[] intValue = new byte[4];
                            bytesRead = bais.read(intValue);
                            LOGGER.info("Read a int value: {}, bytesRead: {}, pEnum: {}", ByteUtils.toString(intValue),
                                bytesRead, ByteUtils.getInt(pEnum));

                            Integer integerValue = ByteUtils.getDWORD(intValue);
                            values.put(pEnum, integerValue);
                            break;
                        default: // byte
                            byte[] byteVal = new byte[1];
                            bytesRead = bais.read(byteVal);
                            LOGGER.info("Read a byte value: {}, bytesRead: {}, pEnum: {}", ByteUtils.toString(byteVal),
                                bytesRead, ByteUtils.getInt(pEnum));
                            Byte byteValue = new Byte(byteVal[0]);
                            values.put(pEnum, byteValue);
                            break;
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
