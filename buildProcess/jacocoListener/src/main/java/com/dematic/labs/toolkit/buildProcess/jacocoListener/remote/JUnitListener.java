/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;


import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import javax.annotation.Nonnull;

/**
 * JUnit listener that instructs JaCoCo to create one session per test.
 */
public class JUnitListener extends RunListener {

    private JacocoController jacoco = new JacocoController();

    @Override
    public void testStarted(@Nonnull final Description description) {
        jacoco.onTestStart();
    }

    @Override
    public void testFinished(@Nonnull final Description description) {
        jacoco.onTestFinish();
    }
}
