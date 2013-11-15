package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.utils.MessageUtils;

public class LcMacroSetMessage extends BidibMessage {

    /**
     * Creates a new LcMacroSetMessage instance with values from provided macro step.
     * @param macro the macro step.
     */
    public LcMacroSetMessage(LcMacro macro) {
        super(0, BidibLibrary.MSG_LC_MACRO_SET, new byte[] { macro.getMacroNumber(), macro.getStepNumber(),
            macro.getDelay(), macro.getOutputType().getType(), macro.getOutputNumber(),
            MessageUtils.getPortStatus(macro) });
    }

}
