package org.bidib.jbidibc;

/**
 * JNA Wrapper for library <b>bidib</b><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public interface BidibLibrary {
    /**
     * <i>native declaration : bidib_messages.h:33</i><br>
     * enum values
     */
    /**
     * <i>native declaration : bidib_messages.h:33</i><br>
     * enum values
     */
    public static interface t_bidib_macro_state {
        /// <i>native declaration : bidib_messages.h:29</i>
        public static final int BIDIB_MACRO_OFF = (int) 0;

        /// <i>native declaration : bidib_messages.h:30</i>
        public static final int BIDIB_MACRO_RUN = (int) 1;

        /// <i>native declaration : bidib_messages.h:31</i>
        public static final int BIDIB_MACRO_SAVE = (int) 254;

        /// <i>native declaration : bidib_messages.h:32</i>
        public static final int BIDIB_MACRO_DELETE = (int) 255;
    };

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_PRG_CV_BLOCKWRITE = (int) ((0 + 96) + 13);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_PROGBUSY = (int) 9;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_OP_DONE = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_GO = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_ADDRSTACK = (int) 17;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DBST = (int) (0 + 48);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_UBM = (int) (128 + 32);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_ON_HERE = (int) 132;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DFC = (int) (0 + 16);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_UBST = (int) (128 + 48);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_GET_PKT_CAPACITY = (int) ((0 + 0) + 10);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_CRC = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_CV = (int) ((128 + 32) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_LOK_DB_STRING = (int) 105;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_VENDOR_DISABLE = (int) ((0 + 16) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_CV_AVAILABLE = (int) 24;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_ADDR_DETECT_ON = (int) 9;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_TURN_ON_NEON = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_OFF_GO_REQ = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_SYS_MSG = (int) 64;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ACCESSORY_PARA_SET = (int) ((0 + 56) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_SW_VERSION = (int) ((128 + 0) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_WAIT = (int) ((128 + 64) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO_PARA = (int) ((128 + 72) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_STAT_READY = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_IDDOUBLE = (int) 18;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_FLAG_CLEAR = (int) 248;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_POM_ACK = (int) ((128 + 96) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_GO_IGN_WD = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_SECACK_AVAILABLE = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODE_CHANGED_ACK = (int) ((0 + 0) + 13);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_INPUT_QUERY0 = (int) 246;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_P_VERSION = (int) ((128 + 0) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_INRUSH_TURNOFF_TIME = (int) 20;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_INPUT_COUNT = (int) 50;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_F5F8_BIT = (int) (1 << 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_POWER_UPx = (int) 127;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_BLINK_A = (int) 5;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_DRIVE_ACK = (int) ((128 + 96) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_VERSION = (int) (0 * 256 + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_INPUT_QUERY1 = (int) 247;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_BLINK_B = (int) 6;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_FORMAT_DCC14 = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_PROG = (int) 8;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FW_UPDATE_STAT = (int) ((128 + 0) + 15);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_VOLT = (int) 16;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_NONE = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_ADDR_GET_RANGE = (int) ((0 + 32) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_DRIVE_ACK = (int) 102;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_MOTOR_COUNT = (int) 56;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_POM_RD_BLOCK = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_ERROR = (int) ((128 + 0) + 6);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_FW_UPDATE_ERROR_SIZE = (int) 5;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_PRG_CV_WRITE = (int) ((0 + 96) + 12);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_FORMAT_DCC28 = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_FW_UPDATE_ERROR_CHECKSUM = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_UNIQUE_ID = (int) ((128 + 0) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODE_LOST = (int) ((128 + 0) + 12);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_DRIVE_EVENT = (int) ((128 + 96) + 6);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_SUBCRC = (int) 19;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_STAT_ERROR = (int) 255;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_CURMEAS_AVAILABLE = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_AMPERE = (int) 22;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ACCESSORY_STATE = (int) ((128 + 56) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODE_NA = (int) ((128 + 0) + 11);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DACC = (int) (0 + 56);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_ALLOC_ACK = (int) ((128 + 96) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_UFC = (int) (128 + 16);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PKT_ESCAPE = (int) 253;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_RESET = (int) ((0 + 0) + 9);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_RESTORE = (int) 252;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_GET_ERROR = (int) ((0 + 0) + 14);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_MAC_COUNT = (int) 62;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_AMPERE_ADJUSTABLE = (int) 21;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_POM_WR_BYTE = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_DISABLE = (int) ((0 + 0) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ACCESSORY_SWITCH_TIME = (int) 254;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_CONFIG_SET = (int) ((0 + 64) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_OP_DATA = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_TURNOFF_TIME = (int) 19;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_PRG_CV_STAT = (int) ((128 + 96) + 12);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ACCESSORY_PARA_GET = (int) ((0 + 56) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODE_NEW = (int) ((128 + 0) + 13);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_VENDOR = (int) ((128 + 16) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_SYS_MAGIC = (int) 45054;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BOOST_CURRENT = (int) ((128 + 48) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_PARA_START_CLK = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_ACCESSORY_MACROMAPPED = (int) 42;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_F13F20_BIT = (int) (1 << 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_GET_P_VERSION = (int) ((0 + 0) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_CV_ON = (int) 25;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_KEY_QUERY = (int) ((0 + 64) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_STATE = (int) ((128 + 96) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODETAB_GETALL = (int) ((0 + 0) + 11);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_LOK_LOST_DETECT = (int) 108;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_BUS = (int) 16;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_MAC_START_DCC = (int) 65;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_DRIVE_BUS = (int) 107;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_PRG_CV_READ = (int) ((0 + 96) + 14);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODETAB = (int) ((128 + 0) + 9);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_ON = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_CURMEAS_INTERVAL = (int) 23;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_MIRROR_MULTIPLE = (int) ((0 + 32) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_RUNNING = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_F21F28_BIT = (int) (1 << 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_ADDR_AND_DIR = (int) 10;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FEATURE_SET = (int) ((0 + 16) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_DIAG_I = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_LOGON_par = (int) 126;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODETAB_COUNT = (int) ((128 + 0) + 8);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_LOK_DB_SIZE = (int) 104;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_VENDOR_SET = (int) ((0 + 16) + 6);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_F9F12_BIT = (int) (1 << 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LOGON_REJECTED = (int) ((0 + 112) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_OUTPUT = (int) ((0 + 64) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_POM_WR_BIT = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_BUSY = (int) 13;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_ON_HOT = (int) 130;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_STRETCH_DIMM = (int) 58;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_MAC_SAVE = (int) 61;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ACCESSORY_SET = (int) ((0 + 56) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_DIAG_T = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_F0F4_BIT = (int) (1 << 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_DIAG_V = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DSYS = (int) (0 + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_DRIVE = (int) ((0 + 96) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_UGEN = (int) (128 + 96);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_TURN_OFF = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_GET_SW_VERSION = (int) ((0 + 0) + 6);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_SIZE = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_OFF_HOT = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_SECACK_ON = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_GET_UNIQUE_ID = (int) ((0 + 0) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_END_OF_MACRO = (int) 255;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_ACCESSORY_SURVEILLED = (int) 41;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_NA = (int) ((128 + 64) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_CUTOUT_AVAIALABLE = (int) 17;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_STOP_MACRO = (int) 253;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_START_MACRO = (int) 254;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_SWITCH_ACK = (int) 103;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_xPOM_WR_BYTE = (int) 131;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DGEN = (int) (0 + 96);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_CONFIDENCE = (int) ((128 + 32) + 9);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_STAT = (int) ((128 + 64) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_FLAG_QUERY = (int) 250;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_KEY = (int) ((128 + 64) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_MAGIC = (int) ((128 + 0) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ID_SEARCH_ACK = (int) ((128 + 48) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LOGON_ACK = (int) ((0 + 112) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_MAC_START_MAN = (int) 64;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_DC_MEAS_AVAILABLE = (int) 6;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_OUTTYPE_LPORT = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_BEGIN_CRITCAL = (int) 252;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_MAC_SIZE = (int) 63;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_QUERY = (int) 255;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BOOST_OFF = (int) ((0 + 48) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FW_UPDATE_OP = (int) ((0 + 0) + 15);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_ADDR_DETECT_AVAILABLE = (int) 8;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_POM = (int) ((0 + 96) + 7);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DLC = (int) (0 + 64);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_END_CRITCAL = (int) 251;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_INHIBIT_AUTOSTART = (int) 26;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO = (int) ((128 + 72) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FEATURE_COUNT = (int) ((128 + 16) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_STOP = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_SOUND_COUNT = (int) 55;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DSTRM = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_SAVE = (int) 253;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_FW_UPDATE_ERROR_RECORD = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FEATURE_GETALL = (int) ((0 + 16) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_POWER_UPx_par = (int) 255;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FEATURE_GET = (int) ((0 + 16) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_PING = (int) ((0 + 0) + 7);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_DIMM_OFF = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ULC = (int) (128 + 64);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO_GET = (int) ((0 + 72) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_IDENTIFY = (int) ((0 + 0) + 8);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_INPUT_NOTIFY = (int) 51;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_SEQUENCE = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_MIRROR_OCC = (int) ((0 + 32) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_DRIVE_MANUAL = (int) ((128 + 96) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_LPORT_COUNT = (int) 53;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_FREE = (int) ((128 + 32) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_OFF = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_ALLOCATE = (int) ((0 + 96) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_ISTSPEED_INTERVAL = (int) 12;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_TURN_ON = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_EXTENSION = (int) 255;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_FW_UPDATE_ERROR_APPCRC = (int) 6;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_FLAG_SET = (int) 249;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_OCC = (int) ((128 + 32) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_VENDOR_GET = (int) ((0 + 16) + 7);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_QUERY = (int) 15;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_OFF_SHORT = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_STAT_EXIT = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_MIRROR_FREE = (int) ((0 + 32) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_SERVO_COUNT = (int) 54;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_OFF_NOPOWER = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LOCAL_PONG = (int) ((128 + 112) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_OP_SETDEST = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_OUTTYPE_MOTOR = (int) 4;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_TXT = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_ENABLE = (int) ((0 + 0) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_FW_UPDATE_MODE = (int) 254;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_STAT_DATA = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_F1F4_BIT = (int) (1 << 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_IDENTIFY_STATE = (int) ((128 + 0) + 7);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BOOST_ON = (int) ((0 + 48) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_ACCESSORY = (int) ((0 + 96) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_SPEED_BIT = (int) (1 << 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_NOTEXIST = (int) 255;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_USTRM = (int) 128;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_CURMEAS_INTERVAL = (int) 5;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_OFF = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_SUBPAKET = (int) 21;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_LOGON = (int) 126;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_ACCESSORY_COUNT = (int) 40;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_INHIBIT_LOCAL_ONOFF = (int) 27;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_SERVICE_MODES = (int) 106;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_PONG = (int) ((128 + 0) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_PARA_REPEAT = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_DRIVE_FORMAT_DCC128 = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_UMAC = (int) (128 + 72);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LOCAL_PING = (int) ((0 + 112) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO_PARA_GET = (int) ((0 + 72) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ADDR_CHANGE_ACK = (int) ((128 + 48) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_VENDOR_ACK = (int) ((128 + 16) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_CONFIG_GET = (int) ((0 + 64) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_OUTTYPE_SPORT = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BOOST_STAT = (int) ((128 + 48) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_MAC_LEVEL = (int) 60;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO_PARA_SET = (int) ((0 + 72) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_DELAY_FIXED = (int) 244;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_PKT_CAPACITY = (int) ((128 + 0) + 10);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_MULTIPLE = (int) ((128 + 32) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_WATCHDOG = (int) 101;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_POM_RD_BYTE = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_BUSY = (int) 125;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_OUTTYPE_SERVO = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_SPYMODE = (int) 100;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_SET_STATE = (int) ((0 + 96) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_ANALOG_COUNT = (int) 57;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_CUTOUT_ON = (int) 18;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_SIZE = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_xPOM_RD_BLOCK = (int) 128;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ULOCAL = (int) (128 + 112);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_OFF_NO_DCC = (int) 6;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_BUSY_par = (int) 125;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_NODE_READY = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO_HANDLE = (int) ((0 + 72) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_GET_CONFIDENCE = (int) ((0 + 32) + 5);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_DELETE = (int) 254;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ACCESSORY_GET = (int) ((0 + 56) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NEW_DECODER = (int) ((128 + 48) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_ISTSPEED_AVAILABLE = (int) 11;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_xPOM_RD_BYTE = (int) 129;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_ON_LIMIT = (int) 129;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_CURRENT = (int) ((128 + 32) + 7);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_ACCESSORY = (int) ((128 + 32) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BST_VOLT_ADJUSTABLE = (int) 15;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_UACC = (int) (128 + 56);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ACCESSORY_PARA_MACROMAP = (int) 253;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_BIN_STATE = (int) ((0 + 96) + 6);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_PARAMETER = (int) 5;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DMAC = (int) (0 + 72);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_FW_UPDATE_ERROR_ADDR = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_ACCESSORY_PARA = (int) ((128 + 56) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSYS_DELAY_RANDOM = (int) 245;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_GET_RANGE = (int) ((0 + 32) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_xPOM_WR_BIT = (int) 130;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_CV_ON = (int) 14;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_PARA_SLOWDOWN = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DBM = (int) (0 + 32);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BOOST_DIAGNOSTIC = (int) ((128 + 48) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_SOFTSTOP = (int) 2;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_VENDOR_ENABLE = (int) ((0 + 16) + 4);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LOGON = (int) ((128 + 112) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FEATURE_GETNEXT = (int) ((0 + 16) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_OP_EXIT = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO_SET = (int) ((0 + 72) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FEATURE_NA = (int) ((128 + 16) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_ADDRESS = (int) ((128 + 32) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_DOUBLE_FLASH = (int) 9;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_DLOCAL = (int) (0 + 112);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_CV_AVAILABLE = (int) 13;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_CS_STATE_OFF = (int) 0;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_CS_ACCESSORY_ACK = (int) ((128 + 96) + 3);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_ON_STOP_REQ = (int) 131;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_ON = (int) 128;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_MACRO_STATE = (int) ((128 + 72) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_FW_UPDATE_ERROR_NO_DEST = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_HW = (int) 32;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIBUS_NODE_BUSY = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_NODETAB_GETNEXT = (int) ((0 + 0) + 12);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_SPEED = (int) ((128 + 32) + 6);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_BST_STATE_OFF_HERE = (int) 5;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PKT_MAGIC = (int) 254;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_ERR_SUBTIME = (int) 20;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BOOST_QUERY = (int) ((0 + 48) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_GET_MAGIC = (int) ((0 + 0) + 1);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_FEATURE = (int) ((128 + 16) + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_SYS_CLOCK = (int) ((0 + 16) + 8);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_OUTTYPE_ANALOG = (int) 5;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_FLASH_A = (int) 7;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_FLASH_B = (int) 8;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_BM_BLOCK_CV = (int) ((128 + 32) + 8);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_LC_CONFIG = (int) ((128 + 64) + 2);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_PORT_DIMM_ON = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_CTRL_SPORT_COUNT = (int) 52;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_STALL = (int) ((128 + 0) + 14);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_USYS = (int) (128 + 0);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_BM_DC_MEAS_ON = (int) 7;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MACRO_START = (int) 1;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_OUTTYPE_SOUND = (int) 3;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int MSG_PRG_CV_BLOCKREAD = (int) ((0 + 96) + 15);

    /// <i>native declaration : bidib_messages.h</i>
    public static final int FEATURE_GEN_NOTIFY_DRIVE_MANUAL = (int) 109;

    /// <i>native declaration : bidib_messages.h</i>
    public static final int BIDIB_MSG_FW_UPDATE_OP_ENTER = (int) 0;
}
