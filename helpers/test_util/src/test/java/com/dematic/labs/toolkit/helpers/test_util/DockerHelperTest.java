/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.dematic.labs.toolkit.helpers.test_util.DockerHelper.PORTS_PROPERTIES;
import static org.junit.Assert.assertEquals;

public class DockerHelperTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    @Rule
    public final TestName testName = new TestName();

    @Before
    @After
    public void ensureNoPortFile() throws IOException {
        Files.deleteIfExists(PORTS_PROPERTIES.toPath());
    }

    @Test
    public void loadDockerPropertiesShouldFailWhenNoFile() {
        expectedException.expect(IllegalStateException.class);
        DockerHelper.loadDockerProperties();
    }

    @Test
    public void getPortShouldFailWhenNoFile() {
        expectedException.expect(IllegalStateException.class);
        DockerHelper.getPort("port");
    }

    @Test
    public void getBaseUrlShouldFailWhenNoFile() {
        expectedException.expect(IllegalStateException.class);
        DockerHelper.getBaseUrl("port", "context");
    }

    @Test
    public void loadDockerPropertiesShouldLoadExistingFile() throws IOException {
        final Properties expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertEquals(expected, DockerHelper.loadDockerProperties());

    }

    @Test
    public void getPortShouldFailWhenNoValue() throws IOException {
        final Properties expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        expectedException.expect(IllegalArgumentException.class);
        DockerHelper.getPort("p0");
    }

    @Test
    public void getPortShouldUseExistingValue() throws IOException {
        final Map<String, String> expected = new HashMap<>();
        expected.put("p1", "1234");
        expected.put("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertEquals(expected.get("p1"), DockerHelper.getPort("p1"));
    }

    @Test
    public void getBaseUrlShouldFailWhenNoValue() throws IOException {
        final Properties expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        expectedException.expect(IllegalArgumentException.class);
        DockerHelper.getBaseUrl("p0", "context");

    }

    @Test
    public void getBaseUrlShouldUseValueFromExistingFile() throws IOException {
        final Properties expected = new Properties();
        expected.setProperty("p1", "1234");
        expected.setProperty("p2", "5678");
        DockerHelper.storeDockerProperties(expected);

        assertEquals("http://localhost:5678/context/", DockerHelper.getBaseUrl("p2", "context"));

    }

}
