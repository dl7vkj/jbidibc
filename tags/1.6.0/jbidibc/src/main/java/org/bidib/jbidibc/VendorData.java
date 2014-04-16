package org.bidib.jbidibc;

public class VendorData {
    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + ",value=" + value + "]";
    }
}
