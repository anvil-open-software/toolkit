/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;


import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit listener that instructs JaCoCo to create one session per test.
 */
public class JUnitListener implements AfterTestExecutionCallback, BeforeTestExecutionCallback {

    private final JacocoController jacoco = new JacocoController();

    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        jacoco.onTestStart();
    }

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        jacoco.onTestFinish();
    }
}
