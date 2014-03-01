package org.bidib.jbidibc.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DefaultContext implements Context {

    private final Map<String, Object> params = new HashMap<String, Object>();

    public DefaultContext() {

    }

    @Override
    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    @Override
    public Object getParam(String key) {
        return params.get(key);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
