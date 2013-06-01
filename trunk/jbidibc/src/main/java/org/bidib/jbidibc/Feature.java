package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.FeatureEnum;

public class Feature {
    private final int type;

    private int value;

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
        catch (IllegalArgumentException ex) {
            return String.valueOf(type);
        }
    }

    public FeatureEnum getFeatureEnum() {
        try {
            return FeatureEnum.valueOf((byte) type);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Feature) {
            return getType() == ((Feature) obj).getType();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getType();
    }

    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ",value=" + value + "]";
    }
}
