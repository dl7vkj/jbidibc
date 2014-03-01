package org.bidib.jbidibc.core;

public interface Context {

    void addParam(String key, Object value);

    Object getParam(String key);
}
