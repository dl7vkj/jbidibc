package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.FeatureEnum;

public class Feature {
    private final int type;

    private final int value;

    public Feature(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getFeatureName() {
    	try {
    		return FeatureEnum.valueOf((byte) type).name();
    	}
    	catch(IllegalArgumentException ex) {
    		return String.valueOf(type);
    	}
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ",value=" + value + "]";
    }
}
