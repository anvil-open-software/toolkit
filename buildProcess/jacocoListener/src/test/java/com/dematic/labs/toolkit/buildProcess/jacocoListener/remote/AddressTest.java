/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;

import org.jacoco.core.runtime.AgentOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AddressTest {
    @Test
    public void defaultAddressShouldBeLocalhost() {
        assertEquals(Address.DEFAULT_HOST, Address.DEFAULT_ADDRESS.getHost());
        assertEquals(AgentOptions.DEFAULT_PORT, Address.DEFAULT_ADDRESS.getPort());
    }

    @Test
    public void emptyStringShouldFail() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new Address(""));
        assertEquals("Invalid agent address syntax \"\".", e.getMessage());
    }

    @Test
    public void portWithoutColonShouldFail() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new Address("4500"));
        assertEquals("Invalid agent address syntax \"4500\".", e.getMessage());
    }

    @Test
    public void portOnlyShouldDefaultToDefaultHost() {
        final var address = new Address(":4500");
        assertEquals(Address.DEFAULT_HOST, address.getHost());
        assertEquals(4500, address.getPort());
    }

    @Test
    public void hostAndPortShouldParse() {
        final var address = new Address("fancy.host.name:4500");
        assertEquals("fancy.host.name", address.getHost());
        assertEquals(4500, address.getPort());
    }

}
