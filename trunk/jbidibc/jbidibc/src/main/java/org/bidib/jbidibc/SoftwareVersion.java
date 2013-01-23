package org.bidib.jbidibc;

public class SoftwareVersion implements Comparable<SoftwareVersion> {
    private final int firstVersion;
    private final int secondVersion;
    private final int thirdVersion;

    public SoftwareVersion(int firstVersion, int secondVersion, int thirdVersion) {
        this.firstVersion = firstVersion;
        this.secondVersion = secondVersion;
        this.thirdVersion = thirdVersion;
    }

    private Integer toInt() {
        return (firstVersion << 16) + (secondVersion << 8) + thirdVersion;
    }

    public String toString() {
        return firstVersion + "." + secondVersion + "." + thirdVersion;
    }

    @Override
    public int compareTo(SoftwareVersion version) {
        return toInt().compareTo(version.toInt());
    }
}
