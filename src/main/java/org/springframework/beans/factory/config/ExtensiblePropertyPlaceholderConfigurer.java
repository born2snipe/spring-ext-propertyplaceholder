package org.springframework.beans.factory.config;


import org.springframework.beans.BeansException;

import java.text.MessageFormat;
import java.util.List;

public class ExtensiblePropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {
    private static final String MANIPULATION_PROBLEM = "A problem occurred when trying to manipulate property value for: {0}";
    private static final String PROPERTY_NOT_FOUND = "Can not locate property ''{0}''";
    private static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private BeanPicker beanPicker;
    private PropertySetter propertySetter;
    private PropertyStore propertyStore;
    private PropertyValueManipulator propertyValueManipulator = new NoOpPropertyValueManipulator();
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;

    public ExtensiblePropertyPlaceholderConfigurer(BeanPicker beanPicker, PropertySetter propertySetter) {
        this.beanPicker = beanPicker;
        this.propertySetter = propertySetter;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanStore beanStore = beanPicker.pick(beanFactory, placeholderPrefix, placeholderSuffix);
        List<String> propertyPlaceholders = beanStore.getAllPropertyPlaceholders();
        for (String propertyPlaceholder : propertyPlaceholders) {
            Object propertyValue = propertyStore.get(propertyPlaceholder);
            if (propertyValue == null) {
                throw new IllegalStateException(MessageFormat.format(PROPERTY_NOT_FOUND, propertyPlaceholder));
            }

            try {
                Object manipulatedValue = propertyValueManipulator.manipulate(propertyValue);
                propertySetter.set(propertyPlaceholder, manipulatedValue, beanStore.getBeansFor(propertyPlaceholder));
            } catch (PropertyValueManipulator.ValueManipulationException e) {
                throw new IllegalArgumentException(MessageFormat.format(MANIPULATION_PROBLEM, propertyPlaceholder), e);
            }
        }
    }

    public void setPropertyStore(PropertyStore propertyStore) {
        this.propertyStore = propertyStore;
    }

    public void setPropertyValueManipulator(PropertyValueManipulator propertyValueManipulator) {
        this.propertyValueManipulator = propertyValueManipulator;
    }

    public void setPlaceholderPrefix(String placeholderPrefix) {
        this.placeholderPrefix = placeholderPrefix;
    }

    public void setPlaceholderSuffix(String placeholderSuffix) {
        this.placeholderSuffix = placeholderSuffix;
    }

    private class NoOpPropertyValueManipulator implements PropertyValueManipulator {
        public Object manipulate(Object value) {
            return value;
        }
    }
}
