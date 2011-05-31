package org.springframework.beans.factory.config;


public interface PropertyStore {
    String get(String propertyNameOrKey);
}