package org.bidib.jbidibc;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a Node in BiDiB
 */
public class Node {
    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);

    private final byte[] addr;

    private final long uniqueId;

    private final int version;

    private int relevantPidBits;

    private boolean switchPortConfigAvailable;

    private String[] storedStrings;
    
    private SoftwareVersion softwareVersion;

    /**
     * the maximum length for strings that can be stored in the node
     */
    private int stringSize;

    protected Node(byte[] addr) {
        this(0, addr, 0);
    }

    /**
     * This constructor is used when the NodeTabResponse is evaluated.
     * @param version the version of the node
     * @param addr the address of the node 
     * @param uniqueId the uniqueId of the node
     */
    public Node(int version, byte[] addr, long uniqueId) {
        this.addr = addr != null ? addr.clone() : null;
        this.uniqueId = uniqueId;
        this.version = version;

        storedStrings = new String[2];

        LOGGER.debug("Created new node, addr: {}, version: {}, {}", addr, version, NodeUtils
            .getUniqueIdAsString(uniqueId));
    }

    /**
     * @return returns address of node
     */
    public byte[] getAddr() {
        return addr;
    }

    /**
     * @return returns the version of the node
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return returns the uniqueId of the node
     */
    public long getUniqueId() {
        return uniqueId;
    }

    /**
     * @return the relevantPidBits
     */
    public int getRelevantPidBits() {
        return relevantPidBits;
    }

    /**
     * @param relevantPidBits the relevantPidBits to set
     */
    public void setRelevantPidBits(int relevantPidBits) {
        this.relevantPidBits = relevantPidBits;
    }

    /**
     * @return the switchPortConfigAvailable
     */
    public boolean isSwitchPortConfigAvailable() {
        return switchPortConfigAvailable;
    }

    /**
     * @param switchPortConfigAvailable the switchPortConfigAvailable to set
     */
    public void setSwitchPortConfigAvailable(boolean switchPortConfigAvailable) {
        this.switchPortConfigAvailable = switchPortConfigAvailable;
    }

    /**
     * @return the stringSize
     */
    public int getStringSize() {
        return stringSize;
    }

    /**
     * @param stringSize the stringSize to set
     */
    public void setStringSize(int stringSize) {
        this.stringSize = stringSize;
    }

    public void setStoredString(int index, String value) {
        if (index < StringData.INDEX_PRODUCTNAME || index > StringData.INDEX_USERNAME) {
            throw new IllegalArgumentException("Index not allowed: " + index);
        }
        storedStrings[index] = value;
    }

    public String getStoredString(int index) {
        if (index < StringData.INDEX_PRODUCTNAME || index > StringData.INDEX_USERNAME) {
            throw new IllegalArgumentException("Index not allowed: " + index);
        }
        return storedStrings[index];
    }

    /**
     * @return <code>true</code> if the node has a stored string, <code>false</code> if the node has no stored strings
     */
    public boolean hasStoredStrings() {
        return StringUtils.isNotBlank(storedStrings[StringData.INDEX_PRODUCTNAME])
            || StringUtils.isNotBlank(storedStrings[StringData.INDEX_USERNAME]);
    }

    /**
     * @return the softwareVersion
     */
    public SoftwareVersion getSoftwareVersion() {
        return softwareVersion;
    }

    /**
     * @param softwareVersion the softwareVersion to set
     */
    public void setSoftwareVersion(SoftwareVersion softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Node) {
            Node node = (Node) other;
            if (ByteUtils.arrayEquals(node.getAddr(), getAddr()) && node.getUniqueId() == uniqueId) {
                LOGGER.debug("Found equal node: {}", node);
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (NodeUtils.convertAddress(addr) + getUniqueId() + version);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb
            .append("[version=").append(version).append(",addr=").append(Arrays.toString(addr)).append(",uniqueId=")
            .append(String.format("0x%014x", uniqueId & 0xffffffffffffffL));
        if (softwareVersion != null) {
            sb.append(",sw-version=").append(softwareVersion);
        }
        sb.append("]");
        return sb.toString();
    }
}
