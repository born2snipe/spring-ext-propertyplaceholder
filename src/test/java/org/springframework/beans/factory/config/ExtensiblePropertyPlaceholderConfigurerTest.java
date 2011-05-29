package org.springframework.beans.factory.config;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.*;

public class ExtensiblePropertyPlaceholderConfigurerTest {
    private static final String PROPERTY_NAME = "property.name";
    private static final String PROPERTY_VALUE = "property.value";
    private static final String OTHER_PROPERTY_NAME = "other.property.name";
    private static final String OTHER_PROPERTY_VALUE = "other.property.value";

    private ExtensiblePropertyPlaceholderConfigurer configurer;
    @Mock
    private ConfigurableListableBeanFactory beanFactory;
    @Mock
    private PropertyStore propertyStore;
    @Mock
    private PropertyStore otherPropertyStore;
    @Mock
    private BeanPicker beanPicker;
    @Mock
    private BeanStore beanStore;
    @Mock
    private BeanDefinition beanDefinition;
    @Mock
    private PropertySetter propertySetter;
    private List<BeanDefinition> beans;
    @Mock
    private PropertyValueManipulator propertyValueManipulator;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        beans = Arrays.asList(beanDefinition);

        configurer = new ExtensiblePropertyPlaceholderConfigurer(beanPicker, propertySetter);
        configurer.setPropertyStore(propertyStore);

        when(beanPicker.pick(beanFactory, "${", "}")).thenReturn(beanStore);
        when(beanStore.getBeansFor(PROPERTY_NAME)).thenReturn(beans);
        when(beanStore.getAllPropertyPlaceholders()).thenReturn(Arrays.asList(PROPERTY_NAME));
    }

    @Test
    public void placeholderPrefixAndSuffixCanBeOverriden() {
        BeanStore otherBeanStore = mock(BeanStore.class, "other");

        String prefix = "#(";
        String suffix = ")#";
        configurer.setPlaceholderPrefix(prefix);
        configurer.setPlaceholderSuffix(suffix);

        when(beanPicker.pick(beanFactory, prefix, suffix)).thenReturn(otherBeanStore);
        when(propertyStore.get(OTHER_PROPERTY_NAME)).thenReturn(OTHER_PROPERTY_VALUE);
        when(otherBeanStore.getAllPropertyPlaceholders()).thenReturn(Arrays.asList(OTHER_PROPERTY_NAME));
        when(otherBeanStore.getBeansFor(OTHER_PROPERTY_NAME)).thenReturn(beans);

        configurer.postProcessBeanFactory(beanFactory);

        verify(propertySetter).set(OTHER_PROPERTY_NAME, OTHER_PROPERTY_VALUE, beans);
    }

    @Test
    public void propertyValueGetsManipulatedBeforeBeingSetOnTheBeans() {
        configurer.setPropertyValueManipulator(propertyValueManipulator);

        when(propertyStore.get(PROPERTY_NAME)).thenReturn(PROPERTY_VALUE);
        when(propertyValueManipulator.manipulate(PROPERTY_VALUE)).thenReturn(OTHER_PROPERTY_VALUE);

        configurer.postProcessBeanFactory(beanFactory);

        verify(propertySetter).set(PROPERTY_NAME, OTHER_PROPERTY_VALUE, beans);
    }

    @Test
    public void canNotLocatePropertyNameInPropertyStore() {
        when(propertyStore.get(PROPERTY_NAME)).thenReturn(null);

        try {
            configurer.postProcessBeanFactory(beanFactory);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Can not locate property '" + PROPERTY_NAME + "'", e.getMessage());
        }
    }

    @Test
    public void singlePropertyPlaceholder() {
        when(propertyStore.get(PROPERTY_NAME)).thenReturn(PROPERTY_VALUE);

        configurer.postProcessBeanFactory(beanFactory);

        verify(propertySetter).set(PROPERTY_NAME, PROPERTY_VALUE, beans);
    }

    @Test
    public void multiplePropertyPlaceholders() {
        when(beanStore.getAllPropertyPlaceholders()).thenReturn(Arrays.asList(PROPERTY_NAME, OTHER_PROPERTY_NAME));
        when(propertyStore.get(PROPERTY_NAME)).thenReturn(PROPERTY_VALUE);
        when(propertyStore.get(OTHER_PROPERTY_NAME)).thenReturn(OTHER_PROPERTY_VALUE);
        when(beanStore.getBeansFor(OTHER_PROPERTY_NAME)).thenReturn(beans);

        configurer.postProcessBeanFactory(beanFactory);

        verify(propertySetter).set(PROPERTY_NAME, PROPERTY_VALUE, beans);
        verify(propertySetter).set(OTHER_PROPERTY_NAME, OTHER_PROPERTY_VALUE, beans);
    }
}
