package org.bidib.jbidibc;

public class ProtocolVersion implements Comparable<ProtocolVersion> {
    private final int firstVersion;

    private final int secondVersion;

    public ProtocolVersion(int firstVersion, int secondVersion) {
        this.firstVersion = firstVersion;
        this.secondVersion = secondVersion;
    }

    private Integer toInt() {
        return (firstVersion << 8) + secondVersion;
    }

    public String toString() {
        return firstVersion + "." + secondVersion;
    }

    @Override
    public int compareTo(ProtocolVersion version) {
        return toInt().compareTo(version.toInt());
    }
}
