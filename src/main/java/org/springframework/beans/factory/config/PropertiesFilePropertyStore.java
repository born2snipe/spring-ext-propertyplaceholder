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


import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * Using property file(s) as a store
 */
public class PropertiesFilePropertyStore implements PropertyStore, InitializingBean {
    private Properties properties = new Properties();
    private Resource[] locations;

    public String get(String propertyNameOrKey) {
        return properties.getProperty(propertyNameOrKey);
    }

    public void setLocation(Resource location) {
        setLocations(new Resource[]{location});
    }

    public void afterPropertiesSet() throws Exception {
        for (Resource location : locations) {
            properties.load(location.getInputStream());
        }
    }

    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }
}
