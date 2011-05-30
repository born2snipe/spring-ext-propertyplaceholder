package org.springframework.beans.factory.config;


public class EnvironmentPropertyStore implements PropertyStore {
    public Object get(String propertyNameOrKey) {
        return System.getenv(propertyNameOrKey);
    }
}
