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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class EnvironmentPropertyStoreTest {
    private EnvironmentPropertyStore store;

    @Before
    public void setUp() throws Exception {
        store = new EnvironmentPropertyStore();
    }

    @Test
    public void emptyString() {
        assertNull(store.get(""));
    }

    @Test
    public void propertyDoesNotExist() {
        assertNull(store.get("does.not.exist"));
    }

    @Test
    public void propertyExists() {
        assertEquals(System.getenv("PATH"), store.get("PATH"));
    }
}
