/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
