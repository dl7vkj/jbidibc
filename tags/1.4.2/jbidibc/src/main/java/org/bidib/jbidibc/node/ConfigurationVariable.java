package org.bidib.jbidibc.node;

import java.io.Serializable;

public class ConfigurationVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String value;

    public ConfigurationVariable() {
    }

    /**
     * Creates a new instance of ConfigurationVariable
     * @param name the CV name
     * @param value the CV value
     */
    public ConfigurationVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
