package org.springframework.beans.factory.config;


public class SystemPropertyStore implements PropertyStore {
    public Object get(String propertyNameOrKey) {
        return System.getProperty(propertyNameOrKey);
    }
}
