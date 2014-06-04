package org.bidib.jbidibc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CvUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CvUtils.class);

    public static int preparePomBitCvValue(int bitNumber, int bitValue) {

        // Beim Bit Schreiben wird das zuschreibende Bit mittels DATA bestimmt: DATA = 1111DBBB,
        // wobei BBB die Bitposition angibt und D den Wert des Bits (identisch zur DCC Definition)

        // set integer value from selected bit
        int cvValue = bitNumber & 0x07;

        // set the value of the bit
        if (bitValue != 0) {
            cvValue = (byte) (cvValue | 0x08);
        }

        // set the top 4 bits to 1
        cvValue |= 0xF0;

        LOGGER.debug("Prepared bit-based cvValue: {}", ByteUtils.byteToHex(cvValue));

        return cvValue;
    }

    public static int preparePtBitCvValue(boolean write, int bitNumber, int bitValue) {

        // Beim Bit Schreiben wird das zuschreibende Bit mittels DATA bestimmt: DATA = 111KDBBB,
        // wobei BBB die Bitposition angibt und D den Wert des Bits. K ist die Operation (1=write,
        // 0=read)(identisch zur DCC Definition)

        // set integer value from selected bit
        int cvValue = bitNumber & 0x07;

        // set the value of the bit
        if (bitValue != 0) {
            cvValue = (byte) (cvValue | 0x08);
        }

        // set the flag for bit write
        if (write) {
            cvValue = (cvValue | 0x10);
        }

        // set the top 3 bits to 1
        cvValue |= 0xE0;

        LOGGER.debug("Prepared bit-based cvValue: {}", ByteUtils.byteToHex(cvValue));

        return cvValue;
    }

}
