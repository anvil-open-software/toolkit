/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import ch.qos.logback.classic.Level;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.containsString;

public class SimpleLogAssertionTest {
    private final ExpectedException thrown = ExpectedException.none();
    private final SimpleLogAssertion simpleLogAssertion = new SimpleLogAssertion().recordAt(Level.DEBUG);
    @Rule
    public final RuleChain ruleChain = RuleChain.outerRule(thrown).around(simpleLogAssertion);
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLogAssertionTest.class);

    @Test
    public void testMatches() {
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class);
        LOGGER.debug("hello");
        simpleLogAssertion.assertThat(containsString("hello"));
        Assert.assertEquals(1, simpleLogAssertion.size());
    }

    @Test
    public void testMatchesMultipleLines() {
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class).recordFor(SimpleLogAssertionTest.class);
        LOGGER.debug("hello");
        LOGGER.debug("world");
        simpleLogAssertion.assertThat(containsString("hello"));
        simpleLogAssertion.assertThat(containsString("world"));
        Assert.assertEquals(2, simpleLogAssertion.size());
    }

    @Test
    public void testMatchesMultipleLinesMultipleLogger() {
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class).recordFor(SimpleLogAssertion.class);
        LOGGER.debug("hello");
        //noinspection LoggerInitializedWithForeignClass
        LoggerFactory.getLogger(SimpleLogAssertion.class).debug("world");
        simpleLogAssertion.assertThat(containsString("hello"));
        simpleLogAssertion.assertThat(containsString("world"));
    }

    @Test
    public void testDoNotMatch() {
        thrown.expect(AssertionError.class);
        simpleLogAssertion.recordFor(SimpleLogAssertionTest.class);
        LOGGER.debug("hi");
        simpleLogAssertion.assertThat(containsString("hello"));
    }

    @Test
    public void testUseOtherLog() {
        thrown.expect(AssertionError.class);
        simpleLogAssertion.recordFor(SimpleLogAssertion.class);
        LOGGER.debug("hello");
        simpleLogAssertion.assertThat(containsString("hello"));
    }
}
