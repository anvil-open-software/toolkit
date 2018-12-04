/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;

import org.jacoco.core.runtime.AgentOptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;


public class Address {
    public static final String DEFAULT_HOST = "localhost";
    public static final Address DEFAULT_ADDRESS = new Address(Address.DEFAULT_HOST, AgentOptions.DEFAULT_PORT);
    private final String host;
    private final int port;

    private Address(@Nonnull final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public Address(@Nonnull final String address) {
        final int pos = address.indexOf(':');
        if (pos == -1) {
            throw new IllegalArgumentException(format("Invalid agent address syntax \"%s\".", address));
        }
        host = pos == 0 ? DEFAULT_HOST : address.substring(0, pos);
        port = parseInt(address.substring(pos + 1));
    }

    @Nonnull
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return host + ':' + port;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Address address = (Address) o;
        return port == address.port && Objects.equals(host, address.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
