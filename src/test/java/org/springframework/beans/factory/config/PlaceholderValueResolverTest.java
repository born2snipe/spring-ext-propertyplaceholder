package org.springframework.beans.factory.config;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class PlaceholderValueResolverTest {
    @Mock
    private PropertyStore propertyStore;
    private StubPropertyValueManipulator propertyValueManipulator;
    private PlaceholderValueResolver resolver;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        propertyValueManipulator = new StubPropertyValueManipulator();

        resolver = new PlaceholderValueResolver(propertyStore, propertyValueManipulator, "${", "}");
    }

    @Test
    public void makeSureAllRegexCharactersAreSupported() {
        resolver = new PlaceholderValueResolver(propertyStore, propertyValueManipulator, "^([.+", "])|*");
        when(propertyStore.get("key")).thenReturn("value");

        assertEquals("value", resolver.resolveStringValue("^([.+key])|*"));
    }

    @Test
    public void multiplePlaceholders_notAllFound() {
        when(propertyStore.get("value")).thenReturn("stuff");
        when(propertyStore.get("value2")).thenReturn(null);
        assertEquals("${value2} stuff", resolver.resolveStringValue("${value2} ${value}"));
    }

    @Test
    public void multiplePlaceholders_allFound() {
        when(propertyStore.get("value")).thenReturn("stuff");
        when(propertyStore.get("value2")).thenReturn("cool");
        assertEquals("cool stuff", resolver.resolveStringValue("${value2} ${value}"));
    }

    @Test
    public void placeholderIsOnlyPartOfTheString() {
        when(propertyStore.get("value")).thenReturn("stuff");
        assertEquals("cool stuff", resolver.resolveStringValue("cool ${value}"));
    }

    @Test
    public void invalidPlaceholder_onlyHasTheBeginning() {
        assertEquals("${value", resolver.resolveStringValue("${value"));
    }

    @Test
    public void wholeValueIsAPlaceholder() {
        when(propertyStore.get("key")).thenReturn("value");

        assertEquals("value", resolver.resolveStringValue("${key}"));
    }

    @Test
    public void hasPlaceholderAndIsManipulated() {
        propertyValueManipulator.newValue = "diff";
        when(propertyStore.get("key")).thenReturn("value");

        assertEquals("diff", resolver.resolveStringValue("${key}"));
    }

    @Test
    public void stringDoesNotHaveAPlaceholder() {
        assertEquals("value", resolver.resolveStringValue("value"));
    }

    @Test
    public void stringDoesNotHaveAPlaceholderButCanBeManipulated() {
        propertyValueManipulator.newValue = "newValue";

        assertEquals("newValue", resolver.resolveStringValue("value"));
    }

    private class StubPropertyValueManipulator implements PropertyValueManipulator {
        String newValue;

        public String manipulate(String value) throws ValueManipulationException {
            return newValue == null ? value : newValue;
        }
    }
}
