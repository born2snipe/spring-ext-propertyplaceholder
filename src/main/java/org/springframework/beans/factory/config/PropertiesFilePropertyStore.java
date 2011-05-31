package org.springframework.beans.factory.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.util.Properties;

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
