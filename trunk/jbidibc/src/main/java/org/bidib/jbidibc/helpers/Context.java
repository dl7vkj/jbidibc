package org.bidib.jbidibc.helpers;

public interface Context {

    /**
     * Register a value under the specified key.
     * 
     * @param key
     *            the key
     * @param content
     *            the value
     * @return the previous assigned value or <tt>null</tt> if no value was stored under this key
     */
    Object register(String key, Object content);

    /**
     * Unregister the value that is stored under the specified key.
     * 
     * @param key
     *            the key
     * @return the stored value or <tt>null</tt> if no value was stored under this key
     */
    Object unregister(String key);

    /**
     * Returns the stored value.
     * 
     * @param key
     *            the key
     * @return the value stored under the specified key or <tt>null</tt> if no value is available
     */
    Object get(String key);

    /**
     * Returns the stored value and specified type.
     * 
     * @param key
     *            the key
     * @param type
     *            the type of the value
     * @param defaultValue
     *            the default value
     * @return the value stored under the specified key or <tt>null</tt> if no value is available or the value is not of
     *         the specified type
     */
    <T> T get(String key, Class<T> type, T defaultValue);
}
