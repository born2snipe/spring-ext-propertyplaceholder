package org.springframework.beans.factory.config;


public class SystemPropertyStore implements PropertyStore {
    public String get(String propertyNameOrKey) {
        if ("".equals(propertyNameOrKey)) {
            return null;
        }
        return System.getProperty(propertyNameOrKey);
    }
}
