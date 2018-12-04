/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;

import org.jacoco.core.runtime.AgentOptions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;


public class AddressTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void defaultAddressShouldBeLocalhost() {
        assertEquals(Address.DEFAULT_HOST, Address.DEFAULT_ADDRESS.getHost());
        assertEquals(AgentOptions.DEFAULT_PORT, Address.DEFAULT_ADDRESS.getPort());
    }

    @Test
    public void emptyStringShouldFail() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid agent address syntax");
        new Address("");
    }

    @Test
    public void portWithoutColonShouldFail() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid agent address syntax");
        new Address("4500");
    }

    @Test
    public void portOnlyShouldDefaultToDefaultHost() {
        final Address address = new Address(":4500");
        assertEquals(Address.DEFAULT_HOST, address.getHost());
        assertEquals(4500, address.getPort());
    }

    @Test
    public void hostAndPortShouldParse() {
        final Address address = new Address("fancy.host.name:4500");
        assertEquals("fancy.host.name", address.getHost());
        assertEquals(4500, address.getPort());
    }

}
