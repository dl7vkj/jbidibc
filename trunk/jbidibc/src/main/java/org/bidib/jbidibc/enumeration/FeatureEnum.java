package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

/**
 * The features
 */
public enum FeatureEnum implements BidibEnum {
    //@formatter:off
    // BELEGTMELDER
    FEATURE_BM_SIZE(BidibLibrary.FEATURE_BM_SIZE, 0, 128), FEATURE_BM_ON(BidibLibrary.FEATURE_BM_ON, 0, 1), FEATURE_BM_SECACK_AVAILABLE(
        BidibLibrary.FEATURE_BM_SECACK_AVAILABLE, 0, 1), FEATURE_BM_SECACK_ON(BidibLibrary.FEATURE_BM_SECACK_ON, 0,
        null), FEATURE_BM_CURMEAS_AVAILABLE(BidibLibrary.FEATURE_BM_CURMEAS_AVAILABLE, 0, 1), FEATURE_BM_CURMEAS_INTERVAL(
        BidibLibrary.FEATURE_BM_CURMEAS_INTERVAL, 0, null), FEATURE_BM_DC_MEAS_AVAILABLE(
        BidibLibrary.FEATURE_BM_DC_MEAS_AVAILABLE, 0, 1), FEATURE_BM_DC_MEAS_ON(BidibLibrary.FEATURE_BM_DC_MEAS_ON, 0,
        1), FEATURE_BM_ADDR_DETECT_AVAILABLE(BidibLibrary.FEATURE_BM_ADDR_DETECT_AVAILABLE, 0, 1), FEATURE_BM_ADDR_DETECT_ON(
        BidibLibrary.FEATURE_BM_ADDR_DETECT_ON, 0, 1), FEATURE_BM_ADDR_AND_DIR(BidibLibrary.FEATURE_BM_ADDR_AND_DIR, 0,
        1), FEATURE_BM_ISTSPEED_AVAILABLE(BidibLibrary.FEATURE_BM_ISTSPEED_AVAILABLE, 0, 1), FEATURE_BM_ISTSPEED_INTERVAL(
        BidibLibrary.FEATURE_BM_ISTSPEED_INTERVAL, 0, null), FEATURE_BM_CV_AVAILABLE(
        BidibLibrary.FEATURE_BM_CV_AVAILABLE, 0, 1), FEATURE_BM_CV_ON(BidibLibrary.FEATURE_BM_CV_ON, 0, 1),
    // BOOSTER
    FEATURE_BST_VOLT_ADJUSTABLE(BidibLibrary.FEATURE_BST_VOLT_ADJUSTABLE, 0, 1), FEATURE_BST_VOLT(
        BidibLibrary.FEATURE_BST_VOLT, 0, 250), FEATURE_BST_CUTOUT_AVAIALABLE(
        BidibLibrary.FEATURE_BST_CUTOUT_AVAIALABLE), FEATURE_BST_CUTOUT_ON(BidibLibrary.FEATURE_BST_CUTOUT_ON, 0, 1), FEATURE_BST_TURNOFF_TIME(
        BidibLibrary.FEATURE_BST_TURNOFF_TIME), FEATURE_BST_INRUSH_TURNOFF_TIME(
        BidibLibrary.FEATURE_BST_INRUSH_TURNOFF_TIME), FEATURE_BST_AMPERE_ADJUSTABLE(
        BidibLibrary.FEATURE_BST_AMPERE_ADJUSTABLE, 0, 1), FEATURE_BST_AMPERE(BidibLibrary.FEATURE_BST_AMPERE, 0, 250), FEATURE_BST_CURMEAS_INTERVAL(
        BidibLibrary.FEATURE_BST_CURMEAS_INTERVAL, 0, 250), FEATURE_BST_CV_AVAILABLE(
        BidibLibrary.FEATURE_BST_CV_AVAILABLE), FEATURE_BST_CV_ON(BidibLibrary.FEATURE_BST_CV_ON), FEATURE_BST_INHIBIT_AUTOSTART(
        BidibLibrary.FEATURE_BST_INHIBIT_AUTOSTART, 0, 1), FEATURE_BST_INHIBIT_LOCAL_ONOFF(
        BidibLibrary.FEATURE_BST_INHIBIT_LOCAL_ONOFF, 0, 1),
    // 28 - 40 missing
    // ACCESSORY
    FEATURE_ACCESSORY_COUNT(BidibLibrary.FEATURE_ACCESSORY_COUNT, 0, 127), FEATURE_ACCESSORY_SURVEILLED(
        BidibLibrary.FEATURE_ACCESSORY_SURVEILLED, 0, 1), FEATURE_ACCESSORY_MACROMAPPED(
        BidibLibrary.FEATURE_ACCESSORY_MACROMAPPED),
    // 43 - 99 missing
    // COMMAND STATION
    FEATURE_GEN_SPYMODE(BidibLibrary.FEATURE_GEN_SPYMODE, 0, 1), FEATURE_GEN_WATCHDOG(
        BidibLibrary.FEATURE_GEN_WATCHDOG, 0, 100), FEATURE_GEN_DRIVE_ACK(BidibLibrary.FEATURE_GEN_DRIVE_ACK, 0, 2), FEATURE_GEN_SWITCH_ACK(
        BidibLibrary.FEATURE_GEN_SWITCH_ACK, 0, 3), FEATURE_GEN_LOK_DB_SIZE(BidibLibrary.FEATURE_GEN_LOK_DB_SIZE), FEATURE_GEN_LOK_DB_STRING(
        BidibLibrary.FEATURE_GEN_LOK_DB_STRING), FEATURE_GEN_SERVICE_MODES(BidibLibrary.FEATURE_GEN_SERVICE_MODES), FEATURE_GEN_DRIVE_BUS(
        BidibLibrary.FEATURE_GEN_DRIVE_BUS, 0, 1), FEATURE_GEN_LOK_LOST_DETECT(
        BidibLibrary.FEATURE_GEN_LOK_LOST_DETECT, 0, 1), FEATURE_GEN_NOTIFY_DRIVE_MANUAL(
        BidibLibrary.FEATURE_GEN_NOTIFY_DRIVE_MANUAL, 0, 1),
    // FIRMWARE UPDATE
    FEATURE_FW_UPDATE_MODE(BidibLibrary.FEATURE_FW_UPDATE_MODE),
    // RELEVANT PID BITS
    FEATURE_RELEVANT_PID_BITS(BidibLibrary.FEATURE_RELEVANT_PID_BITS),
    // RELEVANT PID BITS
    FEATURE_STRING_SIZE(BidibLibrary.FEATURE_STRING_SIZE),

