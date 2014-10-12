package org.bidib.jbidibc.core.helpers;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultContext implements Context {
    private Map<String, Object> registry = new LinkedHashMap<>();

    @Override
    public Object register(String key, Object content) {
        return registry.put(key, content);
    }

    @Override
    public Object unregister(String key) {
        return registry.remove(key);
    }

    @Override
    public Object get(String key) {
        return registry.get(key);
    }

    @Override
    public <T> T get(String key, Class<T> type, T defaultValue) {
        T value = get(type, get(key));
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    protected <T> T get(Class<T> type, Object body) {
        // if same type
        if (type.isInstance(body)) {
            return type.cast(body);
        }

        return null;
    }

}
