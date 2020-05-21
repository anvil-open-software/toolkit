/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.System.clearProperty;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Paths.get;


/**
 * load system properties from the junit.properties file
 */
public final class SystemPropertyRule implements AfterTestExecutionCallback, BeforeTestExecutionCallback {
    private final Map<String, String> previousValues = new HashMap<>();

    @Override
    public void beforeTestExecution(final ExtensionContext context) throws IOException {
        final var junitPropertiesPath = get(getProperty("user.home"), ".m2", "junit.properties");
        if (!Files.exists(junitPropertiesPath)) {
            return;
        }
        try (final var inputStream = newInputStream(junitPropertiesPath)) {
            final var junitProperties = new Properties();
            junitProperties.load(inputStream);
            for (final var propertyKey : junitProperties.stringPropertyNames()) {
                if (propertyKey.startsWith("nexus")) {
                    continue;
                }
                put(propertyKey, junitProperties.getProperty(propertyKey));
            }
        }
    }

    public void put(@Nonnull final String propertyKey, @Nonnull final String propertyValue) {
        previousValues.put(propertyKey, setProperty(propertyKey, propertyValue));
    }

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        for (final var property : previousValues.entrySet()) {
            if (property.getValue() == null) {
                clearProperty(property.getKey());
            } else {
                setProperty(property.getKey(), property.getValue());
            }
        }
    }

}
