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

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CompositePropertyStoreTest {
    @Mock
    private PropertyStore propertyStore;
    @Mock
    private PropertyStore otherPropertyStore;
    private CompositePropertyStore store;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        store = new CompositePropertyStore();
    }

    @Test
    public void multipleStores_firstStoreDoesNotHaveProperty() {
        store.setPropertyStores(Arrays.asList(propertyStore, otherPropertyStore));

        when(propertyStore.get("key")).thenReturn(null);
        when(otherPropertyStore.get("key")).thenReturn("value");

        assertEquals("value", store.get("key"));

    }

    @Test
    public void singleStore() {
        store.setPropertyStores(Arrays.asList(propertyStore));
        when(propertyStore.get("key")).thenReturn("value");

        assertEquals("value", store.get("key"));
    }

    @Test(expected = IllegalStateException.class)
    public void noStores() {
        store.get("value");
    }

    @Test(expected = IllegalStateException.class)
    public void storesSetToNull() {
        store.setPropertyStores(null);
        store.get("value");
    }
}
