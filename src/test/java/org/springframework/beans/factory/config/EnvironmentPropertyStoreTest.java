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
    public void propertyDoesNotExist() {
        assertNull(store.get("does.not.exist"));
    }

    @Test
    public void propertyExists() {
        assertEquals(System.getenv("PATH"), store.get("PATH"));
    }
}
