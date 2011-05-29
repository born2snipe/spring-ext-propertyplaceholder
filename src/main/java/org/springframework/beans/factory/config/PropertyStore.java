package org.springframework.beans.factory.config;


public interface PropertyStore {
    Object get(String propertyNameOrKey);
}