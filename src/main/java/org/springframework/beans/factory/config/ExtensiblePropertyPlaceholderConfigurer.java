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


import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Method;

public class ExtensiblePropertyPlaceholderConfigurer implements BeanFactoryPostProcessor, BeanNameAware, BeanFactoryAware {
    private static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private PropertyStore propertyStore;
    private PropertyValueManipulator propertyValueManipulator = new NoOpPropertyValueManipulator();
    private String placeholderPrefix = DEFAULT_PLACEHOLDER_PREFIX;
    private String placeholderSuffix = DEFAULT_PLACEHOLDER_SUFFIX;
    private BeanFactory beanFactory;
    private String beanName;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactoryToProcess) throws BeansException {
        StringValueResolver valueResolver = new PlaceholderValueResolver(propertyStore, propertyValueManipulator, placeholderPrefix, placeholderSuffix);

        /**
         * Everything below here is a direct copy-n-paste from the PropertyPlaceholderConfigurer
         */
        BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);
        String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
        for (String curName : beanNames) {
            if (!(curName.equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
                BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(curName);
                try {
                    visitor.visitBeanDefinition(bd);
                } catch (Exception ex) {
                    throw new BeanDefinitionStoreException(bd.getResourceDescription(), curName, ex.getMessage());
                }
            }
        }

        // New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
        beanFactoryToProcess.resolveAliases(valueResolver);

        addEmbeddedValueResolver(beanFactoryToProcess, valueResolver);
    }

    private void addEmbeddedValueResolver(ConfigurableListableBeanFactory beanFactoryToProcess, StringValueResolver valueResolver) {
        // New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
        Method method = BeanUtils.findMethod(ConfigurableListableBeanFactory.class, "addEmbeddedValueResolver", StringValueResolver.class);
        if (method != null) {
            try {
                method.invoke(beanFactoryToProcess, valueResolver);
            } catch (Exception e) {
                throw new RuntimeException(e);
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

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setBeanName(String name) {
        beanName = name;
    }

    private class NoOpPropertyValueManipulator implements PropertyValueManipulator {
        public String manipulate(String value) {
            return value;
        }
    }
}
