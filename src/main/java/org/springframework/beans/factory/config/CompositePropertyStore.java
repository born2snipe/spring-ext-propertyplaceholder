package org.springframework.beans.factory.config;


import java.util.ArrayList;
import java.util.List;

public class CompositePropertyStore implements PropertyStore {
    private List<PropertyStore> propertyStores = new ArrayList<PropertyStore>();

    public String get(String propertyNameOrKey) {
        if (propertyStores.isEmpty()) {
            throw new IllegalStateException("No stores configured -- programmer error");
        }

        for (PropertyStore propertyStore : propertyStores) {
            String value = propertyStore.get(propertyNameOrKey);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    public void setPropertyStores(List<PropertyStore> propertyStores) {
        if (propertyStores != null) this.propertyStores = propertyStores;
    }
}
