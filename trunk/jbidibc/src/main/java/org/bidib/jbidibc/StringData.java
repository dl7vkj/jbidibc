package org.bidib.jbidibc;

public class StringData {
    public static final int INDEX_PRODUCTNAME = 0;

    public static final int INDEX_USERNAME = 1;

    public static final int NAMESPACE_NODE = 0;

    private int namespace;

    private int index;

    private String value;

    /**
     * @return the namespace
     */
    public int getNamespace() {
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(int namespace) {
        this.namespace = namespace;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[namespace=" + namespace + ", index=" + index + ",value=" + value + "]";
    }
}