    // LC
    FEATURE_CTRL_INPUT_COUNT(BidibLibrary.FEATURE_CTRL_INPUT_COUNT, 0, 127), FEATURE_CTRL_INPUT_NOTIFY(
        BidibLibrary.FEATURE_CTRL_INPUT_NOTIFY, 0, 1), FEATURE_CTRL_SPORT_COUNT(BidibLibrary.FEATURE_CTRL_SPORT_COUNT,
        0, 127), FEATURE_CTRL_LPORT_COUNT(BidibLibrary.FEATURE_CTRL_LPORT_COUNT, 0, 127), FEATURE_CTRL_SERVO_COUNT(
        BidibLibrary.FEATURE_CTRL_SERVO_COUNT, 0, 127), FEATURE_CTRL_SOUND_COUNT(BidibLibrary.FEATURE_CTRL_SOUND_COUNT,
        0, 127), FEATURE_CTRL_MOTOR_COUNT(BidibLibrary.FEATURE_CTRL_MOTOR_COUNT, 0, 127), FEATURE_CTRL_ANALOG_COUNT(
        BidibLibrary.FEATURE_CTRL_ANALOG_COUNT, 0, 127), FEATURE_CTRL_STRETCH_DIMM(
        BidibLibrary.FEATURE_CTRL_STRETCH_DIMM, 1, 250), FEATURE_CTRL_MAC_SIZE(BidibLibrary.FEATURE_CTRL_MAC_SIZE), FEATURE_CTRL_MAC_COUNT(
        BidibLibrary.FEATURE_CTRL_MAC_COUNT), FEATURE_CTRL_MAC_LEVEL(BidibLibrary.FEATURE_CTRL_MAC_LEVEL, 0, 2), FEATURE_CTRL_MAC_START_DCC(
        BidibLibrary.FEATURE_CTRL_MAC_START_DCC, 0, 1), FEATURE_CTRL_MAC_START_MAN(
        BidibLibrary.FEATURE_CTRL_MAC_START_MAN, 0, 1), FEATURE_CTRL_MAC_SAVE(BidibLibrary.FEATURE_CTRL_MAC_SAVE), FEATURE_CTRL_PORT_QUERY_AVAILABLE(
        BidibLibrary.FEATURE_CTRL_PORT_QUERY_AVAILABLE, 0, 1);
    //@formatter:on

    private final byte type;

    private final Integer min;

    private final Integer max;

    FeatureEnum(int type) {
        this(type, null, null);
    }

    FeatureEnum(int type, Integer min, Integer max) {
        this.type = (byte) type;
        this.min = min;
        this.max = max;
    }

    public byte getType() {
        return type;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    /**
     * Create feature enum value from the provided byte value.
     * 
     * @param type
     *            numeric value of the feature
     * 
     * @return the feature enum value
     */
    public static FeatureEnum valueOf(byte type) {
        FeatureEnum result = null;

        for (FeatureEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a feature.");
        }
        return result;
    }

}
