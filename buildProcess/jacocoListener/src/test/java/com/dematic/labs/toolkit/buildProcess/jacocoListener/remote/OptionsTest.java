/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void creatorShouldRejectIrregularFormat() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid agent option syntax");
        new Options("destfile,append");
    }

    @Test
    public void creatorShouldRejectUnknownOptions() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unknown agent option");
        new Options("foo=42");
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
    public void toStringShouldShowAllFields(){
        assertEquals("addresses=localhost:9876,host1:32456,append=false,destfile=/var/foo",
                new Options("addresses=host1:32456,:9876,destfile=/var/foo,append=false").toString());
    }

}
