/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;

import org.jacoco.core.tools.ExecDumpClient;
import org.jacoco.core.tools.ExecFileLoader;

import java.io.File;
import java.io.IOException;

import static java.lang.System.getProperty;

public class JacocoController {

    private boolean testStarted;

    public void onTestStart() {
        if (testStarted) {
            throw new JacocoControllerError("Looks like several tests executed in parallel in the same JVM, thus coverage per test can't be recorded correctly.");
        }
        // Dump coverage between tests
        dump();
        testStarted = true;
    }

    public void onTestFinish() {
        // Dump coverage for test
        dump();
        testStarted = false;
    }

    private void dump() {
        final Options agentOptions = new Options(getProperty("com.dematic.labs.toolkit.buildProcess.jacocoListener.remote.agent"));
        final ExecDumpClient client = new ExecDumpClient();
        client.setReset(true);
        try {
            for (Address address : agentOptions.getAddresses()) {
                final ExecFileLoader dump = client.dump(address.getHost(), address.getPort());
                dump.save(new File(agentOptions.getDestfile()), agentOptions.getAppend());
            }
        } catch (IOException e) {
            throw new JacocoControllerError(e);
        }
    }

}
