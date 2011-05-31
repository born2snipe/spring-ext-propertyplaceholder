package org.springframework.beans.factory.config;


import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows embedding environment variables in property value
 * <p/>
 * example:
 * <p/>
 * key=file:${env.JAVA_HOME}/bin/java
 */
public class EnvironmentVariablePropertyValueManipulator implements PropertyValueManipulator {
    private static final String NOT_FOUND = "Could not locate environment variable ''{0}''";
    private static final Pattern ENV_PATTERN = Pattern.compile("\\$\\{env\\.(.+?)\\}", Pattern.CASE_INSENSITIVE);
    private VariableAccessor variableAccessor;

    public EnvironmentVariablePropertyValueManipulator() {
        this(new VariableAccessor());
    }

    protected EnvironmentVariablePropertyValueManipulator(VariableAccessor variableAccessor) {
        this.variableAccessor = variableAccessor;
    }

    public String manipulate(String value) {
        String result = value.toString();
        Matcher matcher = ENV_PATTERN.matcher(result);
        int offset = 0;
        while (matcher.find(offset)) {
            String placeholder = matcher.group(0);
            String envVariableName = matcher.group(1);

            String env = variableAccessor.get(envVariableName);
            if (env == null) {
                throw new ValueManipulationException(MessageFormat.format(NOT_FOUND, envVariableName));
            }

            result = result.replace(placeholder, env);
            offset = matcher.end(0);
        }
        return result;
    }

    static class VariableAccessor {
        public String get(String name) {
            return System.getenv(name);
        }
    }
}
