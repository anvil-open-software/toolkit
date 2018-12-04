/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;


import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;

/**
 * Use by unit/integration tests to read the secrets. In order for tests to simulate an orchestrated environment,
 * we mount what should be secrets from the file system.
 */
public class SecretHelper {

    public static final String SECRETS_PATH = "src/test/docker/secrets";

    @Nonnull
    public static String getSecret(@Nonnull final String name) {
        final Path path = get(SECRETS_PATH, name);
        try {
            return lines(path).findFirst().orElseThrow(() -> new IllegalArgumentException("Undefined secret " + path));
        } catch (final IOException e) {
            throw new IllegalArgumentException("Undefined secret " + path, e);
        }
    }

}
