/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;

import javax.annotation.Nullable;

public class JacocoControllerError extends Error {
    public JacocoControllerError(@Nullable final String message) {
        super(message);
    }

    public JacocoControllerError(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    public JacocoControllerError(@Nullable final Throwable cause) {
        super(cause);
    }
}
