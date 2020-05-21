package com.dematic.labs.toolkit.helpers.test_util;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SystemPropertyRuleTest {
    private static final String PROPERTY_NAME = "this_is_a_test_property";
    @RegisterExtension
    public final SystemPropertyRule systemPropertyRule = new SystemPropertyRule();

    @Test
    @Order(1)
    void putShouldAddAValue() {
        assertNull(System.getProperty(PROPERTY_NAME));
        systemPropertyRule.put(PROPERTY_NAME, "a value");
        assertEquals("a value", System.getProperty(PROPERTY_NAME));
    }

    @Test
    @Order(2)
    void ValueAddedInFirstTestShouldBeGone() {
        assertNull(System.getProperty(PROPERTY_NAME));
    }
}
