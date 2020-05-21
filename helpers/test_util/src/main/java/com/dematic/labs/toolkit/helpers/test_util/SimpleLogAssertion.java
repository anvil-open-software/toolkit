/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.test_util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.read.ListAppender;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SimpleLogAssertion implements AfterTestExecutionCallback, BeforeTestExecutionCallback {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SimpleLogAssertion.class);
    private final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    private final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

    private final List<Class<?>> loggingSources = new ArrayList<>();

    private Level level = Level.TRACE;

    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        for (final var logSource : loggingSources) {
            addAppenderToType(logSource);
        }
        listAppender.start();
    }

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        listAppender.stop();
        lc.reset();
        try {
            new ContextInitializer(lc).autoConfig();
        } catch (final JoranException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Nonnull
    public SimpleLogAssertion recordAt(@Nonnull final Level level) {
        this.level = level;
        return this;
    }

    @Nonnull
    public SimpleLogAssertion recordFor(@Nonnull final Class<?>... types) {
        for (final var type : types) {
            loggingSources.add(type);
            addAppenderToType(type);
        }
        return this;
    }

    public void assertThat(@Nonnull final Matcher<String> matcher) {
        MatcherAssert.assertThat(listAppender.list, CoreMatchers.hasItem(new TypeSafeMatcher<ILoggingEvent>() {
            @Override
            protected boolean matchesSafely(@Nonnull final ILoggingEvent item) {
                return matcher.matches(item.getFormattedMessage());
            }

            @Override
            public void describeTo(@Nonnull final Description description) {
                description.appendText("a log message with ").appendDescriptionOf(matcher);
            }
        }));
    }

    public int size() {
        return listAppender.list.size();
    }

    private void addAppenderToType(@Nonnull final Class<?> type) {
        final var logger = (Logger) LoggerFactory.getLogger(type);
        logger.addAppender(listAppender);
        logger.setLevel(level);
    }
}
