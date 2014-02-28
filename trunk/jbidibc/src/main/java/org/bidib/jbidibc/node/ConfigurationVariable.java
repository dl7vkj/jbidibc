package org.bidib.jbidibc.node;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationVariable implements Serializable, Comparable<ConfigurationVariable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationVariable.class);

    private static final long serialVersionUID = 1L;

    private String name;

    private String value;

    private boolean timeout;

    private boolean skipOnTimeout;

    private int minCvNumber;

    private int maxCvNumber;

    public ConfigurationVariable() {
    }

    /**
     * Creates a new instance of ConfigurationVariable
     * 
     * @param name
     *            the CV name
     * @param value
     *            the CV value
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
     * @param name
     *            the name to set
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
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the timeout
     */
    public boolean isTimeout() {
        return timeout;
    }

    /**
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the skipOnTimeout flag
     */
    public boolean isSkipOnTimeout() {
        return skipOnTimeout;
    }

    /**
     * @param skipOnTimeout
     *            the skipOnTimeout flag to set
     */
    public void setSkipOnTimeout(boolean skipOnTimeout) {
        this.skipOnTimeout = skipOnTimeout;
    }

    /**
     * @return the minCvNumber
     */
    public int getMinCvNumber() {
        return minCvNumber;
    }

    /**
     * @param minCvNumber
     *            the maxCvNumber to set
     */
    public void setMinCvNumber(int minCvNumber) {
        LOGGER.debug("Set the minCvNumber: {}", minCvNumber);
        this.minCvNumber = minCvNumber;
    }

    /**
     * @return the maxCvNumber
     */
    public int getMaxCvNumber() {
        return maxCvNumber;
    }

    /**
     * @param maxCvNumber
     *            the maxCvNumber to set
     */
    public void setMaxCvNumber(int maxCvNumber) {
        LOGGER.debug("Set the maxCvNumber: {}", maxCvNumber);
        this.maxCvNumber = maxCvNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigurationVariable) {
            ConfigurationVariable other = (ConfigurationVariable) obj;
            return name.equals(other.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(ConfigurationVariable o) {
        return name.compareTo(o.getName());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
