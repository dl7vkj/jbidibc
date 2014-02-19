package org.bidib.jbidibc.message;

public interface BidibCommand {

    /**
     * @return the expected response types
     */
    Integer[] getExpectedResponseTypes();

    void setSendMsgNum(int sendMsgNum);

    byte[] getAddr();

    byte getType();

    byte[] getData();

    /**
     * @return the answerSize
     */
    int getAnswerSize();

    /**
     * @param answerSize the answerSize to set
     */
    void setAnswerSize(int answerSize);
}
