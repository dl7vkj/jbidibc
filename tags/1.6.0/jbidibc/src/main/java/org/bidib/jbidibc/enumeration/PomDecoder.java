package org.bidib.jbidibc.enumeration;

public enum PomDecoder {
    LOCO("loco"), ACCESSORY("accessory"), EXT_ACCESSORY("extAccessory"), DID("decoderId");

    private final String key;

    private PomDecoder(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
