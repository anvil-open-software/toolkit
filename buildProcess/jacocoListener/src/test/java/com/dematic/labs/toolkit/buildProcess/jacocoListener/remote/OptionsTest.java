/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OptionsTest {

    @Test
    public void creatorShouldRejectIrregularFormat() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new Options("destfile,append"));
        assertEquals("Invalid agent option syntax \"destfile,append\".", e.getMessage());
    }

    @Test
    public void creatorShouldRejectUnknownOptions() {
        final var e = assertThrows(IllegalArgumentException.class, () -> new Options("foo=42"));
        assertEquals("Unknown agent option \"foo\".", e.getMessage());
    }

    @Test
    public void creatorShouldConvertNullToEmptyPropertySet() {
        assertTrue(new Options(null).toString().isEmpty());
    }

    @Test
    public void creatorShouldConvertEmptyStringToEmptyPropertySet() {
        assertTrue(new Options("").toString().isEmpty());
    }

    @Test
    public void defaultAppendShouldBeToTrue() {
        assertTrue(new Options("").getAppend());
    }

    @Test
    public void testGetAppendTrue() {
        assertTrue(new Options("append=true").getAppend());
    }

    @Test
    public void testGetAppendFalse() {
        assertFalse(new Options("append=false").getAppend());
    }

    @Test
    public void destfileShouldHandleSpecialCharacters() {
        assertEquals("/var/foo, bar-1_0.exec", new Options("destfile=/var/foo, bar-1_0.exec").getDestfile());
    }

    @Test
    public void defaultDestfileShouldBeSet() {
        assertEquals("jacoco.exec", new Options("").getDestfile());
    }

    @Test
    public void defaultAddressesShouldBeLocal() {
        assertThat(new Options("").getAddresses(),
            contains(Address.DEFAULT_ADDRESS));
    }

    @Test
    public void aAddressesShouldRecognizeSingleEntry() {
        assertThat(new Options("addresses=host1:32456").getAddresses(),
            contains(new Address("host1:32456")));
    }

    @Test
    public void addressesShouldRecognizeList() {
        assertThat(new Options("addresses=host1:32456,:9876,host2:9876").getAddresses(),
            containsInAnyOrder(new Address("host1:32456"), new Address(":9876"), new Address("host2:9876")));
    }

    @Test
    public void toStringShouldShowAllFields() {
        assertEquals("addresses=localhost:9876,host1:32456,append=false,destfile=/var/foo",
            new Options("addresses=host1:32456,:9876,destfile=/var/foo,append=false").toString());
    }

}
