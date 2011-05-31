package org.springframework.beans.factory.config;


import org.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderValueResolver implements StringValueResolver {
    private PropertyStore propertyStore;
    private PropertyValueManipulator propertyValueManipulator;
    private String placeholderPrefix;
    private String placeholderSuffix;
    private Pattern placeholderPattern;

    public PlaceholderValueResolver(PropertyStore propertyStore, PropertyValueManipulator propertyValueManipulator, String placeholderPrefix, String placeholderSuffix) {
        this.propertyStore = propertyStore;
        this.propertyValueManipulator = propertyValueManipulator;
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
        this.placeholderPattern = Pattern.compile(escapeForRegex(placeholderPrefix) + "(.+?)" + escapeForRegex(placeholderSuffix));
    }

    public String resolveStringValue(String value) {
        String newValue = value;
        if (hasPlaceholder(value)) {
            for (String placeholder : placeholders(value)) {
                String propertyValue = lookupPlaceholder(placeholder);
                if (propertyValue != null) {
                    newValue = newValue.replace(placeholder, propertyValue);
                }
            }
        }
        return propertyValueManipulator.manipulate(newValue);
    }

    private List<String> placeholders(String value) {
        List<String> placeholders = new ArrayList<String>();
        Matcher matcher = placeholderPattern.matcher(value);
        int offset = 0;
        while (matcher.find(offset)) {
            placeholders.add(matcher.group(0));
            offset = matcher.end(0);
        }
        return placeholders;
    }

    private boolean hasPlaceholder(String value) {
        return !placeholders(value).isEmpty();
    }

    private String lookupPlaceholder(String value) {
        return propertyStore.get(value.replace(placeholderPrefix, "").replace(placeholderSuffix, ""));
    }

    private String escapeForRegex(String text) {
        return text.replace("$", "\\$")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("^", "\\^")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("|", "\\|")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace(".", "\\.")
                .replace("*", "\\*")
                .replace("+", "\\+");
    }
}
