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
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class PropertiesFilePropertyStoreTest {
    private PropertiesFilePropertyStore store;

    @Before
    public void setUp() throws Exception {
        store = new PropertiesFilePropertyStore();
    }

    @Test
    public void multipleFiles() throws Exception {
        InputStreamResource file1 = new InputStreamResource(new ByteArrayInputStream("key=value".getBytes()));
        InputStreamResource file2 = new InputStreamResource(new ByteArrayInputStream("key2=value2".getBytes()));
        store.setLocations(new Resource[]{file1, file2});
        store.afterPropertiesSet();

        assertEquals("value", store.get("key"));
        assertEquals("value2", store.get("key2"));
    }

    @Test
    public void singleFile() throws Exception {
        store.setLocation(new InputStreamResource(new ByteArrayInputStream("key=value".getBytes())));
        store.afterPropertiesSet();

        assertEquals("value", store.get("key"));
        assertNull(store.get("does.not.exist"));
    }
}
