package org.bidib.jbidibc;

public interface ConnectionListener {

    /**
     * The communication was opened.
     * @param port the port identifier
     */
    void opened(String port);

    /**
     * The communication was closed.
     * @param port the port identifier
     */
    void closed(String port);
}
