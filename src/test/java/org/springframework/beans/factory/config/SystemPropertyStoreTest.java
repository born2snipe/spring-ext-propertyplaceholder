package org.springframework.beans.factory.config;


import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class SystemPropertyStoreTest {
    private SystemPropertyStore store;

    @Before
    public void setUp() throws Exception {
        store = new SystemPropertyStore();
        System.setProperty("test", "test.value");
    }

    @Test
    public void emptyString() {
        assertNull(store.get(""));
    }

    @Test
    public void propertyWasNotFound() {
        assertNull(store.get("does.not.exist"));
    }

    @Test
    public void propertyWasFound() {
        assertEquals("test.value", store.get("test"));
    }
}
