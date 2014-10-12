package org.bidib.jbidibc.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DriveState {

    private int address;

    private int format;

    private int outputActive;

    private int speed;

    private int lights;

    public DriveState(int address, int format, int outputActive, int speed, int lights) {
        this.address = address;
        this.format = format;
        this.outputActive = outputActive;
        this.speed = speed;
        this.lights = lights;
    }

    /**
     * @return the address
     */
    public int getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(int address) {
        this.address = address;
    }

    /**
     * @return the format
     */
    public int getFormat() {
        return format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(int format) {
        this.format = format;
    }

    /**
     * @return the outputActive
     */
    public int getOutputActive() {
        return outputActive;
    }

    /**
     * @param outputActive
     *            the outputActive to set
     */
    public void setOutputActive(int outputActive) {
        this.outputActive = outputActive;
    }

    /**
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * @param speed
     *            the speed to set
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * @return the lights
     */
    public int getLights() {
        return lights;
    }

    /**
     * @param lights
     *            the lights to set
     */
    public void setLights(int lights) {
        this.lights = lights;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
