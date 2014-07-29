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

    /**
     * Create a input enum.
     * 
     * @param type
     *            numeric value of the input enum
     * 
     * @return InputEnum
     */
    public static Feature valueOf(String key, int value) {
        Feature result = null;

        FeatureEnum featureEnum = null;
        for (FeatureEnum e : FeatureEnum.values()) {
            if (e.name().equals(key)) {
                featureEnum = e;
                break;
            }
        }

        if (featureEnum != null) {
            // TODO add missing implementation
        }
        else {
            result = new Feature(Integer.valueOf(key), value);
        }

        if (result == null) {
            throw new IllegalArgumentException("cannot map " + key + " to a feature");
        }
        return result;
    }

}
