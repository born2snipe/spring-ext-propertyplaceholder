package org.springframework.beans.factory.config;


public interface PropertyValueManipulator {
    String manipulate(String value) throws ValueManipulationException;

    public static class ValueManipulationException extends RuntimeException {
        public ValueManipulationException(String s) {
            super(s);
        }

        public ValueManipulationException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }
}
