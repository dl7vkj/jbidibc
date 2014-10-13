package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcPortMapping;
import org.bidib.jbidibc.core.enumeration.LcMappingPortType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcMappingResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcMappingResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_LC_MAPPING;

    LcMappingResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("no MSG_LC_MAPPING received");
        }
    }

    public LcMappingResponse(byte[] addr, int num, byte portType, byte portCount, byte[] portAvailable,
        byte[] portMapping) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_MAPPING, ByteUtils.concat(new byte[] { portType, portCount },
            ByteUtils.concat(portAvailable, portMapping)));
    }

    public String getName() {
        return "MSG_LC_MAPPING";
    }

    public LcMappingPortType getLcMappingPortType() {
        return LcMappingPortType.valueOf(getData()[0]);
    }

    /**
     * @return the total number of ports available on the node
     */
    public int getPortCount() {
        return ByteUtils.getInt(getData()[1]);
    }

    private int getFieldLen() {
        int portCount = getPortCount();
        if (portCount > 0) {
            return (portCount / 8) + (portCount % 8 > 0 ? 1 : 0);
        }
        return 0;
    }

    /**
     * @return the array with the information if the port is available for configuration on the requested port type.
     */
    public int[] getPortAvailable() {

        byte[] data = getData();
        int[] portAvailables = new int[getFieldLen() * 8];

        int baseOffset = 2;

        for (int index = 0; index < getFieldLen(); index++) {
            byte valueByte = data[index + baseOffset];
            for (int bit = 0; bit < 8; bit++) {

                int value = (valueByte >> bit) & 0x01;
                LOGGER.trace("Current bit: {}, value: {}, index: {}", bit, value, index);

                portAvailables[index * 8 + bit] = value;
            }
        }
        return portAvailables;
    }

    /**
     * @return the array with information if the port is mapped on the requested port type.
     */
    public int[] getPortMapping() {

        byte[] data = getData();
        int[] portMappings = new int[getFieldLen() * 8];

        int baseOffset = 2 + getFieldLen();

        for (int index = 0; index < getFieldLen(); index++) {
            byte valueByte = data[index + baseOffset];
            for (int bit = 0; bit < 8; bit++) {

                int value = (valueByte >> bit) & 0x01;
                LOGGER.trace("Current bit: {}, value: {}, index: {}", bit, value, index);

                portMappings[index * 8 + bit] = value;
            }
        }
        return portMappings;
    }

    public LcPortMapping getLcPortMapping() {
        LcPortMapping lcPortMapping = new LcPortMapping(getLcMappingPortType(), getPortCount());
        if (getFieldLen() > 0) {
            lcPortMapping.setPortAvailable(getPortAvailable());
            lcPortMapping.setPortMapping(getPortMapping());
        }
        return lcPortMapping;
    }
}
