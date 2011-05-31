package org.springframework.beans.factory.config;


public class EnvironmentPropertyStore implements PropertyStore {
    public String get(String propertyNameOrKey) {
        return System.getenv(propertyNameOrKey);
    }
}
