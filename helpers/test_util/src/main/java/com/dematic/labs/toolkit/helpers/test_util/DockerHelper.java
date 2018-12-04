/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static java.time.ZonedDateTime.now;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Use to read the port(s) assigned by io.fabric8:maven-docker-plugin to the container(s) it has launched.
 */
@SuppressWarnings("WeakerAccess")
public class DockerHelper {
    private static final Logger LOGGER = getLogger(DockerHelper.class);

    public static final File PORTS_PROPERTIES = new File("target/ports.properties");

    @Nonnull
    public static String getBaseUrl(@Nonnull final String portPropertyName, @Nonnull final String applicationContext) {
        return "http://localhost:" + getPort(portPropertyName) + "/" + applicationContext + "/";
    }

    @Nonnull
    public static String getPort(@Nonnull String portPropertyName) {
        final String value = loadDockerProperties().getProperty(portPropertyName);
        if (value == null) {
            throw new IllegalArgumentException("Port " + portPropertyName + " has no defined value");
        }
        return value;
    }

    @Nonnull
    public static Properties loadDockerProperties() {
        final Properties properties = new Properties();
        try (final FileReader reader = new FileReader(PORTS_PROPERTIES)) {
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Error loading port mapping file " + PORTS_PROPERTIES.getAbsolutePath() + ": " + e, e);
        }
    }

    /**
     * @see #storeDockerProperties(Properties)
     */
    public static void storeDockerProperties(@Nonnull final Map<String, String> map) throws IOException {
        final Properties properties = new Properties();
        properties.putAll(map);
        storeDockerProperties(properties);
    }

    /**
     * Creates a {@link #PORTS_PROPERTIES} from code.
     * Logged as an error as this should not be used once code has been committed.
     */
    public static void storeDockerProperties(@Nonnull final Properties properties) throws IOException {
        LOGGER.error("Generating " + PORTS_PROPERTIES.getAbsolutePath());

        try (final FileWriter writer = new FileWriter(PORTS_PROPERTIES)) {
            properties.store(writer, "Manually generated at " + now());
        }
    }
}
