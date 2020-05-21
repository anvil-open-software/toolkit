/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SecretHelperTest {

    @Test
    public void existingSecretCanBeRead() {
        assertEquals("password", SecretHelper.getSecret("PASSWORD"));
    }

    @Test
    public void missingSecretFailToRead() {
        final var e = assertThrows(IllegalArgumentException.class, () -> SecretHelper.getSecret("MISSING_SECRET"));
        assertThat(e.getMessage(), endsWith("src/test/docker/secrets/MISSING_SECRET"));
    }
}
