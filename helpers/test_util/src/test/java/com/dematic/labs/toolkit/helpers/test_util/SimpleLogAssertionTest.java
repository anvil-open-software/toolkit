/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleLogAssertionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLogAssertionTest.class);
    @RegisterExtension
    public final SimpleLogAssertion simpleLogAssertion = new SimpleLogAssertion().recordAt(Level.DEBUG);

    @Test
    public void testMatches() {
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class);
        LOGGER.debug("hello");
        simpleLogAssertion.assertThat(containsString("hello"));
        assertEquals(1, simpleLogAssertion.size());
    }

    @Test
    public void testMatchesMultipleLines() {
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class).recordFor(SimpleLogAssertionTest.class);
        LOGGER.debug("hello");
        LOGGER.debug("world");
        simpleLogAssertion.assertThat(containsString("hello"));
        simpleLogAssertion.assertThat(containsString("world"));
        assertEquals(2, simpleLogAssertion.size());
    }

    @Test
    public void testMatchesMultipleLinesMultipleLogger() {
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class).recordFor(SimpleLogAssertion.class);
        LOGGER.debug("hello");
        LoggerFactory.getLogger(SimpleLogAssertion.class).debug("world");
        simpleLogAssertion.assertThat(containsString("hello"));
        simpleLogAssertion.assertThat(containsString("world"));
    }

    @Test
    public void testDoNotMatch() {
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class);
        LOGGER.debug("hi");
        assertThrows(AssertionError.class, () -> simpleLogAssertion.assertThat(containsString("hello")));
    }

    @Test
    public void testUseOtherLog() {
        simpleLogAssertion.recordFor(SimpleLogAssertion.class);
        LOGGER.debug("hello");
        assertThrows(AssertionError.class, () -> simpleLogAssertion.assertThat(containsString("hello")));
    }
}
