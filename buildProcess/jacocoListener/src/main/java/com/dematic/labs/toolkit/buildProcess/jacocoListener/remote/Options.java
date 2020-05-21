/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */
package com.dematic.labs.toolkit.buildProcess.jacocoListener.remote;

import org.jacoco.core.runtime.AgentOptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.joining;
import static org.jacoco.core.runtime.AgentOptions.APPEND;
import static org.jacoco.core.runtime.AgentOptions.DESTFILE;

public class Options {
    /**
     * The remote servers to contact.
     */
    public static final String ADDRESSES = "addresses";

    private static final Collection<String> VALID_OPTIONS = Arrays.asList(ADDRESSES, APPEND, DESTFILE);

    private static final Pattern OPTION_SPLIT = Pattern.compile(",(?=\\w+=)");
    private static final Pattern COMMA_SPLIT = Pattern.compile(",");
    private final Map<String, String> options = new HashMap<>();
    private final Set<Address> addresses = new HashSet<>();

    public Options(@Nullable final String optionstr) {
        if (optionstr == null || optionstr.isEmpty()) {
            return;
        }

        for (final var entry : OPTION_SPLIT.split(optionstr)) {
            final var pos = entry.indexOf('=');
            if (pos == -1) {
                throw new IllegalArgumentException(format("Invalid agent option syntax \"%s\".", optionstr));
            }
            final var key = entry.substring(0, pos);
            if (!VALID_OPTIONS.contains(key)) {
                throw new IllegalArgumentException(format("Unknown agent option \"%s\".", key));
            }

            final var value = entry.substring(pos + 1);
            if (ADDRESSES.equals(key)) {
                for (final var address : COMMA_SPLIT.split(value)) {
                    addresses.add(new Address(address));
                }
            } else {
                options.put(key, value);
            }
        }
    }

    /**
     * Returns whether the output should be appended to an existing file.
     *
     * @return <code>true</code>, when the output should be appended
     */
    public boolean getAppend() {
        return parseBoolean(options.getOrDefault(APPEND, TRUE.toString()));
    }

    /**
     * Returns the output file location.
     *
     * @return output file location
     */
    @Nonnull
    public String getDestfile() {
        return options.getOrDefault(DESTFILE, AgentOptions.DEFAULT_DESTFILE);
    }

    public Set<Address> getAddresses() {
        return addresses.isEmpty() ? singleton(Address.DEFAULT_ADDRESS) : addresses;
    }

    @Override
    public String toString() {
        final var sb = new StringBuilder();
        if (!addresses.isEmpty()) {
            sb
                    .append(ADDRESSES)
                    .append('=')
                    .append(addresses.stream().map(Address::toString).collect(joining(","))
                    );
        }
        if (!options.isEmpty() && sb.length() > 0) {
            sb.append(',');
        }
        sb.append(options.entrySet().stream().map(e-> e.getKey() +"="+ e.getValue()).sorted().collect(joining(",")));
        return sb.toString();
    }
}
