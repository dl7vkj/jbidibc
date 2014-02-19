package org.bidib.jbidibc.message;

public interface BidibCommand {

    /**
     * @return the expected response types
     */
    Integer[] getExpectedResponseTypes();

    void setSendMsgNum(int sendMsgNum);

    byte getType();

    byte[] getData();
}
