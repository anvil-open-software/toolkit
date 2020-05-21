/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;

import static com.dematic.labs.toolkit.helpers.test_util.DockerHelper.PORTS_PROPERTIES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DockerHelperTest {
    @BeforeEach
    @AfterEach
    public void ensureNoPortFile() throws IOException {
        Files.deleteIfExists(PORTS_PROPERTIES.toPath());
    }

    @Test
    public void loadDockerPropertiesShouldFailWhenNoFile() {
        assertThrows(IllegalStateException.class,
            DockerHelper::loadDockerProperties);
    }

    @Test
    public void getPortShouldFailWhenNoFile() {
        assertThrows(IllegalStateException.class,
            () -> DockerHelper.getPort("port"));
    }

    @Test
    public void getBaseUrlShouldFailWhenNoFile() {
        assertThrows(IllegalStateException.class,
            () -> DockerHelper.getBaseUrl("port", "context"));
    }

    @Test
    public void loadDockerPropertiesShouldLoadExistingFile() throws IOException {
        final var expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertEquals(expected, DockerHelper.loadDockerProperties());

    }

    @Test
    public void getPortShouldFailWhenNoValue() throws IOException {
        final var expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertThrows(IllegalArgumentException.class,
            () -> DockerHelper.getPort("p0"));
    }

    @Test
    public void getPortShouldUseExistingValue() throws IOException {
        final var expected = Map.of("p1", "1234", "p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertEquals(expected.get("p1"), DockerHelper.getPort("p1"));
    }

    @Test
    public void getBaseUrlShouldFailWhenNoValue() throws IOException {
        final var expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertThrows(IllegalArgumentException.class,
            () -> DockerHelper.getBaseUrl("p0", "context"));

    }

    @Test
    public void getBaseUrlShouldUseValueFromExistingFile() throws IOException {
        final var expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertEquals("http://localhost:5678/context/", DockerHelper.getBaseUrl("p2", "context"));

    }

}
