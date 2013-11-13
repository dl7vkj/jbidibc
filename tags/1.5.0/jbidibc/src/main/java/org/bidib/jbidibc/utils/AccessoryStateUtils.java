package org.bidib.jbidibc.utils;

import org.bidib.jbidibc.BidibLibrary;

public class AccessoryStateUtils {
    public enum ErrorAccessoryState {
        //@formatter:off
        NO_MORE_ERROR(0x00), COMMAND_NOT_EXECUTABLE_UNKNOWN_COMMAND_OR_ASPECT(0x01), POWER_CONSUMPTION_HIGH(0x02), POWER_SUPPLY_BELOW_LIMITS(
            0x03), FUSE_BLOWN(0x04), TEMPERATURE_TOO_HIGH(0x05), FEEDBACK_ERROR_UNWANTED_CHANGE_POSITION(0x06), MANUAL_CONTROL(
            0x07), BULB_OUT_OF_ORDER(0x10), SERVO_OUT_OF_ORDER(0x20), INTERNAL_ERROR(0x3F);
        //@formatter:on

        private final byte errorCode;

        ErrorAccessoryState(int errorCode) {
            this.errorCode = (byte) errorCode;
        }

        /**
         * Create a error accessory state.
         * 
         * @param wait
         *            numeric value of the error accessory state
         * 
         * @return ErrorAccessoryState
         */
        public static ErrorAccessoryState valueOf(byte wait) {
            ErrorAccessoryState result = null;

            for (ErrorAccessoryState e : values()) {
                if (e.errorCode == (wait & 0x3F)) {
                    result = e;
                    break;
                }
            }
            if (result == null) {
                throw new IllegalArgumentException("cannot map " + wait + " to a error accessory state");
            }
            return result;
        }

    }

    public static boolean hasError(byte execute) {
        return (execute & BidibLibrary.BIDIB_ACC_STATE_ERROR) == BidibLibrary.BIDIB_ACC_STATE_ERROR;
    }

    public static String getOperationResult(byte execute) {
        // TODO refactor this
        StringBuffer sb = new StringBuffer();
        switch (execute & 0x01) {
            case BidibLibrary.BIDIB_ACC_STATE_DONE: // done
                sb.append(" => Reached end position.");
                break;
            case BidibLibrary.BIDIB_ACC_STATE_WAIT: // wait
                sb.append(" => End position not yet reached. Check WAIT.");
                break;
            case BidibLibrary.BIDIB_ACC_STATE_NO_FB_AVAILABLE: // no feedback available
                sb.append(" => No feedback available.");
                break;
            default:
                sb.append(" => Unknown.");
                break;
        }
        switch (execute & 0x02) {
            case 0x00:
                sb.append(" => End position is verified by feedback.");
                break;
            case 0x02:
                sb.append(" => No control of end position possible.");
                break;
            default:
                sb.append(" => Unknown.");
                break;
        }
        return sb.toString();
    }

    public static boolean hasMoreErrors(byte wait) {
        return (wait & 0x40) == 0x40;
    }

    public static ErrorAccessoryState getErrorState(byte wait) {
        return ErrorAccessoryState.valueOf(wait);
    }
}
