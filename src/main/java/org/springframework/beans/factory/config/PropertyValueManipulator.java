package org.springframework.beans.factory.config;


public interface PropertyValueManipulator {
    Object manipulate(Object value);
}
