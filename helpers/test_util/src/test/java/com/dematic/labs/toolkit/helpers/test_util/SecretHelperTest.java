/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.assertEquals;

public class SecretHelperTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void existingSecretCanBeRead() {
        assertEquals("password", SecretHelper.getSecret("PASSWORD"));
    }

    @Test
    public void missingSecretFailToRead() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(endsWith("src/test/docker/secrets/MISSING_SECRET"));
        SecretHelper.getSecret("MISSING_SECRET");
    }
}
