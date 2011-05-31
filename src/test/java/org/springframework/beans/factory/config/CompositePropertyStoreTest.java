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
