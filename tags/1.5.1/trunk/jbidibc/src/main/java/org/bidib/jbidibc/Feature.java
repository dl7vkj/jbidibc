package org.bidib.jbidibc;

import java.io.Serializable;

import org.bidib.jbidibc.enumeration.FeatureEnum;

public class Feature implements Serializable {
    private static final long serialVersionUID = 1L;

    private int type;

    private int value;

    public Feature() {
        type = -1;
    }

    public Feature(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
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
