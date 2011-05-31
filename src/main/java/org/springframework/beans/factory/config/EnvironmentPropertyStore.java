package org.springframework.beans.factory.config;

/**
 * Using environment variables as a property store
 */
public class EnvironmentPropertyStore implements PropertyStore {
    public String get(String propertyNameOrKey) {
        return System.getenv(propertyNameOrKey);
    }
}
