package org.springframework.beans.factory.config;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

public class EnvironmentVariablePropertyValueManipulatorTest {
    private EnvironmentVariablePropertyValueManipulator manipulator;
    @Mock
    private EnvironmentVariablePropertyValueManipulator.VariableAccessor variableAccessor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        manipulator = new EnvironmentVariablePropertyValueManipulator(variableAccessor);
    }

    @Test
    public void blowUpIfTheEnironmentVariableIsNull() {
        when(variableAccessor.get("BOOM")).thenReturn(null);
        try {
            manipulator.manipulate("${env.BOOM}");
            fail();
        } catch (PropertyValueManipulator.ValueManipulationException e) {
            assertEquals("Could not locate environment variable 'BOOM'", e.getMessage());
        }
    }

    @Test
    public void replaceMultipleEnvsInTheSameString() {
        when(variableAccessor.get("1")).thenReturn("one");
        when(variableAccessor.get("2")).thenReturn("two");

        assertEquals("one two", manipulator.manipulate("${env.1} ${env.2}"));
    }

    @Test
    public void doesNotAlterAnyOtherPartOfTheValue() {
        when(variableAccessor.get("TEST")).thenReturn("safety");
        assertEquals("environmental safety", manipulator.manipulate("environmental ${env.TEST}"));
    }

    @Test
    public void environmentVariable_uppercaseEnv() {
        when(variableAccessor.get("TEST")).thenReturn("value");
        assertEquals("value", manipulator.manipulate("${ENV.TEST}"));
    }

    @Test
    public void environmentVariable_lowercaseEnv() {
        when(variableAccessor.get("TEST")).thenReturn("value");
        assertEquals("value", manipulator.manipulate("${env.TEST}"));
    }

    @Test
    public void noEnvironmentVariable() {
        assertEquals("does not contain an environment variable", manipulator.manipulate("does not contain an environment variable"));
    }
}
