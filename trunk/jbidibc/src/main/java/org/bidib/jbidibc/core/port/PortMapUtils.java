package org.bidib.jbidibc.core.port;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcOutputType;

public class PortMapUtils {

    private PortMapUtils() {

    }

    public static boolean supportsPortType(LcOutputType lcOutputType, ReconfigPortConfigValue reconfigPortConfigValue) {
        boolean supportsPortType = false;
        int portMap = reconfigPortConfigValue.getPortMap();
        switch (lcOutputType) {
            case SWITCHPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_SPORT) & 0x01) == 0x01;
                break;
            case LIGHTPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_LPORT) & 0x01) == 0x01;
                break;
            case SERVOPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_SERVO) & 0x01) == 0x01;
                break;
            case SOUNDPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_SOUND) & 0x01) == 0x01;
                break;
            case MOTORPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_MOTOR) & 0x01) == 0x01;
                break;
            case ANALOGPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_ANALOG) & 0x01) == 0x01;
                break;
            case BACKLIGHTPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_BACKLIGHT) & 0x01) == 0x01;
                break;
            case INPUTPORT:
                supportsPortType = ((portMap >> BidibLibrary.BIDIB_OUTTYPE_INPUT) & 0x01) == 0x01;
                break;
            default:
                break;
        }

        return supportsPortType;
    }

}
